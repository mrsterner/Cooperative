package dev.sterner.coopperative.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class AbstractCopperTrapdoorBlock extends TrapdoorBlock {
    public AbstractCopperTrapdoorBlock(Settings settings) {
        super(settings);
    }

    private int getCloseSound() {
        return 1011;
    }

    private int getOpenSound() {
        return  1005;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(POWERED)) return ActionResult.PASS;
        state = state.cycle(OPEN);
        world.setBlockState(pos, state, 10);
        if (state.get(WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        world.syncWorldEvent(player, state.get(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
        world.emitGameEvent(player, state.get(OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return ActionResult.success(world.isClient());
    }
}
