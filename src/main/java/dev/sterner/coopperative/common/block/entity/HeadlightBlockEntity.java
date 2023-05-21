package dev.sterner.coopperative.common.block.entity;

import dev.sterner.coopperative.common.block.HeadLightBlock;
import dev.sterner.coopperative.common.block.SpotLightBlock;
import dev.sterner.coopperative.common.registry.CBlockEntityTypes;
import dev.sterner.coopperative.common.registry.CObjects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class HeadlightBlockEntity extends BlockEntity {

    private @Nullable BlockPos savedSpotlight;

    public HeadlightBlockEntity(BlockPos pos, BlockState state) {
        super(CBlockEntityTypes.HEADLIGHT_BLOCK_ENTITY, pos, state);
    }

    public void checkOcclusion() {
        if (!hasWorld()) return;
        if (world.getTime() % 10 != 0) return;

        var currentTarget = findTarget();
        var different = currentTarget.isEmpty() || !currentTarget.get().equals(savedSpotlight);

        if (different) {
            if (savedSpotlight != null) extinguishSpotlight();
            currentTarget.ifPresent(this::placeSpotlight);
        }
    }

    public void createSpotlight() {
        if (!hasWorld()) return;

        var targetPos = findTarget();
        targetPos.ifPresent(this::placeSpotlight);
    }

    private void placeSpotlight(BlockPos at) {
        world.setBlockState(at, CObjects.SPOT_LIGHT.createState(world, at), 2);
        saveSpotlight(at);
    }

    public void extinguishSpotlight() {
        if (!hasWorld()) return;

        getSavedSpotlight().ifPresent(targetPos -> {
            var spotlight = world.getBlockState(targetPos);
            if (spotlight.isOf(CObjects.SPOT_LIGHT)) {
                world.removeBlock(targetPos, false);
                forgetSpotlight();
            }
        });
    }

    private Optional<BlockPos> findTarget() {
        Direction facing = getCachedState().get(HeadLightBlock.FACING);

        BlockPos openSpace = null;

        for (int i = 1; HeadLightBlock.RANGE > i; i++) {
            BlockPos targetPos = getPos().offset(facing, i);
            var state = world.getBlockState(targetPos);
            if(isReplaceable(state)) openSpace = targetPos;
            if (!shinesThrough(state)) {
                return Optional.ofNullable(openSpace);
            }
        }

        return Optional.empty();
    }

    private boolean shinesThrough(BlockState state) {
        return state.isAir() || !state.isOpaque();
    }

    private boolean isReplaceable(BlockState state) {
        if (state.isAir() || state.isOf(CObjects.SPOT_LIGHT)) return true;
        if(state.isOf(Blocks.WATER)) return state.getFluidState().isStill();
        return false;
    }

    public Optional<BlockPos> getSavedSpotlight() {
        return Optional.ofNullable(savedSpotlight);
    }

    private void saveSpotlight(BlockPos pos) {
        savedSpotlight = pos;
    }

    private void forgetSpotlight() {
        savedSpotlight = null;
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        extinguishSpotlight();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("ActiveSpotlight")) {
            savedSpotlight = NbtHelper.toBlockPos(nbt.getCompound("ActiveSpotlight"));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        if (savedSpotlight != null) {
            nbt.put("ActiveSpotlight", NbtHelper.fromBlockPos(savedSpotlight));
        }
    }
}
