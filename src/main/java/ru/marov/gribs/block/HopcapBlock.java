package ru.marov.gribs.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/** Прыгунок: springy cap that tosses whatever steps on it into the air. */
public class HopcapBlock extends GribBlock {
    public static final MapCodec<HopcapBlock> CODEC = simpleCodec(HopcapBlock::new);
    public static final double LIVING_BOOST = 0.95;

    public HopcapBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HopcapBlock> codec() {
        return CODEC;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        Vec3 motion = entity.getDeltaMovement();
        if (motion.y > 0.0 || entity.isSuppressingBounce() || entity.getY() - pos.getY() > 0.5) {
            return;
        }
        entity.setDeltaMovement(motion.x, entity instanceof LivingEntity ? LIVING_BOOST : 0.5, motion.z);
        entity.resetFallDistance();
        entity.hasImpulse = true;
        // The bouncing player hears it locally; everyone else gets it from the server.
        level.playSound(entity instanceof Player player ? player : null, pos, SoundEvents.SLIME_JUMP_SMALL,
                SoundSource.BLOCKS, 0.7F, 1.1F + level.random.nextFloat() * 0.3F);
    }
}
