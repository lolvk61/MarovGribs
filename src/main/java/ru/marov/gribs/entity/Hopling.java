package ru.marov.gribs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import ru.marov.gribs.registry.ModEntities;
import ru.marov.gribs.registry.ModItems;

/**
 * Прыгунец: a little walking hopcap. Moves in hops, takes no fall damage, flees with
 * huge leaps, breeds on hopcaps and now and then sheds a fresh hopcap (like a chicken lays eggs).
 */
public class Hopling extends Animal {
    private static final int SHED_MIN_TICKS = 6000;
    private static final int SHED_RANDOM_TICKS = 6000;
    private int shedTime;

    public Hopling(EntityType<? extends Hopling> type, Level level) {
        super(type, level);
        shedTime = nextShedTime(random);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.JUMP_STRENGTH, 0.55);
    }

    public static boolean checkSpawnRules(EntityType<Hopling> type, LevelAccessor level, MobSpawnType spawnType,
                                          BlockPos pos, RandomSource random) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(BlockTags.DIRT) || below.is(BlockTags.MUSHROOM_GROW_BLOCK);
    }

    private static int nextShedTime(RandomSource random) {
        return SHED_MIN_TICKS + random.nextInt(SHED_RANDOM_TICKS);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, 1.6));
        goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        goalSelector.addGoal(3, new TemptGoal(this, 1.15, this::isFood, false));
        goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide || !isAlive()) {
            return;
        }
        if (onGround() && getNavigation().isInProgress() && random.nextInt(isPanicking() ? 2 : 5) == 0) {
            getJumpControl().jump();
        }
        if (!isBaby() && --shedTime <= 0) {
            playSound(SoundEvents.SLIME_SQUISH_SMALL, 1.0F, 1.4F);
            spawnAtLocation(ModItems.HOPCAP.get());
            gameEvent(GameEvent.ENTITY_PLACE);
            shedTime = nextShedTime(random);
        }
    }

    @Override
    protected float getJumpPower() {
        return super.getJumpPower() * (isPanicking() ? 1.7F : 1.0F);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItems.HOPCAP.get());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntities.HOPLING.get().create(level);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SLIME_HURT_SMALL;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH_SMALL;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.SLIME_SQUISH_SMALL, 0.3F, 1.3F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ShedTime", shedTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ShedTime")) {
            shedTime = tag.getInt("ShedTime");
        }
    }
}
