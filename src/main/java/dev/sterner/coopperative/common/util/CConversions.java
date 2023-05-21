package dev.sterner.coopperative.common.util;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import dev.sterner.coopperative.Coopperative;
import dev.sterner.coopperative.CoopperativeConfig;
import dev.sterner.coopperative.common.registry.CObjects;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CConversions {
    private static final Supplier<BiMap<Block, Block>> WEATHERING_BLOCKS = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
            .putAll(blockMapFromArray(CObjects.COPPER_BRICKS))
            .putAll(blockMapFromArray(CObjects.COPPER_PILLAR))
            .putAll(blockMapFromArray(CObjects.COPPER_TILES))
            .putAll(blockMapFromArray(CObjects.TOGGLER))
            .putAll(blockMapFromArray(CObjects.HEADLIGHT))

            .put(Blocks.REPEATER, CObjects.EXPOSED_REPEATER)
            .put(CObjects.EXPOSED_REPEATER, CObjects.WEATHERED_REPEATER)
            .put(CObjects.WEATHERED_REPEATER, CObjects.OXIDIZED_REPEATER)

            .put(Blocks.COMPARATOR, CObjects.EXPOSED_COMPARATOR)
            .put(CObjects.EXPOSED_COMPARATOR, CObjects.WEATHERED_COMPARATOR)
            .put(CObjects.WEATHERED_COMPARATOR, CObjects.OXIDIZED_COMPARATOR)

            .put(Blocks.PISTON, CObjects.EXPOSED_PISTON)
            .put(CObjects.EXPOSED_PISTON, CObjects.WEATHERED_PISTON)
            .put(CObjects.WEATHERED_PISTON, CObjects.OXIDIZED_PISTON)

            .put(Blocks.STICKY_PISTON, CObjects.EXPOSED_STICKY_PISTON)
            .put(CObjects.EXPOSED_STICKY_PISTON, CObjects.WEATHERED_STICKY_PISTON)
            .put(CObjects.WEATHERED_STICKY_PISTON, CObjects.OXIDIZED_STICKY_PISTON)

            .put(Blocks.OBSERVER, CObjects.EXPOSED_OBSERVER)
            .put(CObjects.EXPOSED_OBSERVER, CObjects.WEATHERED_OBSERVER)
            .put(CObjects.WEATHERED_OBSERVER, CObjects.OXIDIZED_OBSERVER)

            .put(Blocks.DISPENSER, CObjects.EXPOSED_DISPENSER)
            .put(CObjects.EXPOSED_DISPENSER, CObjects.WEATHERED_DISPENSER)
            .put(CObjects.WEATHERED_DISPENSER, CObjects.OXIDIZED_DISPENSER)

            .put(Blocks.DROPPER, CObjects.EXPOSED_DROPPER)
            .put(CObjects.EXPOSED_DROPPER, CObjects.WEATHERED_DROPPER)
            .put(CObjects.WEATHERED_DROPPER, CObjects.OXIDIZED_DROPPER)

            .put(Blocks.LEVER, CObjects.EXPOSED_LEVER)
            .put(CObjects.EXPOSED_LEVER, CObjects.WEATHERED_LEVER)
            .put(CObjects.WEATHERED_LEVER, CObjects.OXIDIZED_LEVER)

            .put(Blocks.POWERED_RAIL, CObjects.EXPOSED_POWERED_RAIL)
            .put(CObjects.EXPOSED_POWERED_RAIL, CObjects.WEATHERED_POWERED_RAIL)
            .put(CObjects.WEATHERED_POWERED_RAIL, CObjects.OXIDIZED_POWERED_RAIL)

            .putAll(blockMapFromArray(CObjects.COPPER_DOORS))
            .putAll(blockMapFromArray(CObjects.COPPER_TRAPDOORS))
            .build());
    private static final Supplier<BiMap<Block, Block>> WAXED_BLOCKS = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
            .putAll(waxedEntries(CObjects.COPPER_BRICKS, CObjects.WAXED_COPPER_BRICKS))
            .putAll(waxedEntries(CObjects.COPPER_TILES, CObjects.WAXED_COPPER_TILES))
            .putAll(waxedEntries(CObjects.COPPER_PILLAR, CObjects.WAXED_COPPER_PILLAR))
            .putAll(waxedEntries(CObjects.COPPER_DOORS, CObjects.WAXED_COPPER_DOORS))
            .putAll(waxedEntries(CObjects.COPPER_TRAPDOORS, CObjects.WAXED_COPPER_TRAPDOORS))
            .build());

    public static Optional<Block> getWaxedVersion(Block block) {
        return Optional.ofNullable(WAXED_BLOCKS.get().get(block));
    }

    public static Optional<Block> getUnwaxedVersion(Block block) {
        return Optional.ofNullable(WAXED_BLOCKS.get().inverse().get(block));
    }

    public static Optional<Block> getWeatheredVersion(Block block) {
        if (CoopperativeConfig.isOverwriteDisabled(block, CoopperativeConfig.OverrideTarget.WEATHERING)) return Optional.empty();
        return Optional.ofNullable(WEATHERING_BLOCKS.get().get(block));
    }

    public static Optional<Block> getUnweatheredVersion(Block block) {
        return Optional.ofNullable(WEATHERING_BLOCKS.get().inverse().get(block));
    }

    public static Stream<Map.Entry<Block, Block>> getWaxedPairs() {
        return WAXED_BLOCKS.get().entrySet().stream();
    }

    public static Stream<Map.Entry<Block, Block>> getWeatheredPairs() {
        return WEATHERING_BLOCKS.get().entrySet().stream();
    }

    public static Block getFirst(Block block) {
        var first = block;

        while (true) {
            var previous = CConversions.getUnweatheredVersion(first);
            if (previous.isEmpty()) break;
            first = previous.get();
        }

        return first;
    }

    private static <T extends Block, R extends Block> ImmutableBiMap<Block, Block> waxedEntries(List<T> unwaxedList, List<R> waxedList) {
        if (unwaxedList.size() != waxedList.size())
            throw new IllegalArgumentException("waxed and unwaxed lists are not equals in size");

        ImmutableBiMap.Builder<Block, Block> map = new ImmutableBiMap.Builder<>();

        for (int i = 0; i < waxedList.size(); i++) {
            map.put(unwaxedList.get(i), waxedList.get(i));
        }

        return map.build();
    }

    private static <B extends Block> ImmutableBiMap<Block, Block> blockMapFromArray(List<B> blockArrayList) {
        ImmutableBiMap.Builder<Block, Block> map = new ImmutableBiMap.Builder<>();
        for (int i = 0; blockArrayList.size() - 1 > i; i++)
            map.put(blockArrayList.get(i), blockArrayList.get(i + 1));

        return map.build();
    }
}
