package dev.sterner.coopperative.common.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.DropperBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.collection.DefaultedList;

public class WeatheringDropperBlock extends DropperBlock implements CWeatheringCopper {

    public final OxidationLevel oxidationLevel;

    public WeatheringDropperBlock(OxidationLevel oxidationLevel) {
        super(Settings.copy(Blocks.DROPPER).sounds(BlockSoundGroup.COPPER));
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    public OxidationLevel getDegradationLevel() {
        return oxidationLevel;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        insert(this, false, stacks, itemStack -> itemStack.getItem().equals(Items.DROPPER), true);
    }
}
