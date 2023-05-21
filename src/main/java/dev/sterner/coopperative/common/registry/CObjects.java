package dev.sterner.coopperative.common.registry;

import dev.sterner.coopperative.Coopperative;
import dev.sterner.coopperative.common.block.*;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface CObjects {
    Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
    Map<Item, Identifier> ITEMS = new LinkedHashMap<>();


    SpotLightBlock SPOT_LIGHT = register("spot_light", new SpotLightBlock(AbstractBlock.Settings.of(Material.AIR).noCollision().dropsNothing().nonOpaque().luminance(SpotLightBlock.LIGHT_EMISSION)), settings(), true);

    Block PATINA_BLOCK = register("patina_block", new SandBlock(0xBD3A0, AbstractBlock.Settings.copy(Blocks.SAND).sounds(BlockSoundGroup.TUFF)), settings().group(ItemGroup.BUILDING_BLOCKS), true);

    List<OxidizableBlock> COPPER_BRICKS = registerOxidationSet("copper_bricks", weatherState -> new OxidizableBlock(weatherState, AbstractBlock.Settings.copy(Blocks.CUT_COPPER)), settings().group(ItemGroup.BUILDING_BLOCKS));
    List<WeatheringPillarBlock> COPPER_PILLAR = registerOxidationSet("copper_pillar", weatherState -> new WeatheringPillarBlock(weatherState, AbstractBlock.Settings.copy(Blocks.CUT_COPPER)), settings().group(ItemGroup.BUILDING_BLOCKS));
    List<WeatheringPillarBlock> COPPER_TILES = registerOxidationSet("copper_tiles", weatherState -> new WeatheringPillarBlock(weatherState, AbstractBlock.Settings.copy(Blocks.CUT_COPPER)), settings().group(ItemGroup.BUILDING_BLOCKS));

    List<Block> WAXED_COPPER_BRICKS = registerWaxedSet("copper_bricks", $ -> new Block(AbstractBlock.Settings.copy(Blocks.CUT_COPPER)), settings().group(ItemGroup.BUILDING_BLOCKS));
    List<PillarBlock> WAXED_COPPER_PILLAR = registerWaxedSet("copper_pillar", $ -> new PillarBlock(AbstractBlock.Settings.copy(Blocks.CUT_COPPER)), settings().group(ItemGroup.BUILDING_BLOCKS));
    List<PillarBlock> WAXED_COPPER_TILES = registerWaxedSet("copper_tiles", $ -> new PillarBlock(AbstractBlock.Settings.copy(Blocks.CUT_COPPER)), settings().group(ItemGroup.BUILDING_BLOCKS));

    Block EXPOSED_REPEATER = register("exposed_repeater", new WeatheringRepeaterBlock(Oxidizable.OxidationLevel.EXPOSED), settings().group(ItemGroup.REDSTONE), true);
    Block WEATHERED_REPEATER = register("weathered_repeater", new WeatheringRepeaterBlock(Oxidizable.OxidationLevel.WEATHERED), settings().group(ItemGroup.REDSTONE), true);
    Block OXIDIZED_REPEATER = register("oxidized_repeater", new WeatheringRepeaterBlock(Oxidizable.OxidationLevel.OXIDIZED), settings().group(ItemGroup.REDSTONE), true);

    Block EXPOSED_COMPARATOR = register("exposed_comparator", new WeatheringComparatorBlock(Oxidizable.OxidationLevel.EXPOSED), settings().group(ItemGroup.REDSTONE), true);
    Block WEATHERED_COMPARATOR = register("weathered_comparator", new WeatheringComparatorBlock(Oxidizable.OxidationLevel.WEATHERED), settings().group(ItemGroup.REDSTONE), true);
    Block OXIDIZED_COMPARATOR = register("oxidized_comparator", new WeatheringComparatorBlock(Oxidizable.OxidationLevel.OXIDIZED), settings().group(ItemGroup.REDSTONE), true);

    Block EXPOSED_PISTON = register("exposed_piston", new WeatheringPistonBlock(Oxidizable.OxidationLevel.EXPOSED, false), settings().group(ItemGroup.REDSTONE), true);
    Block WEATHERED_PISTON = register("weathered_piston", new WeatheringPistonBlock(Oxidizable.OxidationLevel.WEATHERED, false), settings().group(ItemGroup.REDSTONE), true);
    Block OXIDIZED_PISTON = register("oxidized_piston", new WeatheringPistonBlock(Oxidizable.OxidationLevel.OXIDIZED, false), settings().group(ItemGroup.REDSTONE), true);

    Block EXPOSED_STICKY_PISTON = register("exposed_sticky_piston", new WeatheringPistonBlock(Oxidizable.OxidationLevel.EXPOSED, true), settings().group(ItemGroup.REDSTONE), true);
    Block WEATHERED_STICKY_PISTON = register("weathered_sticky_piston", new WeatheringPistonBlock(Oxidizable.OxidationLevel.WEATHERED, true), settings().group(ItemGroup.REDSTONE), true);
    Block OXIDIZED_STICKY_PISTON = register("oxidized_sticky_piston", new WeatheringPistonBlock(Oxidizable.OxidationLevel.OXIDIZED, true), settings().group(ItemGroup.REDSTONE), true);

    Block EXPOSED_OBSERVER = register("exposed_observer", new WeatheringObserverBlock(Oxidizable.OxidationLevel.EXPOSED), settings().group(ItemGroup.REDSTONE), true);
    Block WEATHERED_OBSERVER = register("weathered_observer", new WeatheringObserverBlock(Oxidizable.OxidationLevel.WEATHERED), settings().group(ItemGroup.REDSTONE), true);
    Block OXIDIZED_OBSERVER = register("oxidized_observer", new WeatheringObserverBlock(Oxidizable.OxidationLevel.OXIDIZED), settings().group(ItemGroup.REDSTONE), true);
    Block EXPOSED_DISPENSER = register("exposed_dispenser", new WeatheringDispenserBlock(Oxidizable.OxidationLevel.EXPOSED), settings().group(ItemGroup.REDSTONE), true);
    Block WEATHERED_DISPENSER = register("weathered_dispenser", new WeatheringDispenserBlock(Oxidizable.OxidationLevel.WEATHERED), settings().group(ItemGroup.REDSTONE), true);
    Block OXIDIZED_DISPENSER = register("oxidized_dispenser", new WeatheringDispenserBlock(Oxidizable.OxidationLevel.OXIDIZED), settings().group(ItemGroup.REDSTONE), true);

    Block EXPOSED_DROPPER = register("exposed_dropper", new WeatheringDropperBlock(Oxidizable.OxidationLevel.EXPOSED), settings().group(ItemGroup.REDSTONE), true);
    Block WEATHERED_DROPPER = register("weathered_dropper", new WeatheringDropperBlock(Oxidizable.OxidationLevel.WEATHERED), settings().group(ItemGroup.REDSTONE), true);
    Block OXIDIZED_DROPPER = register("oxidized_dropper", new WeatheringDropperBlock(Oxidizable.OxidationLevel.OXIDIZED), settings().group(ItemGroup.REDSTONE), true);

    Block EXPOSED_LEVER = register("exposed_lever", new WeatheringLeverBlock(Oxidizable.OxidationLevel.EXPOSED), settings().group(ItemGroup.REDSTONE), true);
    Block WEATHERED_LEVER = register("weathered_lever", new WeatheringLeverBlock(Oxidizable.OxidationLevel.WEATHERED), settings().group(ItemGroup.REDSTONE), true);
    Block OXIDIZED_LEVER = register("oxidized_lever", new WeatheringLeverBlock(Oxidizable.OxidationLevel.OXIDIZED), settings().group(ItemGroup.REDSTONE), true);

    List<DoorBlock> COPPER_DOORS = registerOxidationSet("copper_door", it -> new CopperDoorBlock(it, AbstractBlock.Settings.copy(Blocks.IRON_DOOR).sounds(BlockSoundGroup.COPPER)),  settings().group(ItemGroup.REDSTONE));
    List<TrapdoorBlock> COPPER_TRAPDOORS = registerOxidationSet("copper_trapdoor", it -> new CopperTrapdoorBlock(it, AbstractBlock.Settings.copy(Blocks.IRON_TRAPDOOR).sounds(BlockSoundGroup.COPPER)), settings().group(ItemGroup.REDSTONE));
    List<DoorBlock> WAXED_COPPER_DOORS = registerWaxedSet("copper_door", it -> new WaxedDoorBlock(AbstractBlock.Settings.copy(Blocks.IRON_DOOR).sounds(BlockSoundGroup.COPPER)), settings().group(ItemGroup.REDSTONE));
    List<TrapdoorBlock> WAXED_COPPER_TRAPDOORS = registerWaxedSet("copper_trapdoor", $ -> new AbstractCopperTrapdoorBlock(AbstractBlock.Settings.copy(Blocks.IRON_TRAPDOOR).sounds(BlockSoundGroup.COPPER)), settings().group(ItemGroup.REDSTONE));



    List<Block> HEADLIGHT = registerOxidationSet("headlight", weatherState -> new HeadLightBlock(weatherState, AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK).luminance(HeadLightBlock.LIGHT_EMISSION)), settings().group(ItemGroup.REDSTONE));

    List<Block> TOGGLER = registerOxidationSet("toggler", state -> new TogglerBlock(state, AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)), settings().group(ItemGroup.REDSTONE));

    // Rails
    Block EXPOSED_POWERED_RAIL = register("exposed_powered_rail", new WeatheringPoweredRailBlock(Oxidizable.OxidationLevel.EXPOSED), settings().group(ItemGroup.TRANSPORTATION), true);
    Block WEATHERED_POWERED_RAIL = register("weathered_powered_rail", new WeatheringPoweredRailBlock(Oxidizable.OxidationLevel.WEATHERED), settings().group(ItemGroup.TRANSPORTATION), true);
    Block OXIDIZED_POWERED_RAIL = register("oxidized_powered_rail", new WeatheringPoweredRailBlock(Oxidizable.OxidationLevel.OXIDIZED), settings().group(ItemGroup.TRANSPORTATION), true);

    // Workstations
    //Block SOLDERING_TABLE = register("soldering_table", new SolderingTableBlock(BlockBehaviour.AbstractBlock.Settings.copy(Blocks.SMITHING_TABLE)), REDSTONE);

    
    static Item.Settings settings() {
        return new Item.Settings();
    }

    static <T extends Item> T register(String name, T item) {
        ITEMS.put(item, Coopperative.id(name));
        return item;
    }

    static <T extends Block> T register(String name, T block, Item.Settings settings, boolean createItem) {
        BLOCKS.put(block, Coopperative.id(name));
        if (createItem) {
            ITEMS.put(new BlockItem(block, settings), BLOCKS.get(block));
        }
        return block;
    }

    static <B extends Block> List<B> registerOxidationSet(UnaryOperator<String> name, Function<Oxidizable.OxidationLevel, B> function, Item.Settings settings) {
        Oxidizable.OxidationLevel[] wStates = Oxidizable.OxidationLevel.values();
        ArrayList<B> blocks = new ArrayList<>(4);
        for (final Oxidizable.OxidationLevel weatherState : wStates) {
            String prefix = weatherState.equals(Oxidizable.OxidationLevel.UNAFFECTED) ? "" : weatherState.name().toLowerCase() + "_";
            Supplier<? extends B> supplier = () -> function.apply(weatherState);
            blocks.add(register(name.apply(prefix), supplier.get(), settings, true));
        }
        return blocks;
    }

    static <B extends Block> List<B> registerOxidationSet(String name, Function<Oxidizable.OxidationLevel, B> function, Item.Settings settings) {
        return registerOxidationSet(prefix -> prefix + name, function, settings);
    }

    static <B extends Block> List<B> registerWaxedSet(String name, Function<Oxidizable.OxidationLevel, B> function, Item.Settings settings) {
        return registerOxidationSet(prefix -> "waxed_" + prefix + name, function, settings);
    }

    static void init() {
        BLOCKS.keySet().forEach(block -> Registry.register(Registry.BLOCK, BLOCKS.get(block), block));
        ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
    }
}
