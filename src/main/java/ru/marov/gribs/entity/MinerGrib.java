package ru.marov.gribs.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;
import ru.marov.gribs.registry.ModBlocks;

/**
 * Гриб-шахтёр: lives underground and digs tunnels on its own, putting the ore it breaks into its
 * backpack and lighting the way with glowcaps. When a player comes near it walls itself in with
 * cobblestone; dig it out and defeat it, and the backpack spills everything it has mined.
 * It only digs natural blocks (stone, dirt, gravel, sand, ores, cobblestone), never builds.
 */
public class MinerGrib extends PathfinderMob {
    /** Drawn at 14/16 of the model so it stays under a block tall and fits one-block-high gaps. */
    public static final float SCALE = 0.875F;
    public static final float HEIGHT = SCALE;
    public static final int BACKPACK_SIZE = 18;
    public static final double WALL_UP_RANGE = 8.0;
    public static final double CALM_RANGE = 16.0;
    public static final int CALM_TICKS = 100;
    public static final int GLOWCAP_EVERY = 10;
    public static final float MAX_HARDNESS = 4.5F;
    /** After this many dead ends in a row it stops digging for a while and wanders off instead. */
    public static final int GIVE_UP_AFTER = 4;
    public static final int REST_TICKS = 100;
    private static final EntityDataAccessor<Boolean> DATA_HIDING = SynchedEntityData.defineId(MinerGrib.class, EntityDataSerializers.BOOLEAN);

    private final SimpleContainer backpack = new SimpleContainer(BACKPACK_SIZE);
    @Nullable
    private Direction digDirection;
    private int tunnelLength;

    public MinerGrib(EntityType<? extends MinerGrib> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ARMOR, 2.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    /** Deep underground, in the dark, on natural stone. */
    public static boolean checkSpawnRules(EntityType<MinerGrib> type, ServerLevelAccessor level, MobSpawnType spawnType,
                                          BlockPos pos, RandomSource random) {
        return pos.getY() < 40 && level.getBrightness(LightLayer.SKY, pos) == 0
                && level.getBlockState(pos.below()).is(BlockTags.BASE_STONE_OVERWORLD)
                && Mob.checkMobSpawnRules(type, level, spawnType, pos, random);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_HIDING, false);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new WallUpGoal());
        goalSelector.addGoal(3, new DigTunnelGoal());
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public boolean isHiding() {
        return entityData.get(DATA_HIDING);
    }

    void setHiding(boolean hiding) {
        entityData.set(DATA_HIDING, hiding);
    }

    public SimpleContainer getBackpack() {
        return backpack;
    }

    @Nullable
    Player nearbyPlayer(double range) {
        return level().getNearestPlayer(getX(), getY(), getZ(), range, player -> !player.isSpectator());
    }

    static boolean canMine(Level level, BlockPos pos, BlockState state) {
        float hardness = state.getDestroySpeed(level, pos);
        return hardness >= 0.0F && hardness <= MAX_HARDNESS && level.getBlockEntity(pos) == null
                && state.getFluidState().isEmpty()
                && (state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(Tags.Blocks.ORES) || state.is(BlockTags.DIRT)
                || state.is(Tags.Blocks.GRAVELS) || state.is(BlockTags.SAND) || state.is(Tags.Blocks.COBBLESTONES)
                || state.is(Blocks.CALCITE) || state.is(Blocks.CLAY));
    }

    /** Breaks the block; ore goes into the backpack (spilling over if it is full), everything else is just gone. */
    void mine(BlockPos pos) {
        if (!(level() instanceof ServerLevel server)) {
            return;
        }
        BlockState state = server.getBlockState(pos);
        if (state.is(Tags.Blocks.ORES)) {
            for (ItemStack drop : Block.getDrops(state, server, pos, null, this, new ItemStack(Items.IRON_PICKAXE))) {
                ItemStack rest = backpack.addItem(drop);
                if (!rest.isEmpty()) {
                    spawnAtLocation(rest);
                }
            }
        }
        server.destroyBlock(pos, false, this);
    }

    private static Direction randomHorizontal(RandomSource random) {
        return Direction.Plane.HORIZONTAL.getRandomDirection(random);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        for (ItemStack stack : backpack.removeAllItems()) {
            spawnAtLocation(stack);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Backpack", backpack.createTag(registryAccess()));
        tag.putInt("TunnelLength", tunnelLength);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        backpack.fromTag(tag.getList("Backpack", Tag.TAG_COMPOUND), registryAccess());
        tunnelLength = tag.getInt("TunnelLength");
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.FUNGUS_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.FUNGUS_BREAK;
    }

    /**
     * Sees a player (or gets hit) and bricks itself up until things have been quiet for a while.
     * Walls go around wherever it stands once it has landed (a hit knocks it back first).
     */
    private class WallUpGoal extends Goal {
        private final List<BlockPos> toPlace = new ArrayList<>();
        private final List<BlockPos> wallOffsets = new ArrayList<>();
        private int calmTicks;
        private int placeDelay;

        WallUpGoal() {
            setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            Player player = nearbyPlayer(WALL_UP_RANGE);
            boolean seen = player != null && (hasLineOfSight(player) || distanceToSqr(player) < 16.0);
            boolean hurt = getLastHurtByMob() != null && tickCount - getLastHurtByMobTimestamp() < 60;
            return seen || hurt;
        }

        @Override
        public void start() {
            getNavigation().stop();
            setHiding(true);
            calmTicks = CALM_TICKS;
            placeDelay = 0;
            toPlace.clear();
            wallOffsets.clear();
            for (Direction side : Direction.Plane.HORIZONTAL) {
                wallOffsets.add(BlockPos.ZERO.relative(side));
                wallOffsets.add(BlockPos.ZERO.above().relative(side));
            }
            wallOffsets.add(BlockPos.ZERO.above(2));
            Player player = nearbyPlayer(CALM_RANGE);
            if (player != null) {
                Vec3 eye = player.getEyePosition().subtract(position());
                wallOffsets.sort(Comparator.comparingDouble(offset -> offset.distToCenterSqr(eye)));
            }
            toPlace.addAll(wallOffsets);
        }

        @Override
        public boolean canContinueToUse() {
            return calmTicks > 0;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            Player player = nearbyPlayer(CALM_RANGE);
            if (player == null) {
                calmTicks--;
            } else {
                calmTicks = CALM_TICKS;
                getLookControl().setLookAt(player);
            }
            if (toPlace.isEmpty() || !onGround() || --placeDelay > 0) {
                return;
            }
            placeDelay = 3;
            BlockPos pos = blockPosition().offset(toPlace.remove(0));
            BlockState wall = (pos.getY() < 0 ? Blocks.COBBLED_DEEPSLATE : Blocks.COBBLESTONE).defaultBlockState();
            if (EventHooks.canEntityGrief(level(), MinerGrib.this) && level().getBlockState(pos).canBeReplaced()
                    && level().isUnobstructed(wall, pos, CollisionContext.empty())) {
                level().setBlock(pos, wall, Block.UPDATE_ALL);
                level().playSound(null, pos, SoundEvents.STONE_PLACE, getSoundSource(), 1.0F, 0.9F);
                swing(InteractionHand.MAIN_HAND);
            }
        }

        @Override
        public void stop() {
            setHiding(false);
            toPlace.clear();
        }
    }

    /**
     * Digs a two-block-high tunnel one step at a time, turning now and then. Near the surface it
     * digs a staircase down instead, or straight ahead when the way down is blocked. Steers clear
     * of water and lava, and gives up for a while after a few dead ends so it never freezes.
     */
    private class DigTunnelGoal extends Goal {
        private final List<BlockPos> toBreak = new ArrayList<>();
        @Nullable
        private BlockPos breaking;
        @Nullable
        private BlockPos stepTo;
        private int breakTicks;
        private int breakTotal;
        private int walkTicks;
        private int deadEnds;
        private int restUntil;

        DigTunnelGoal() {
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return !isHiding() && onGround() && tickCount >= restUntil && EventHooks.canEntityGrief(level(), MinerGrib.this)
                    && random.nextInt(5) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return !isHiding() && deadEnds < GIVE_UP_AFTER && EventHooks.canEntityGrief(level(), MinerGrib.this);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            deadEnds = 0;
            if (digDirection == null) {
                digDirection = randomHorizontal(random);
            }
            planStep();
        }

        @Override
        public void stop() {
            if (deadEnds >= GIVE_UP_AFTER) {
                restUntil = tickCount + REST_TICKS;
            }
            if (breaking != null) {
                level().destroyBlockProgress(getId(), breaking, -1);
            }
            breaking = null;
            stepTo = null;
            toBreak.clear();
        }

        @Override
        public void tick() {
            if (breaking != null) {
                getLookControl().setLookAt(Vec3.atCenterOf(breaking));
                if (++breakTicks % 8 == 1) {
                    swing(InteractionHand.MAIN_HAND);
                }
                level().destroyBlockProgress(getId(), breaking, Math.min(9, breakTicks * 10 / breakTotal));
                if (breakTicks >= breakTotal) {
                    level().destroyBlockProgress(getId(), breaking, -1);
                    mine(breaking);
                    breaking = null;
                }
                return;
            }
            if (!toBreak.isEmpty()) {
                BlockPos next = toBreak.remove(0);
                BlockState state = level().getBlockState(next);
                if (state.canBeReplaced()) {
                    return;
                }
                if (canMine(level(), next, state)) {
                    breaking = next;
                    breakTicks = 0;
                    breakTotal = 10 + (int) (state.getDestroySpeed(level(), next) * 12.0F);
                } else {
                    turn();
                }
                return;
            }
            if (stepTo != null) {
                // Lands lower than planned if the step was over a ledge; that still counts.
                BlockPos at = blockPosition();
                if (at.getX() == stepTo.getX() && at.getZ() == stepTo.getZ() && onGround()) {
                    arrived();
                } else if (++walkTicks > 60) {
                    turn();
                } else {
                    // The navigator treats a neighbouring block as already reached, so steer directly.
                    getMoveControl().setWantedPosition(stepTo.getX() + 0.5, stepTo.getY(), stepTo.getZ() + 0.5, 1.0);
                }
                return;
            }
            planStep();
        }

        private void arrived() {
            deadEnds = 0;
            tunnelLength++;
            if (tunnelLength % GLOWCAP_EVERY == 0 && digDirection != null) {
                BlockPos behind = blockPosition().relative(digDirection.getOpposite());
                BlockState glowcap = ModBlocks.GLOWCAP.get().defaultBlockState();
                if (level().getBlockState(behind).isAir() && glowcap.canSurvive(level(), behind)) {
                    level().setBlock(behind, glowcap, Block.UPDATE_ALL);
                }
            }
            stepTo = null;
            planStep();
        }

        private void turn() {
            deadEnds++;
            digDirection = randomHorizontal(random);
            toBreak.clear();
            stepTo = null;
        }

        private void planStep() {
            if (digDirection == null || random.nextInt(8) == 0) {
                digDirection = randomHorizontal(random);
            }
            walkTicks = 0;
            BlockPos feet = blockPosition();
            BlockPos front = feet.relative(digDirection);
            boolean nearSurface = level().canSeeSky(feet) || feet.getY() > 40;
            boolean down = feet.getY() > level().getMinBuildHeight() + 8 && (nearSurface || random.nextInt(6) == 0);
            toBreak.clear();
            if (down && passable(front) && passable(front.below())) {
                toBreak.add(front);
                toBreak.add(front.below());
                stepTo = front.below();
            } else if (passable(front.above()) && passable(front)) {
                toBreak.add(front.above());
                toBreak.add(front);
                stepTo = front;
            } else {
                turn();
                return;
            }
            for (BlockPos pos : toBreak) {
                if (nearFluid(pos)) {
                    turn();
                    return;
                }
            }
        }

        /** Already open, or something it can dig through. */
        private boolean passable(BlockPos pos) {
            BlockState state = level().getBlockState(pos);
            return state.canBeReplaced() || canMine(level(), pos, state);
        }

        private boolean nearFluid(BlockPos pos) {
            for (Direction side : Direction.values()) {
                if (!level().getFluidState(pos.relative(side)).isEmpty()) {
                    return true;
                }
            }
            return !level().getFluidState(pos).isEmpty();
        }
    }
}
