package ru.marov.gribs.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.marov.gribs.entity.SporeClouds;

/**
 * Дождевик: puffs a blinding spore cloud at anything that walks into it, then needs a few
 * seconds to refill. Sneaking past does not set it off.
 */
public class PuffballBlock extends GribBlock {
    public static final MapCodec<PuffballBlock> CODEC = simpleCodec(PuffballBlock::new);
    public static final BooleanProperty PUFFED = BooleanProperty.create("puffed");
    public static final int REFILL_TICKS = 100;
    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);

    public PuffballBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(PUFFED, false));
    }

    @Override
    protected MapCodec<? extends PuffballBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PUFFED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || state.getValue(PUFFED) || !(entity instanceof LivingEntity living)
                || living.isSteppingCarefully() || SporeClouds.isSporeImmune(living)) {
            return;
        }
        SporeClouds.spawn(level, Vec3.atBottomCenterOf(pos), 2.5F, 60, SporeClouds.PUFFBALL_COLOR, null,
                new MobEffectInstance(MobEffects.BLINDNESS, 60),
                new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
        level.setBlock(pos, state.setValue(PUFFED, true), Block.UPDATE_ALL);
        level.scheduleTick(pos, this, REFILL_TICKS);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(PUFFED)) {
            level.setBlock(pos, state.setValue(PUFFED, false), Block.UPDATE_ALL);
        }
    }
}
