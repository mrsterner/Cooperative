package dev.sterner.coopperative.common.block;

import dev.sterner.coopperative.common.block.entity.HeadlightBlockEntity;
import dev.sterner.coopperative.common.registry.CBlockEntityTypes;
import dev.sterner.coopperative.common.registry.CObjects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class HeadLightBlock extends FacingBlock implements CWeatheringCopper, BlockEntityProvider {

    private final OxidationLevel oxidationLevel;
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final BooleanProperty BROKEN = BooleanProperty.of("broken");

    public static final int RANGE = 30;

    public static final int LIGHT_LEVEL = 6;
    public static final ToIntFunction<BlockState> LIGHT_EMISSION = state -> isLit(state) ? LIGHT_LEVEL : 0;

    public HeadLightBlock(OxidationLevel oxidationLevel, Settings settings) {
        super(settings);
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, BROKEN);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HeadlightBlockEntity(pos, state);
    }

    @Override
    public OxidationLevel getDegradationLevel() {
        return oxidationLevel;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.with(POWERED, true), 2);
            spotlight(state, world, pos);
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            boolean currentlyLit = isLit(state);
            if (currentlyLit != world.isReceivingRedstonePower(pos)) {
                if (currentlyLit) {
                    world.createAndScheduleBlockTick(pos, this, 4);
                } else {
                    world.setBlockState(pos, state.with(POWERED, true), 2);
                    spotlight(state, world, pos);
                }
            }
        }
    }

    private static Optional<HeadlightBlockEntity> getTile(WorldAccess world, BlockPos pos) {
        return world.getBlockEntity(pos, CBlockEntityTypes.HEADLIGHT_BLOCK_ENTITY);
    }

    private void spotlight(BlockState state, WorldAccess world, BlockPos pos) {
        if (state.get(BROKEN)) return;
        getTile(world, pos).ifPresent(HeadlightBlockEntity::createSpotlight);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (isLit(state) && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.cycle(POWERED), 2);
            getTile(world, pos).ifPresent(HeadlightBlockEntity::extinguishSpotlight);
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (type == CBlockEntityTypes.HEADLIGHT_BLOCK_ENTITY && isLit(state)) {
            return (BlockEntityTicker<T>) createTicker();
        } else {
            return null;
        }
    }

    private BlockEntityTicker<HeadlightBlockEntity> createTicker() {
        return (world, pos, state, tile) -> tile.checkOcclusion();
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    public static boolean isLit(BlockState state) {
        return state.get(POWERED) && !state.get(BROKEN);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        insert(this, false, stacks, itemStack -> itemStack.getItem().equals(CObjects.OXIDIZED_OBSERVER.asItem()), false);
        super.appendStacks(group, stacks);
    }
}
