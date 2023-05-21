package dev.sterner.coopperative;

import dev.sterner.coopperative.client.screen.SolderingScreen;
import dev.sterner.coopperative.common.registry.CBlockEntityTypes;
import dev.sterner.coopperative.common.registry.CObjects;
import dev.sterner.coopperative.common.registry.CRecipeTypes;
import dev.sterner.coopperative.common.registry.CScreenHandlerTypes;
import dev.sterner.coopperative.common.util.CConversions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Coopperative implements ModInitializer, ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("cooperative-fabric");
    public static final String MODID = "coopperative";

    public static Identifier id(String name) {
        return new Identifier(MODID, name);
    }

    @Override
    public void onInitialize() {
        CObjects.init();
        CBlockEntityTypes.init();
        CRecipeTypes.init();

        //TODO make this not bad
        for (Block block : CObjects.BLOCKS.keySet()) {
            CConversions.getUnwaxedVersion(block).ifPresent(unwaxed -> OxidizableBlocksRegistry.registerWaxableBlockPair(block, unwaxed));
            CConversions.getUnweatheredVersion(block).ifPresent(unweather -> OxidizableBlocksRegistry.registerOxidizableBlockPair(block, unweather));
        }
    }

    @Override
    public void onInitializeClient() {
        CScreenHandlerTypes.init();

    }
}