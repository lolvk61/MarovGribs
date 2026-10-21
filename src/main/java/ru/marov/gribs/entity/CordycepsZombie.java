package ru.marov.gribs.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

/**
 * A zombie overgrown with cordyceps: a little tougher, faster and harder-hitting than a plain one.
 * A few zombies in the wild come out like this (see GameEvents#onFinalizeSpawn).
 */
public class CordycepsZombie extends Zombie {
    /** Chance that a zombie spawning naturally comes out overgrown. */
    public static final float INFECTION_CHANCE = 0.06F;

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
}
