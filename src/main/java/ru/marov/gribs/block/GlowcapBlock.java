package ru.marov.gribs.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/** Светлячок: glowing cave mushroom that also grows on bare stone. */
public class GlowcapBlock extends GribBlock {
    public static final MapCodec<GlowcapBlock> CODEC = simpleCodec(GlowcapBlock::new);

    public GlowcapBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends GlowcapBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos)
                || state.is(BlockTags.BASE_STONE_OVERWORLD)
                || state.is(BlockTags.MOSS_REPLACEABLE);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(4) == 0) {
            level.addParticle(ParticleTypes.GLOW,
                    pos.getX() + 0.3 + random.nextDouble() * 0.4,
                    pos.getY() + 0.4 + random.nextDouble() * 0.3,
                    pos.getZ() + 0.3 + random.nextDouble() * 0.4,
                    0.0, 0.02, 0.0);
        }
    }
}
