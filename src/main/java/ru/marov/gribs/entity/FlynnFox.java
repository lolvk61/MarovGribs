package ru.marov.gribs.entity;

import java.util.EnumSet;
import java.util.UUID;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import ru.marov.gribs.registry.ModEntities;

/**
 * FlynnFlyTaggart: what a fox becomes when it picks up a chanterelle. A fearless, armoured fox in a
 * marine's helmet that never sleeps, follows the player who was nearest when it changed, and hunts
 * monsters; every kill sends it into a short frenzy of speed and strength.
 */
public class FlynnFox extends Fox {
    public static final String NAME = "FlynnFlyTaggart";
    private static final int FRENZY_TICKS = 100;
    @Nullable
    private UUID ownerId;

    public FlynnFox(EntityType<? extends Fox> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Fox.createAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.36)
                .add(Attributes.ATTACK_DAMAGE, 7.0)
                .add(Attributes.ARMOR, 6.0)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    /** Replaces {@code fox} with FlynnFlyTaggart, with a (harmless) lightning strike for drama. */
    @Nullable
    public static FlynnFox transform(Fox fox) {
        if (!(fox.level() instanceof ServerLevel level)) {
            return null;
        }
        FlynnFox flynn = ModEntities.FLYNN_FOX.get().create(level);
        if (flynn == null) {
            return null;
        }
        flynn.moveTo(fox.getX(), fox.getY(), fox.getZ(), fox.getYRot(), fox.getXRot());
        flynn.finalizeSpawn(level, level.getCurrentDifficultyAt(fox.blockPosition()), MobSpawnType.CONVERSION, null);
        flynn.setVariant(fox.getVariant());
        flynn.setAge(0);
        Player owner = level.getNearestPlayer(fox, 16.0);
        if (owner != null) {
            flynn.setOwner(owner.getUUID());
        }
        fox.discard();
        level.addFreshEntity(flynn);
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            bolt.moveTo(Vec3.atBottomCenterOf(flynn.blockPosition()));
            bolt.setVisualOnly(true);
            level.addFreshEntity(bolt);
        }
        level.sendParticles(ParticleTypes.FLAME, flynn.getX(), flynn.getY() + 0.4, flynn.getZ(), 30, 0.4, 0.3, 0.4, 0.05);
        return flynn;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // Doom's marine fears nothing and never naps.
        goalSelector.removeAllGoals(goal -> goal instanceof AvoidEntityGoal<?> || goal.getClass().getSimpleName().equals("SleepGoal"));
        goalSelector.addGoal(5, new FollowOwnerGoal());
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, 10, true, false,
                monster -> !(monster instanceof Creeper)));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
                                        @Nullable SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        setCustomName(Component.literal(NAME));
        setCustomNameVisible(true);
        setPersistenceRequired();
        return data;
    }

    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity victim) {
        if (victim instanceof Monster) {
            addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, FRENZY_TICKS, 1));
            addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, FRENZY_TICKS, 0));
            playSound(SoundEvents.FOX_AGGRO, 1.5F, 0.8F);
        }
        return super.killedEntity(level, victim);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Nullable
    public Player getOwner() {
        return ownerId == null ? null : level().getPlayerByUUID(ownerId);
    }

    public void setOwner(@Nullable UUID ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (ownerId != null) {
            tag.putUUID("FlynnOwner", ownerId);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        ownerId = tag.hasUUID("FlynnOwner") ? tag.getUUID("FlynnOwner") : null;
    }

    /** Keeps up with its player; if they get far away it simply catches up. */
    private class FollowOwnerGoal extends Goal {
        FollowOwnerGoal() {
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            Player owner = getOwner();
            return owner != null && !owner.isSpectator() && getTarget() == null && distanceToSqr(owner) > 100.0;
        }

        @Override
        public boolean canContinueToUse() {
            Player owner = getOwner();
            return owner != null && getTarget() == null && distanceToSqr(owner) > 9.0;
        }

        @Override
        public void tick() {
            Player owner = getOwner();
            if (owner == null) {
                return;
            }
            getLookControl().setLookAt(owner, 10.0F, getMaxHeadXRot());
            if (distanceToSqr(owner) > 1024.0) {
                moveTo(owner.getX(), owner.getY(), owner.getZ(), getYRot(), getXRot());
                getNavigation().stop();
            } else {
                getNavigation().moveTo(owner, 1.3);
            }
        }
    }
}
