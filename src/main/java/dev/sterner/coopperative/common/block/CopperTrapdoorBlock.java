package dev.sterner.coopperative.common.block;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;

public class CopperTrapdoorBlock extends AbstractCopperTrapdoorBlock implements CWeatheringCopper{

    private final OxidationLevel weatherState;

    public CopperTrapdoorBlock(OxidationLevel weatherState, Settings settings) {
        super(settings);
        this.weatherState = weatherState;
    }

    @Override
    public OxidationLevel getDegradationLevel() {
        return weatherState;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        insert(this, false, stacks, itemStack -> itemStack.getItem().equals(Items.IRON_TRAPDOOR), false);
        super.appendStacks(group, stacks);
    }
}
