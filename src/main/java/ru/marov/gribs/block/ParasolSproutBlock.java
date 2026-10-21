package ru.marov.gribs.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.marov.gribs.worldgen.HugeParasolFeature;

/** A young parasol mushroom. Bone meal grows it into a huge parasol that gives shade. */
public class ParasolSproutBlock extends GribBlock {
    public static final MapCodec<ParasolSproutBlock> CODEC = simpleCodec(ParasolSproutBlock::new);
    private static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 14.0, 12.0);

    public ParasolSproutBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends ParasolSproutBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.45F;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        HugeParasolFeature.grow(level, pos, random);
    }
}
