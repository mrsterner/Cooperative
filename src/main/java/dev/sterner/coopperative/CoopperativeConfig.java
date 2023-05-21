package dev.sterner.coopperative;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.Collection;
import java.util.List;

public class CoopperativeConfig {

    public enum OverrideTarget {
        APPEARANCE, RECIPE, WEATHERING
    }

    private final static List<Block> overwrittenBlocks = List.of(
            Blocks.REPEATER,
            Blocks.COMPARATOR,
            Blocks.PISTON,
            Blocks.STICKY_PISTON,
            Blocks.DISPENSER,
            Blocks.DROPPER,
            Blocks.OBSERVER,
            Blocks.LEVER,
            Blocks.POWERED_RAIL
    );

    public static Collection<Block> getPossibleOverwrites() {
        return overwrittenBlocks;
    }
    
    public static boolean isOverwriteDisabled(Block block, OverrideTarget weathering){
        return false;
    }
}
