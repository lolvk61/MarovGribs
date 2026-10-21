package ru.marov.gribs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;
import ru.marov.gribs.registry.ModBlocks;
import ru.marov.gribs.registry.ModEntities;

/**
 * Dense mushroom beds come alive: when many mushrooms of one kind grow together, one of them now
 * and then turns into the mob that looks after that kind (lots of fly agarics give an agaric guard).
 */
public final class GribAwakening {
    public static final int RADIUS = 4;
    public static final int MIN_PATCH = 10;
    public static final int MAX_KEEPERS = 2;
    public static final double KEEPER_RANGE = 16.0;

    private GribAwakening() {
    }

    @Nullable
    public static EntityType<? extends Mob> keeperFor(BlockState state) {
        if (state.is(ModBlocks.FLY_AGARIC.get())) {
            return ModEntities.AGARIC_GUARD.get();
        }
        if (state.is(ModBlocks.HOPCAP.get())) {
            return ModEntities.HOPLING.get();
        }
        if (state.is(ModBlocks.PUFFBALL.get())) {
            return ModEntities.PUFFER.get();
        }
        if (state.is(ModBlocks.GLOWCAP.get())) {
            return ModEntities.MINER_GRIB.get();
        }
        if (state.is(ModBlocks.CHANTERELLE.get())) {
            return ModEntities.CHANTERELLE_GRIB.get();
        }
        if (state.is(ModBlocks.PARASOL_SPROUT.get())) {
            return ModEntities.PARASOL_GRIB.get();
        }
        return null;
    }

    public static int countPatch(Level level, BlockPos center, Block block) {
        int count = 0;
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-RADIUS, -1, -RADIUS), center.offset(RADIUS, 1, RADIUS))) {
            if (level.getBlockState(pos).is(block)) {
                count++;
            }
        }
        return count;
    }

    /** Turns the mushroom at {@code pos} into its keeper if the patch is big enough and not guarded yet. */
    @Nullable
    public static Mob tryAwaken(ServerLevel level, BlockPos pos, BlockState state) {
        EntityType<? extends Mob> type = keeperFor(state);
        if (type == null || countPatch(level, pos, state.getBlock()) < MIN_PATCH) {
            return null;
        }
        AABB area = new AABB(pos).inflate(KEEPER_RANGE);
        if (level.getEntitiesOfClass(Mob.class, area, mob -> mob.getType() == type && mob.isAlive()).size() >= MAX_KEEPERS) {
            return null;
        }
        Mob mob = type.create(level);
        if (mob == null) {
            return null;
        }
        level.removeBlock(pos, false);
        mob.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, level.random.nextFloat() * 360.0F, 0.0F);
        EventHooks.finalizeMobSpawn(mob, level, level.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null);
        level.addFreshEntityWithPassengers(mob);
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                15, 0.4, 0.4, 0.4, 0.0);
        level.playSound(null, pos, SoundEvents.FUNGUS_PLACE, SoundSource.BLOCKS, 1.0F, 0.7F);
        return mob;
    }
}
