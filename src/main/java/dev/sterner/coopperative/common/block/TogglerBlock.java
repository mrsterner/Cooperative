package dev.sterner.coopperative.common.block;

import dev.sterner.coopperative.common.registry.CObjects;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;


public class TogglerBlock extends AbstractRedstoneGateBlock implements CWeatheringCopper {

    public final OxidationLevel oxidationLevel;
    public static BooleanProperty POWERING = BooleanProperty.of("powering");

    public TogglerBlock(OxidationLevel oxidationLevel, Settings settings) {
        super(settings);
        this.oxidationLevel = oxidationLevel;
        this.setDefaultState(this.getDefaultState().with(POWERING, false).with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED, POWERING, FACING);
    }

    @Override
    protected int getUpdateDelayInternal(BlockState state) {
        return 1;
    }


    public int getSignal(BlockState blockState, @NotNull BlockView blockAccess, @NotNull BlockPos pos, @NotNull Direction side) {
        return blockState.get(FACING) == side ? this.getOutputLevel(blockAccess, pos, blockState) : 0;
    }

    @Override
    public OxidationLevel getDegradationLevel() {
        return oxidationLevel;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.canModifyBlocks())
            return ActionResult.PASS;
        if (player.isSneaking())
            return ActionResult.PASS;

        return this.activated(world, pos, state);
    }

    @Override
    protected int getOutputLevel(BlockView world, BlockPos pos, BlockState state) {
        return state.get(POWERING) ? 15 : 0;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean poweredPreviously = state.get(POWERED);
        super.scheduledTick(state, world, pos, random);
        BlockState newState = world.getBlockState(pos);
        if (newState.get(POWERED) && !poweredPreviously) {
            this.activated(world, pos, newState);
        }
    }

    protected ActionResult activated(World world, BlockPos pos, BlockState state) {
        if (!world.isClient()) {
            float pitch = !(Boolean)state.get(POWERING) ? 1.0F : 0.6F;
            world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_CLUSTER_PLACE, SoundCategory.BLOCKS, 0.3F, pitch);
            world.setBlockState(pos, state.cycle(POWERING), 2);
        }

        return ActionResult.SUCCESS;
    }

    public boolean canConnectRedstone(BlockState state, BlockView world, BlockPos pos, Direction side) {
        if (side == null)
            return false;

        return side.getAxis() == state.get(FACING).getAxis();
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        insert(this, false, stacks, itemStack -> itemStack.getItem().equals(CObjects.OXIDIZED_COMPARATOR.asItem()), false);
    }
}
