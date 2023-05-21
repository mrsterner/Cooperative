package dev.sterner.coopperative.common.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.function.ToIntFunction;

public class SpotLightBlock extends AirBlock implements Waterloggable {

    public static final int MAX_LEVEL = 15;
    public static final IntProperty LEVEL = Properties.LEVEL_15;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final ToIntFunction<BlockState> LIGHT_EMISSION = (state) -> state.get(LEVEL);

    public SpotLightBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(LEVEL, MAX_LEVEL).with(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL, WATERLOGGED);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    public BlockState createState(WorldAccess world, BlockPos pos) {
        if (world.getFluidState(pos).isOf(Fluids.WATER)) {
            return getDefaultState().with(WATERLOGGED, true);
        } else {
            return getDefaultState();
        }
    }

}
