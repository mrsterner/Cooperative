package dev.sterner.coopperative.common.registry;

import dev.sterner.coopperative.Coopperative;
import dev.sterner.coopperative.common.screen.SolderingScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public interface CScreenHandlerTypes {
    ScreenHandlerType<SolderingScreenHandler> SOLDERING_SCREEN_HANDLER =
            Registry.register(Registry.SCREEN_HANDLER, Coopperative.id("soldering_screen"), new ScreenHandlerType<>(SolderingScreenHandler::new));

    static void init(){

    }
}
