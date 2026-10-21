package ru.marov.gribs.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.marov.gribs.registry.ModBlocks;

/**
 * Гриб-зонтик: a stem two or three blocks high, a wide flat cap and a knob on top, 4-5 blocks in
 * all. The cap is opaque, so the ground under it is out of the sun.
 */
public class HugeParasolFeature extends Feature<NoneFeatureConfiguration> {
    /** Squared radius of the cap: 37 blocks, a rough circle seven blocks across. */
    public static final int CAP_RADIUS_SQ = 10;

    public HugeParasolFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return grow(context.level(), context.origin(), context.random());
    }

    /** Grows a parasol with its stem at {@code origin}. Returns false and changes nothing if there is no room. */
    public static boolean grow(LevelAccessor level, BlockPos origin, RandomSource random) {
        BlockState ground = level.getBlockState(origin.below());
        if (!ground.is(BlockTags.DIRT) && !ground.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
            return false;
        }
        int stem = 2 + random.nextInt(2);
        for (int y = 0; y < stem; y++) {
            if (!isFree(level, origin.above(y), origin)) {
                return false;
            }
        }
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                if (inCap(dx, dz) && !isFree(level, origin.offset(dx, stem, dz), origin)
                        || inKnob(dx, dz) && !isFree(level, origin.offset(dx, stem + 1, dz), origin)) {
                    return false;
                }
            }
        }
        BlockState stemState = ModBlocks.PARASOL_STEM.get().defaultBlockState();
        BlockState capState = ModBlocks.PARASOL_CAP.get().defaultBlockState();
        for (int y = 0; y < stem; y++) {
            level.setBlock(origin.above(y), stemState, Block.UPDATE_ALL);
        }
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                if (inCap(dx, dz)) {
                    level.setBlock(origin.offset(dx, stem, dz), capState, Block.UPDATE_ALL);
                }
                if (inKnob(dx, dz)) {
                    level.setBlock(origin.offset(dx, stem + 1, dz), capState, Block.UPDATE_ALL);
                }
            }
        }
        return true;
    }

    private static boolean inCap(int dx, int dz) {
        return dx * dx + dz * dz <= CAP_RADIUS_SQ;
    }

    private static boolean inKnob(int dx, int dz) {
        return Math.abs(dx) + Math.abs(dz) <= 1;
    }

    private static boolean isFree(LevelAccessor level, BlockPos pos, BlockPos origin) {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || state.canBeReplaced() || pos.equals(origin) && state.is(ModBlocks.PARASOL_SPROUT.get());
    }
}
