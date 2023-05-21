package dev.sterner.coopperative.common.block;

import dev.sterner.coopperative.common.util.CConversions;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

public class AbstractCopperDoorBlock extends DoorBlock {
    public AbstractCopperDoorBlock(Settings settings) {
        super(settings);
    }

    private int getCloseSound() {
        return 1011;
    }

    private int getOpenSound() {
        return 1005;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(POWERED)) return ActionResult.PASS;
        state = state.cycle(OPEN);
        world.setBlockState(pos, state, 10);
        world.syncWorldEvent(player, state.get(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
        world.emitGameEvent(player, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return ActionResult.success(world.isClient);
    }


    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient()) {
            var otherState = world.getBlockState(neighborPos);
            if (otherState.getBlock() instanceof AbstractCopperDoorBlock) updateOtherHalf(otherState, world, neighborPos);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    protected void updateOtherHalf(BlockState state, WorldAccess world, BlockPos pos) {
        BlockPos otherHalfPos = state.get(HALF).equals(DoubleBlockHalf.UPPER) ? pos.down() : pos.up();
        BlockState otherHalfState = world.getBlockState(otherHalfPos);

        if (otherHalfState.isOf(state.getBlock())) return;

        var waxed = CConversions.getWaxedVersion(state.getBlock());
        var unwaxed = CConversions.getUnwaxedVersion(state.getBlock());

        if (waxed.filter(otherHalfState::isOf).isPresent()) {
            world.syncWorldEvent(3004, otherHalfPos, 0);
        } else if (unwaxed.filter(otherHalfState::isOf).isPresent()) {
            world.syncWorldEvent(3003, otherHalfPos, 0);
        } else {
            world.syncWorldEvent(3005, otherHalfPos, 0);
        }

        var newState = state.getBlock().getStateWithProperties(otherHalfState);
        world.setBlockState(otherHalfPos, newState, 2);
    }
}
