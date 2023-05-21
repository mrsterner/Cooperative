package dev.sterner.coopperative.common.block;

import dev.sterner.coopperative.common.util.CConversions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeverBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class WeatheringLeverBlock extends LeverBlock implements CWeatheringCopper {

    public final OxidationLevel oxidationLevel;

    public WeatheringLeverBlock(OxidationLevel oxidationLevel) {
        super(Settings.copy(Blocks.LEVER).sounds(BlockSoundGroup.COPPER));
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    public OxidationLevel getDegradationLevel() {
        return oxidationLevel;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return CConversions.getWeatheredVersion(state.getBlock()).isPresent() || super.hasRandomTicks(state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.tickDegradation(state, world, pos, random);
        super.randomTick(state, world, pos, random);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        insert(this, false, stacks, itemStack -> itemStack.getItem().equals(Items.LEVER), true);
    }
}
