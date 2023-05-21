package dev.sterner.coopperative.common.registry;

import dev.sterner.coopperative.Coopperative;
import dev.sterner.coopperative.common.recipe.SolderingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

public interface CRecipeTypes {
    RecipeSerializer<SolderingRecipe> SOLDERING_RECIPE_SERIALIZER = new SolderingRecipe.Serializer();
    RecipeType<SolderingRecipe> SOLDERING_RECIPE_TYPE = new RecipeType<>() {
        @Override
        public String toString() {
            return Coopperative.MODID + ":soldering";
        }
    };

    static void init() {
        Registry.register(Registry.RECIPE_SERIALIZER, Coopperative.id("soldering"), SOLDERING_RECIPE_SERIALIZER);
        Registry.register(Registry.RECIPE_TYPE, Coopperative.id("soldering"), SOLDERING_RECIPE_TYPE);
    }
}
