package dev.sterner.coopperative.common.item;

import dev.sterner.coopperative.common.util.CConversions;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PatinaItem extends Item {
    public PatinaItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        if (ctx.getPlayer() == null) return super.useOnBlock(ctx);
        World world = ctx.getWorld();
        PlayerEntity player = ctx.getPlayer();
        BlockState state = world.getBlockState(ctx.getBlockPos());
        Block block = state.getBlock();
        ItemStack stack = ctx.getStack();
        Hand hand = ctx.getHand();
        BlockPos pos = ctx.getBlockPos();
        if (block instanceof Oxidizable && Oxidizable.getIncreasedOxidationBlock(block).isPresent()) {
            Block nextWeatherBlock = Oxidizable.getIncreasedOxidationBlock(block).get();
            return apply(world, player, stack, hand, pos, state, nextWeatherBlock);
        }

        return CConversions.getWeatheredVersion(block).map(weathered ->
                apply(world, player, stack, hand, pos, state, weathered)
        ).orElseGet(() ->
                super.useOnBlock(ctx)
        );
    }

    private ActionResult apply(World world, PlayerEntity player, ItemStack stack, Hand hand, BlockPos pos, BlockState state, Block newBlock) {
        player.swingHand(hand);
        world.playSound(player, pos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.syncWorldEvent(player, 3005, pos, 0);
        world.setBlockState(pos, newBlock.getStateWithProperties(state), 11);

        if (!player.isCreative())
            stack.decrement(1);
        if (player instanceof ServerPlayerEntity)
            Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)player, pos, stack);
        return ActionResult.CONSUME;
    }
}
