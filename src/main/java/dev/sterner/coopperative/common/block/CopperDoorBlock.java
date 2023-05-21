package dev.sterner.coopperative.common.block;

import dev.sterner.coopperative.common.util.CConversions;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class CopperDoorBlock extends AbstractCopperDoorBlock implements CWeatheringCopper {
    private final OxidationLevel weatherState;

    public CopperDoorBlock(OxidationLevel weatherState, Settings settings) {
        super(settings);
        this.weatherState = weatherState;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.UPPER && CConversions.getWeatheredVersion(state.getBlock()).isPresent();
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.tickDegradation(state, world, pos, random);
    }

    @Override
    public OxidationLevel getDegradationLevel() {
        return weatherState;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        insert(this, false, stacks, itemStack -> itemStack.getItem().equals(Items.IRON_DOOR), false);
        super.appendStacks(group, stacks);
    }


}
