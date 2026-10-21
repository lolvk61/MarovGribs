package ru.marov.gribs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

/**
 * Мухоморыч: a neutral fly agaric warrior. Leaves you alone until you hurt one of them or
 * pick fly agarics near them; then the whole group comes at you with poisonous punches.
 */
public class AgaricGuard extends PathfinderMob {
    public static final double GUARD_RADIUS = 16.0;
    /** One guard in this many is born with a smiley face instead of the grumpy one. */
    public static final int SMILEY_ODDS = 200;
    private static final DustParticleOptions ANGRY_DUST = new DustParticleOptions(new Vector3f(0.85F, 0.15F, 0.12F), 1.0F);
    private static final EntityDataAccessor<Boolean> DATA_SMILEY = SynchedEntityData.defineId(AgaricGuard.class, EntityDataSerializers.BOOLEAN);

    public AgaricGuard(EntityType<? extends AgaricGuard> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SMILEY, false);
    }

    public boolean isSmiley() {
        return entityData.get(DATA_SMILEY);
    }

    public void setSmiley(boolean smiley) {
        entityData.set(DATA_SMILEY, smiley);
    }

    /** NeoForge marks finalizeSpawn override-only, hence the deprecation. */
    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
                                        @Nullable SpawnGroupData spawnGroupData) {
        setSmiley(level.getRandom().nextInt(SMILEY_ODDS) == 0);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Smiley", isSmiley());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSmiley(tag.getBoolean("Smiley"));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ARMOR, 2.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.4)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    public static boolean checkSpawnRules(EntityType<AgaricGuard> type, LevelAccessor level, MobSpawnType spawnType,
                                          BlockPos pos, RandomSource random) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(BlockTags.DIRT) || below.is(BlockTags.MUSHROOM_GROW_BLOCK);
    }

    /** Called when a player picks a fly agaric: every guard nearby turns on them. */
    public static int alertGuards(Level level, BlockPos pos, Player player) {
        int alerted = 0;
        for (AgaricGuard guard : level.getEntitiesOfClass(AgaricGuard.class, new AABB(pos).inflate(GUARD_RADIUS))) {
            if (guard.getTarget() == null) {
                guard.setTarget(player);
                guard.playSound(SoundEvents.FUNGUS_PLACE, 1.0F, 0.6F);
                alerted++;
            }
        }
        return alerted;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.15, true));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.7));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 80), this);
        }
        return hit;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide && isAggressive() && random.nextInt(3) == 0) {
            level().addParticle(ANGRY_DUST, getRandomX(0.6), getY() + getBbHeight() * 0.85, getRandomZ(0.6), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
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
        playSound(SoundEvents.FUNGUS_STEP, 0.5F, 0.8F);
    }
}
