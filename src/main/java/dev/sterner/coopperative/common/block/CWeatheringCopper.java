package dev.sterner.coopperative.common.block;

import dev.sterner.coopperative.CoopperativeConfig;
import dev.sterner.coopperative.common.util.CConversions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.Optional;
import java.util.function.Predicate;

public interface CWeatheringCopper extends Oxidizable {
    default Optional<BlockState> getNext(BlockState state) {
        return CConversions.getWeatheredVersion(state.getBlock()).map((block) -> block.getStateWithProperties(state));
    }

    default float getChanceModifier() {
        return this.getDegradationLevel() == OxidationLevel.UNAFFECTED ? 0.75F : 1.0F;
    }

    default void insert(Block block, boolean before, DefaultedList<ItemStack> items, Predicate<ItemStack> filter, boolean startFromExposed) {
        if(CoopperativeConfig.isOverwriteDisabled(block, CoopperativeConfig.OverrideTarget.WEATHERING)) return;

        ItemStack stack = new ItemStack(block);
        OxidationLevel weatherState = ((CWeatheringCopper) block).getDegradationLevel();
        int offset = (weatherState.ordinal() - (startFromExposed ? 1 : 0)) * (before ? -1 : 1);
        if (items.stream().anyMatch(filter)) {
            Optional<ItemStack> optional = items.stream().filter(filter).max((a, b) ->
            {
                int valA = items.indexOf(a);
                int valB = items.indexOf(b);
                if (valA == -1 && valB == -1)
                    return 0;
                if (valA == -1)
                    return valB;
                if (valB == -1)
                    return valA;
                return before ? valB - valA : valA - valB;
            });
            if (optional.isPresent()) {
                items.add(items.indexOf(optional.get()) + (before ? 0 : 1) + offset, stack);
                return;
            }
        }
        items.add(stack);
    }
}
