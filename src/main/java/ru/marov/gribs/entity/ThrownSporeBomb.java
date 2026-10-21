package ru.marov.gribs.entity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import ru.marov.gribs.registry.ModEntities;
import ru.marov.gribs.registry.ModItems;

/** Thrown puffball: bursts into a spore cloud that blinds, slows and weakens. */
public class ThrownSporeBomb extends ThrowableItemProjectile {
    public ThrownSporeBomb(EntityType<? extends ThrownSporeBomb> type, Level level) {
        super(type, level);
    }

    public ThrownSporeBomb(Level level, LivingEntity shooter) {
        super(ModEntities.SPORE_BOMB.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.SPORE_BOMB.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide) {
            SporeClouds.spawn(level(), position(), 3.0F, 100, SporeClouds.BOMB_COLOR,
                    getOwner() instanceof LivingEntity owner ? owner : null,
                    new MobEffectInstance(MobEffects.BLINDNESS, 100),
                    new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1),
                    new MobEffectInstance(MobEffects.WEAKNESS, 100));
            discard();
        }
    }
}
