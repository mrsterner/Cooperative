package dev.sterner.coopperative.common.block;

import dev.sterner.coopperative.Coopperative;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SolderingTableBlock extends Block {

    private static final Text CONTAINER_TITLE = Text.translatable(Coopperative.MODID,"container.soldering");

    public SolderingTableBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient)
            return ActionResult.SUCCESS;

        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        //player.awardStat();
        return ActionResult.CONSUME;
    }



    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState blockState, World world, BlockPos blockPos) {
        return new SimpleNamedScreenHandlerFactory(
                (id, inventory, player) -> new CraftingScreenHandler(id, inventory, ScreenHandlerContext.create(world, blockPos)),
                CONTAINER_TITLE
        );
    }
}
