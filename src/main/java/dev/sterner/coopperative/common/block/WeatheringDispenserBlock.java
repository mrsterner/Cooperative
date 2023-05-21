package dev.sterner.coopperative.common.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.collection.DefaultedList;

public class WeatheringDispenserBlock extends DispenserBlock implements CWeatheringCopper {

    public final OxidationLevel oxidationLevel;

    public WeatheringDispenserBlock(OxidationLevel oxidationLevel) {
        super(Settings.copy(Blocks.DISPENSER).sounds(BlockSoundGroup.COPPER));
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    public OxidationLevel getDegradationLevel() {
        return oxidationLevel;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        insert(this, false, stacks, itemStack -> itemStack.getItem().equals(Items.DISPENSER), true);
    }
}
