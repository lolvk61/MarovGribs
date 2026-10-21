package ru.marov.gribs.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Грибница: forest mycelium that generates outside mushroom islands. Spreads over dirt like
 * mycelium does, and mushrooms planted on it spread five times faster (see GribBlock).
 */
public class MarovMyceliumBlock extends SpreadingSnowyDirtBlock {
    public static final MapCodec<MarovMyceliumBlock> CODEC = simpleCodec(MarovMyceliumBlock::new);

    public MarovMyceliumBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends MarovMyceliumBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (random.nextInt(8) == 0) {
            level.addParticle(ParticleTypes.WHITE_ASH,
                    pos.getX() + random.nextDouble(), pos.getY() + 1.1, pos.getZ() + random.nextDouble(),
                    0.0, 0.0, 0.0);
        }
    }
}
