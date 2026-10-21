package ru.marov.gribs.entity;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import ru.marov.gribs.registry.ModEntities;
import ru.marov.gribs.registry.ModItems;

/** Spawns spore clouds for the puffball block, the Puffer and the spore bomb. */
public final class SporeClouds {
    /** Cream puff from a stepped-on puffball. */
    public static final int PUFFBALL_COLOR = 0xE6DCC0;
    /** Sickly yellow-green: the Puffer's cloud is poisonous. */
    public static final int PUFFER_COLOR = 0xB5C46E;
    /** Dusty grey from a thrown spore bomb. */
    public static final int BOMB_COLOR = 0xBDB6A8;

    private SporeClouds() {
    }

    /** Puffers, the death cap, and anyone wearing the death cap's hat shrug spore clouds off. */
    public static boolean isSporeImmune(LivingEntity entity) {
        return entity instanceof Puffer || entity instanceof DeathCap
                || entity.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.DEATH_CAP_HAT.get());
    }

    public static SporeCloud spawn(Level level, Vec3 at, float radius, int durationTicks, int color,
                                   @Nullable LivingEntity owner, MobEffectInstance... effects) {
        SporeCloud cloud = ModEntities.SPORE_CLOUD.get().create(level);
        cloud.moveTo(at.x, at.y, at.z);
        cloud.setup(radius, color, durationTicks, owner, effects);
        level.addFreshEntity(cloud);
        level.playSound(null, at.x, at.y, at.z, SoundEvents.PUFFER_FISH_BLOW_UP, SoundSource.NEUTRAL,
                1.0F, 0.7F + level.random.nextFloat() * 0.2F);
        if (level instanceof ServerLevel server) {
            DustParticleOptions dust = new DustParticleOptions(new Vector3f(
                    ((color >> 16) & 0xFF) / 255.0F, ((color >> 8) & 0xFF) / 255.0F, (color & 0xFF) / 255.0F), 1.2F);
            server.sendParticles(dust, at.x, at.y + radius * 0.4, at.z, 24, radius * 0.3, radius * 0.2, radius * 0.3, 0.05);
        }
        return cloud;
    }
}
