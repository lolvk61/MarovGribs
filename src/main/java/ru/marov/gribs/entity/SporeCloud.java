package ru.marov.gribs.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

/**
 * A puffy spore cloud resting on the ground. Bursts out over a few ticks, breathes, then thins out
 * and fades. Its effects reach anything inside its volume (a squashed sphere), not just the ground.
 */
public class SporeCloud extends Entity {
    public static final int BURST_TICKS = 6;
    public static final int FADE_TICKS = 24;
    /** Height of the cloud relative to its width: a cloud is wider than it is tall. */
    public static final float FLATTEN = 0.7F;
    private static final int REAPPLY_TICKS = 20;
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(SporeCloud.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(SporeCloud.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_DURATION = SynchedEntityData.defineId(SporeCloud.class, EntityDataSerializers.INT);

    private final List<MobEffectInstance> effects = new ArrayList<>();
    private final Map<UUID, Integer> nextApplyTick = new HashMap<>();
    @Nullable
    private LivingEntity owner;

    public SporeCloud(EntityType<? extends SporeCloud> type, Level level) {
        super(type, level);
        noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_RADIUS, 2.5F);
        builder.define(DATA_COLOR, 0xE6DCC0);
        builder.define(DATA_DURATION, 60);
    }

    public void setup(float radius, int color, int durationTicks, @Nullable LivingEntity owner, MobEffectInstance... effects) {
        entityData.set(DATA_RADIUS, radius);
        entityData.set(DATA_COLOR, color);
        entityData.set(DATA_DURATION, Math.max(durationTicks, BURST_TICKS + FADE_TICKS));
        this.owner = owner;
        this.effects.clear();
        this.effects.addAll(List.of(effects));
    }

    public float getRadius() {
        return entityData.get(DATA_RADIUS);
    }

    public int getColor() {
        return entityData.get(DATA_COLOR);
    }

    public int getDuration() {
        return entityData.get(DATA_DURATION);
    }

    /** 0..1: how far into the fade-out the cloud is. */
    public float getFade(float partialTick) {
        float age = tickCount + partialTick;
        return Mth.clamp((age - (getDuration() - FADE_TICKS)) / FADE_TICKS, 0.0F, 1.0F);
    }

    /** Radius right now: pops out fast, breathes a little, spreads thin while fading. */
    public float getCurrentRadius(float partialTick) {
        float age = tickCount + partialTick;
        float burst = Mth.clamp(age / BURST_TICKS, 0.0F, 1.0F);
        float popOut = 1.0F - (1.0F - burst) * (1.0F - burst) * (1.0F - burst);
        float breath = 1.0F + Mth.sin(age * 0.15F) * 0.03F;
        return getRadius() * (0.35F + 0.65F * popOut) * breath * (1.0F + 0.2F * getFade(partialTick));
    }

    /** The cloud sits on the ground, so its centre is half a radius up. */
    public Vec3 getCenter(float radius) {
        return position().add(0.0, radius * 0.5, 0.0);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount >= getDuration()) {
            if (!level().isClientSide) {
                discard();
            }
            return;
        }
        float radius = getCurrentRadius(0.0F);
        Vec3 center = getCenter(radius);
        if (level().isClientSide) {
            spawnMotes(radius, center);
        } else if (tickCount % 5 == 0) {
            applyEffects(radius, center);
        }
    }

    private void spawnMotes(float radius, Vec3 center) {
        if (random.nextFloat() > 0.6F - getFade(0.0F) * 0.4F) {
            return;
        }
        int color = getColor();
        DustParticleOptions dust = new DustParticleOptions(new Vector3f(
                ((color >> 16) & 0xFF) / 255.0F, ((color >> 8) & 0xFF) / 255.0F, (color & 0xFF) / 255.0F), 0.8F);
        double angle = random.nextDouble() * Math.PI * 2.0;
        double dist = radius * (0.6 + random.nextDouble() * 0.5);
        level().addParticle(dust, center.x + Math.cos(angle) * dist, center.y + (random.nextDouble() - 0.3) * radius * FLATTEN,
                center.z + Math.sin(angle) * dist, 0.0, 0.0, 0.0);
    }

    private void applyEffects(float radius, Vec3 center) {
        if (effects.isEmpty()) {
            return;
        }
        float height = radius * FLATTEN;
        AABB search = new AABB(center.x - radius, center.y - height, center.z - radius,
                center.x + radius, center.y + height, center.z + radius);
        for (LivingEntity target : level().getEntitiesOfClass(LivingEntity.class, search)) {
            if (!target.isAffectedByPotions() || SporeClouds.isSporeImmune(target) || !touches(target.getBoundingBox(), center, radius, height)) {
                continue;
            }
            Integer next = nextApplyTick.get(target.getUUID());
            if (next != null && tickCount < next) {
                continue;
            }
            nextApplyTick.put(target.getUUID(), tickCount + REAPPLY_TICKS);
            Entity source = owner != null && owner.isAlive() ? owner : this;
            for (MobEffectInstance effect : effects) {
                target.addEffect(new MobEffectInstance(effect), source);
            }
        }
    }

    /** Whether a box overlaps the cloud's ellipsoid (radius wide, height tall). */
    static boolean touches(AABB box, Vec3 center, float radius, float height) {
        double dx = (Mth.clamp(center.x, box.minX, box.maxX) - center.x) / radius;
        double dy = (Mth.clamp(center.y, box.minY, box.maxY) - center.y) / height;
        double dz = (Mth.clamp(center.z, box.minZ, box.maxZ) - center.z) / radius;
        return dx * dx + dy * dy + dz * dz <= 1.0;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        float radius = getRadius() * 1.3F;
        return new AABB(getX() - radius, getY() - 0.5, getZ() - radius, getX() + radius, getY() + radius * 1.5, getZ() + radius);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    // Clouds live a few seconds and are never saved (see ModEntities: noSave).
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }
}
