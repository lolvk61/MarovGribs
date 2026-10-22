package ru.marov.gribs.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * A zombie overgrown with cordyceps: a little tougher, faster and harder-hitting than a plain one.
 * A few zombies in the wild come out like this (see GameEvents#onFinalizeSpawn). Each one gets one
 * of {@link #GROWTH_VARIANTS} patterns of stalks sprouting from its face, head, body, arms and legs.
 */
public class CordycepsZombie extends Zombie {
    /** Chance that a zombie spawning naturally comes out overgrown. */
    public static final float INFECTION_CHANCE = 0.06F;
    /** Must match CORDYCEPS_VARIANTS in art/gribs_art.py. */
    public static final int GROWTH_VARIANTS = 10;
    private static final EntityDataAccessor<Integer> DATA_GROWTH = SynchedEntityData.defineId(CordycepsZombie.class, EntityDataSerializers.INT);

    public CordycepsZombie(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 26.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 4.5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_GROWTH, 0);
    }

    public int getGrowthVariant() {
        return entityData.get(DATA_GROWTH);
    }

    public void setGrowthVariant(int variant) {
        entityData.set(DATA_GROWTH, Math.floorMod(variant, GROWTH_VARIANTS));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
                                        @Nullable SpawnGroupData spawnGroupData) {
        setGrowthVariant(level.getRandom().nextInt(GROWTH_VARIANTS));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("GrowthVariant", getGrowthVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setGrowthVariant(tag.getInt("GrowthVariant"));
    }
}
