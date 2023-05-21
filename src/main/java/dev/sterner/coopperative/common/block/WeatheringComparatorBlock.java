package dev.sterner.coopperative.common.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.collection.DefaultedList;


public class WeatheringComparatorBlock extends ComparatorBlock implements CWeatheringCopper {

    public final OxidationLevel oxidationLevel;

    public WeatheringComparatorBlock(OxidationLevel oxidationLevel) {
        super(Settings.copy(Blocks.COMPARATOR).sounds(BlockSoundGroup.COPPER));
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    public OxidationLevel getDegradationLevel() {
        return oxidationLevel;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        insert(this, false, stacks, itemStack -> itemStack.getItem().equals(Items.COMPARATOR), true);
    }
}
