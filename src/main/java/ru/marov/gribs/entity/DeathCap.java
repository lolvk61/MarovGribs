package ru.marov.gribs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import ru.marov.gribs.registry.ModBlocks;
import ru.marov.gribs.registry.ModEntities;
import ru.marov.gribs.registry.ModItems;

/**
 * Бледная поганка: the dark forest boss. Fights in melee with poisonous blows and on top of that:
 * <ul>
 *     <li>every few seconds bursts into a deadly spore cloud (poison II, wither, nausea);</li>
 *     <li>roots its target in place with mycelium threads;</li>
 *     <li>calls puffers at 2/3 health and agaric guards at 1/3;</li>
 *     <li>below half health flies into a rage: attacks more often, turns the ground around it into
 *     mycelium and heals while standing on it, so lure it off.</li>
 * </ul>
 * Drops its cap, a helmet that keeps poison and spore clouds off the wearer.
 */
public class DeathCap extends Monster {
    public static final int DEATH_CAP_COLOR = 0xC8D39C;
    public static final float SCALE = 1.4F;
    /** Natural spawns are rare on top of the spawn weight: only one try in this many succeeds. */
    public static final int SPAWN_ODDS = 30;
    public static final double SOLITUDE_RANGE = 128.0;
    private static final DustParticleOptions ROOT_DUST = new DustParticleOptions(new Vector3f(0.93F, 0.92F, 0.85F), 1.0F);
    private static final EntityDataAccessor<Boolean> DATA_ENRAGED = SynchedEntityData.defineId(DeathCap.class, EntityDataSerializers.BOOLEAN);

    private final ServerBossEvent bossEvent = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.GREEN,
            BossEvent.BossBarOverlay.NOTCHED_10);
    private int novaCooldown = 100;
    private int rootCooldown = 80;
    private boolean calledPuffers;
    private boolean calledGuards;

    public DeathCap(EntityType<? extends DeathCap> type, Level level) {
        super(type, level);
        xpReward = 60;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 250.0)
                .add(Attributes.ARMOR, 8.0)
                .add(Attributes.ATTACK_DAMAGE, 9.0)
                .add(Attributes.MOVEMENT_SPEED, 0.24)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    /** At night in the dark forest, on the surface, rarely, and never two in one area. */
    public static boolean checkSpawnRules(EntityType<DeathCap> type, ServerLevelAccessor level, MobSpawnType spawnType,
                                          BlockPos pos, RandomSource random) {
        if (!Monster.checkMonsterSpawnRules(type, level, spawnType, pos, random)) {
            return false;
        }
        if (spawnType != MobSpawnType.NATURAL) {
            return true;
        }
        return random.nextInt(SPAWN_ODDS) == 0 && level.canSeeSky(pos)
                && level.getEntitiesOfClass(DeathCap.class, new AABB(pos).inflate(SOLITUDE_RANGE)).isEmpty();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ENRAGED, false);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 16.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public boolean isEnraged() {
        return entityData.get(DATA_ENRAGED);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        float health = getHealth() / getMaxHealth();
        bossEvent.setProgress(health);
        if (health < 0.5F && !isEnraged()) {
            entityData.set(DATA_ENRAGED, true);
            playSound(SoundEvents.RAVAGER_ROAR, 2.0F, 0.6F);
        }
        LivingEntity target = getTarget();
        if (target != null && target.isAlive()) {
            if (--novaCooldown <= 0) {
                sporeNova();
            }
            if (--rootCooldown <= 0 && distanceToSqr(target) < 400.0 && hasLineOfSight(target)) {
                root(target);
            }
        }
        if (health < 2.0F / 3.0F && !calledPuffers) {
            calledPuffers = true;
            summon(ModEntities.PUFFER.get(), 3, target);
        }
        if (health < 1.0F / 3.0F && !calledGuards) {
            calledGuards = true;
            summon(ModEntities.AGARIC_GUARD.get(), 2, target);
        }
        if (isEnraged() && tickCount % 20 == 0) {
            if (isOnMycelium()) {
                heal(2.0F);
            }
            if (tickCount % 40 == 0) {
                spreadMycelium();
            }
        }
    }

    public void sporeNova() {
        novaCooldown = isEnraged() ? 100 : 160;
        SporeClouds.spawn(level(), position(), 5.0F, 100, DEATH_CAP_COLOR, this,
                new MobEffectInstance(MobEffects.POISON, 120, 1),
                new MobEffectInstance(MobEffects.WITHER, 80),
                new MobEffectInstance(MobEffects.CONFUSION, 140));
        swing(InteractionHand.MAIN_HAND);
    }

    public void root(LivingEntity target) {
        rootCooldown = isEnraged() ? 100 : 140;
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 4), this);
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 60), this);
        playSound(SoundEvents.ROOTED_DIRT_BREAK, 1.5F, 0.5F);
        if (level() instanceof ServerLevel server) {
            Vec3 from = position().add(0.0, 0.3, 0.0);
            Vec3 to = target.position().add(0.0, 0.3, 0.0);
            for (int i = 0; i <= 20; i++) {
                Vec3 p = from.lerp(to, i / 20.0);
                server.sendParticles(ROOT_DUST, p.x, p.y, p.z, 2, 0.05, 0.05, 0.05, 0.0);
            }
            server.sendParticles(ROOT_DUST, to.x, to.y, to.z, 30, 0.5, 0.2, 0.5, 0.0);
        }
    }

    private void summon(EntityType<? extends Mob> type, int count, @Nullable LivingEntity target) {
        if (!(level() instanceof ServerLevel server)) {
            return;
        }
        playSound(SoundEvents.EVOKER_PREPARE_SUMMON, 2.0F, 0.7F);
        for (int i = 0; i < count; i++) {
            Mob minion = type.create(server);
            if (minion == null) {
                continue;
            }
            double angle = random.nextDouble() * Math.PI * 2.0;
            minion.moveTo(getX() + Math.cos(angle) * 3.0, getY(), getZ() + Math.sin(angle) * 3.0, random.nextFloat() * 360.0F, 0.0F);
            EventHooks.finalizeMobSpawn(minion, server, server.getCurrentDifficultyAt(minion.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
            if (target != null) {
                minion.setTarget(target);
            }
            server.addFreshEntityWithPassengers(minion);
            server.sendParticles(ParticleTypes.POOF, minion.getX(), minion.getY() + 0.5, minion.getZ(), 10, 0.3, 0.3, 0.3, 0.02);
        }
    }

    private boolean isOnMycelium() {
        BlockState below = level().getBlockState(blockPosition().below());
        return below.is(ModBlocks.MAROV_MYCELIUM.get()) || below.is(Blocks.MYCELIUM);
    }

    private void spreadMycelium() {
        if (!EventHooks.canEntityGrief(level(), this)) {
            return;
        }
        BlockPos pos = blockPosition().offset(random.nextInt(9) - 4, -1, random.nextInt(9) - 4);
        BlockState state = level().getBlockState(pos);
        if ((state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.PODZOL)) && level().getBlockState(pos.above()).isAir()) {
            level().setBlock(pos, ModBlocks.MAROV_MYCELIUM.get().defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1), this);
        }
        return hit;
    }

    /** Immune to its own poisons. NeoForge marks this override-only, hence the deprecation. */
    @Override
    @SuppressWarnings("deprecation")
    public boolean canBeAffected(MobEffectInstance effect) {
        if (effect.is(MobEffects.POISON) || effect.is(MobEffects.WITHER) || effect.is(MobEffects.CONFUSION)
                || effect.is(MobEffects.MOVEMENT_SLOWDOWN)) {
            return false;
        }
        return super.canBeAffected(effect);
    }

    /** NeoForge marks finalizeSpawn override-only, hence the deprecation. */
    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
                                        @Nullable SpawnGroupData spawnGroupData) {
        setPersistenceRequired();
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        spawnAtLocation(ModItems.DEATH_CAP_HAT.get());
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossEvent.removePlayer(player);
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        bossEvent.setName(getDisplayName());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("CalledPuffers", calledPuffers);
        tag.putBoolean("CalledGuards", calledGuards);
        tag.putBoolean("Enraged", isEnraged());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        calledPuffers = tag.getBoolean("CalledPuffers");
        calledGuards = tag.getBoolean("CalledGuards");
        entityData.set(DATA_ENRAGED, tag.getBoolean("Enraged"));
        if (hasCustomName()) {
            bossEvent.setName(getDisplayName());
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SCULK_CATALYST_BLOOM;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.FUNGUS_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WARDEN_DEATH;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide && isEnraged() && random.nextInt(3) == 0) {
            level().addParticle(ROOT_DUST, getRandomX(0.8), getY() + getBbHeight() * Mth.nextFloat(random, 0.3F, 0.9F),
                    getRandomZ(0.8), 0.0, 0.02, 0.0);
        }
    }
}
