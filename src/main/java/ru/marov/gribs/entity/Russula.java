package ru.marov.gribs.entity;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IShearable;
import org.jetbrains.annotations.Nullable;
import ru.marov.gribs.item.RussulaItem;
import ru.marov.gribs.registry.ModEntities;

/**
 * Сыроежка: a sheep-like mushroom. Its cap comes in dye colours, can be dyed and sheared off for
 * coloured russula caps, and grows back after it nibbles some grass. Breeds on bone meal.
 */
public class Russula extends Animal implements IShearable {
    private static final EntityDataAccessor<Byte> DATA_FLAGS = SynchedEntityData.defineId(Russula.class, EntityDataSerializers.BYTE);
    private static final int COLOR_MASK = 0x0F;
    private static final int SHEARED_BIT = 0x10;
    private static final DyeColor[] NATURAL_COLORS = {
            DyeColor.RED, DyeColor.RED, DyeColor.RED, DyeColor.RED, DyeColor.RED,
            DyeColor.PINK, DyeColor.PINK, DyeColor.PINK, DyeColor.YELLOW, DyeColor.YELLOW, DyeColor.YELLOW,
            DyeColor.PURPLE, DyeColor.PURPLE, DyeColor.GREEN, DyeColor.GREEN, DyeColor.BROWN, DyeColor.BROWN,
            DyeColor.WHITE, DyeColor.WHITE, DyeColor.ORANGE,
    };

    private EatBlockGoal eatBlockGoal;
    private int eatAnimationTick;

    public Russula(EntityType<? extends Russula> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.23);
    }

    public static boolean checkSpawnRules(EntityType<Russula> type, LevelAccessor level, MobSpawnType spawnType,
                                          BlockPos pos, RandomSource random) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(BlockTags.DIRT) || below.is(BlockTags.MUSHROOM_GROW_BLOCK);
    }

    public static DyeColor randomNaturalColor(RandomSource random) {
        return NATURAL_COLORS[random.nextInt(NATURAL_COLORS.length)];
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLAGS, (byte) DyeColor.RED.getId());
    }

    @Override
    protected void registerGoals() {
        eatBlockGoal = new EatBlockGoal(this);
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        goalSelector.addGoal(3, new TemptGoal(this, 1.1, this::isFood, false));
        goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        goalSelector.addGoal(5, eatBlockGoal);
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected void customServerAiStep() {
        eatAnimationTick = eatBlockGoal.getEatAnimationTick();
        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        if (level().isClientSide) {
            eatAnimationTick = Math.max(0, eatAnimationTick - 1);
        }
        super.aiStep();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 10) {
            eatAnimationTick = 40;
        } else {
            super.handleEntityEvent(id);
        }
    }

    /** 0..1 how far it is bent down to nibble grass. */
    public float getEatProgress(float partialTick) {
        if (eatAnimationTick <= 0) {
            return 0.0F;
        }
        float t = eatAnimationTick - partialTick;
        return t >= 4 && t <= 36 ? 1.0F : (t < 4 ? t / 4.0F : (40 - t) / 4.0F);
    }

    @Override
    public void ate() {
        super.ate();
        setSheared(false);
        if (isBaby()) {
            ageUp(60);
        }
    }

    /** Shears (in hand or in a dispenser) go through NeoForge's IShearable. */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof DyeItem dye && isAlive() && !isSheared() && getColor() != dye.getDyeColor()) {
            if (!level().isClientSide) {
                setColor(dye.getDyeColor());
                level().playSound(null, this, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                stack.consume(1, player);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isShearable(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
        return isAlive() && !isSheared() && !isBaby();
    }

    /** Called on both sides; NeoForge spawns the returned caps on the server. */
    @Override
    public List<ItemStack> onSheared(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
        if (!level.isClientSide) {
            level.playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.2F);
        }
        setSheared(true);
        List<ItemStack> caps = new ArrayList<>();
        int count = 1 + random.nextInt(3);
        for (int i = 0; i < count; i++) {
            caps.add(RussulaItem.of(getColor(), 1));
        }
        return caps;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        if (!isSheared() && !isBaby()) {
            spawnAtLocation(RussulaItem.of(getColor(), 1));
        }
    }

    public DyeColor getColor() {
        return DyeColor.byId(entityData.get(DATA_FLAGS) & COLOR_MASK);
    }

    public void setColor(DyeColor color) {
        byte flags = entityData.get(DATA_FLAGS);
        entityData.set(DATA_FLAGS, (byte) (flags & ~COLOR_MASK | color.getId() & COLOR_MASK));
    }

    public boolean isSheared() {
        return (entityData.get(DATA_FLAGS) & SHEARED_BIT) != 0;
    }

    public void setSheared(boolean sheared) {
        byte flags = entityData.get(DATA_FLAGS);
        entityData.set(DATA_FLAGS, (byte) (sheared ? flags | SHEARED_BIT : flags & ~SHEARED_BIT));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.BONE_MEAL);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        Russula child = ModEntities.RUSSULA.get().create(level);
        if (child != null) {
            DyeColor color = otherParent instanceof Russula other && random.nextBoolean() ? other.getColor() : getColor();
            child.setColor(color);
        }
        return child;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
                                        @Nullable SpawnGroupData spawnGroupData) {
        setColor(randomNaturalColor(level.getRandom()));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Sheared", isSheared());
        tag.putByte("Color", (byte) getColor().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSheared(tag.getBoolean("Sheared"));
        setColor(DyeColor.byId(tag.getByte("Color")));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.FUNGUS_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.FUNGUS_BREAK;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.FUNGUS_STEP, 0.4F, 1.2F);
    }
}
