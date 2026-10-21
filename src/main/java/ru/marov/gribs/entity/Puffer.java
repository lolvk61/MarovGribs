package ru.marov.gribs.entity;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Пыхтун: a hostile puffball. Waddles up to you, swells and bursts a poison-blinding spore
 * cloud without dying, then needs time to refill. Hitting it in melee makes it puff at once.
 */
public class Puffer extends Monster {
    public static final int MAX_SWELL = 24;
    public static final int PUFF_COOLDOWN = 120;
    private static final byte PUFF_EVENT = 99;
    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(Puffer.class, EntityDataSerializers.INT);

    private int oldSwell;
    private int swell;
    private int puffCooldown;

    public Puffer(EntityType<? extends Puffer> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 16.0)
                .add(Attributes.MOVEMENT_SPEED, 0.22)
                .add(Attributes.FOLLOW_RANGE, 20.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SWELL_DIR, -1);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new SwellGoal());
        goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, false));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        if (isAlive()) {
            oldSwell = swell;
            if (puffCooldown > 0) {
                puffCooldown--;
            }
            int dir = getSwellDir();
            if (dir > 0 && swell == 0) {
                playSound(SoundEvents.PUFFER_FISH_BLOW_UP, 0.8F, 0.5F);
            }
            swell = Mth.clamp(swell + dir, 0, MAX_SWELL);
            if (swell >= MAX_SWELL && !level().isClientSide) {
                puff();
            }
        }
        super.tick();
    }

    public void puff() {
        swell = 0;
        oldSwell = 0;
        setSwellDir(-1);
        puffCooldown = PUFF_COOLDOWN;
        SporeClouds.spawn(level(), position(), 3.0F, 80, SporeClouds.PUFFER_COLOR, this,
                new MobEffectInstance(MobEffects.POISON, 100),
                new MobEffectInstance(MobEffects.BLINDNESS, 50));
        level().broadcastEntityEvent(this, PUFF_EVENT);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == PUFF_EVENT) {
            swell = 0;
            oldSwell = 0;
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && !level().isClientSide && isAlive() && puffCooldown == 0
                && source.getDirectEntity() instanceof LivingEntity) {
            puff();
        }
        return hurt;
    }

    /** The Puffer never hits; its only weapon is the spore cloud. */
    @Override
    public boolean doHurtTarget(Entity target) {
        return false;
    }

    /** Immune to its own spores. NeoForge marks this override-only, hence the deprecation. */
    @Override
    @SuppressWarnings("deprecation")
    public boolean canBeAffected(MobEffectInstance effect) {
        if (effect.is(MobEffects.POISON) || effect.is(MobEffects.BLINDNESS)) {
            return false;
        }
        return super.canBeAffected(effect);
    }

    public float getSwelling(float partialTick) {
        return Mth.clamp(Mth.lerp(partialTick, (float) oldSwell, (float) swell) / (MAX_SWELL - 2), 0.0F, 1.0F);
    }

    public int getPuffCooldown() {
        return puffCooldown;
    }

    public int getSwellDir() {
        return entityData.get(DATA_SWELL_DIR);
    }

    public void setSwellDir(int dir) {
        entityData.set(DATA_SWELL_DIR, dir);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SLIME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PUFFER_FISH_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.WOOL_STEP, 0.4F, 0.8F);
    }

    private class SwellGoal extends Goal {
        SwellGoal() {
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = getTarget();
            return puffCooldown == 0 && (getSwellDir() > 0 || target != null && distanceToSqr(target) < 9.0);
        }

        @Override
        public void start() {
            getNavigation().stop();
        }

        @Override
        public void stop() {
            setSwellDir(-1);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = getTarget();
            boolean inReach = target != null && distanceToSqr(target) <= 25.0 && getSensing().hasLineOfSight(target);
            setSwellDir(inReach ? 1 : -1);
        }
    }
}
