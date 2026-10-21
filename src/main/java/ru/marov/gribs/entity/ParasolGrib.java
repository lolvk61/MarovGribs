package ru.marov.gribs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import ru.marov.gribs.registry.ModTags;

/**
 * Гриб-зонтик: a towering, slow, peaceful parasol mushroom, about 4.6 blocks tall. Sun-burning
 * mobs standing under its cap don't burn, and in daylight they come looking for it
 * (see SeekParasolShadeGoal).
 */
public class ParasolGrib extends PathfinderMob {
    public static final float SCALE = 2.0F;
    /** Horizontal reach of the cap from the stem. */
    public static final double SHADE_RADIUS = 2.0;
    private static final int SHADE_EFFECT_TICKS = 40;

    public ParasolGrib(EntityType<? extends ParasolGrib> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.18)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    public static boolean checkSpawnRules(EntityType<ParasolGrib> type, LevelAccessor level, MobSpawnType spawnType,
                                          BlockPos pos, RandomSource random) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(BlockTags.DIRT) || below.is(BlockTags.MUSHROOM_GROW_BLOCK);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    /** The area under the cap. */
    public AABB getShadeArea() {
        return new AABB(getX() - SHADE_RADIUS, getY(), getZ() - SHADE_RADIUS,
                getX() + SHADE_RADIUS, getY() + getBbHeight() - 0.5, getZ() + SHADE_RADIUS);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide || tickCount % 10 != 0) {
            return;
        }
        // A mob can't really block sunlight, so the shade works as fire resistance plus putting out the flames.
        for (Mob mob : level().getEntitiesOfClass(Mob.class, getShadeArea(), m -> m.getType().is(ModTags.SEEKS_PARASOL_SHADE))) {
            mob.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, SHADE_EFFECT_TICKS, 0, true, false), this);
            if (!mob.isInLava()) {
                mob.clearFire();
            }
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
        playSound(SoundEvents.FUNGUS_STEP, 0.6F, 0.6F);
    }
}
