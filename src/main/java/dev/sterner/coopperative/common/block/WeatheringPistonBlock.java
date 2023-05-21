package dev.sterner.coopperative.common.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.collection.DefaultedList;

public class WeatheringPistonBlock extends PistonBlock implements CWeatheringCopper {

    private OxidationLevel oxidationLevel;
    private final boolean isSticky;

    public WeatheringPistonBlock(OxidationLevel oxidationLevel, boolean sticky) {
        super(sticky, Settings.copy(sticky ? Blocks.STICKY_PISTON : Blocks.PISTON).sounds(BlockSoundGroup.COPPER));
        this.oxidationLevel = oxidationLevel;
        this.isSticky = sticky;
    }

    @Override
    public OxidationLevel getDegradationLevel() {
        return oxidationLevel;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        insert(this, false, stacks, itemStack -> itemStack.getItem().equals(this.isSticky ? Items.STICKY_PISTON : Items.PISTON), true);
    }

}
