package dev.sterner.coopperative;

import dev.sterner.coopperative.data.CLanguageProvider;
import dev.sterner.coopperative.data.CLootTableProvider;
import dev.sterner.coopperative.data.CModelProvider;
import dev.sterner.coopperative.data.CRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class CoopperativeDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.addProvider(CLanguageProvider::new);
        generator.addProvider(CModelProvider::new);
        generator.addProvider(CRecipeProvider::new);
        generator.addProvider(CLootTableProvider::new);
    }
}
