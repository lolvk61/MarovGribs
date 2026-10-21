package ru.marov.gribs.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.marov.gribs.registry.ModBlocks;

/**
 * Base mushroom plant: grows on dirt-like blocks, slowly spreads like vanilla mushrooms (five times
 * faster on Marov mycelium), sprouts a neighbour when bone-mealed and wakes dense patches up.
 */
public class GribBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<GribBlock> CODEC = simpleCodec(GribBlock::new);
    /** One random tick in this many tries to wake the patch up (see GribAwakening). */
    public static final int AWAKEN_CHANCE = 16;
    private static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 7.0, 11.0);

    public GribBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends GribBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT) || state.is(BlockTags.MUSHROOM_GROW_BLOCK);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(AWAKEN_CHANCE) == 0 && GribAwakening.tryAwaken(level, pos, state) != null) {
            return;
        }
        boolean onMycelium = level.getBlockState(pos.below()).is(ModBlocks.MAROV_MYCELIUM.get());
        if (random.nextInt(onMycelium ? 5 : 25) != 0) {
            return;
        }
        int neighbours = 5;
        for (BlockPos near : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
            if (level.getBlockState(near).is(this) && --neighbours <= 0) {
                return;
            }
        }
        trySprout(state, level, pos, random, 4);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.6F;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        trySprout(state, level, pos, random, 8);
    }

    private void trySprout(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, int attempts) {
        for (int i = 0; i < attempts; i++) {
            BlockPos target = pos.offset(random.nextInt(5) - 2, random.nextInt(3) - 1, random.nextInt(5) - 2);
            if (level.isEmptyBlock(target) && defaultBlockState().canSurvive(level, target)) {
                level.setBlock(target, defaultBlockState(), Block.UPDATE_CLIENTS);
                return;
            }
        }
    }
}
