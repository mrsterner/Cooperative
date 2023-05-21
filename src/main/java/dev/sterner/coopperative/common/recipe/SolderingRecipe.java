package dev.sterner.coopperative.common.recipe;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.sterner.coopperative.common.registry.CRecipeTypes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Objects;

import static net.minecraft.datafixer.fix.BlockEntitySignTextStrictJsonFix.GSON;

public class SolderingRecipe implements Recipe<Inventory> {

    static int MAX_INGREDIENTS = 4;

    private final Identifier id;
    final String group;
    final ItemStack result;
    final DefaultedList<Ingredient> ingredients;

    public SolderingRecipe(Identifier id, String group, ItemStack result, DefaultedList<Ingredient> ingredients) {
        this.id = id;
        this.group = group;
        this.ingredients = ingredients;
        this.result = result;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return false;
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        return this.result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getOutput() {
        return result;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CRecipeTypes.SOLDERING_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return CRecipeTypes.SOLDERING_RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<SolderingRecipe> {

        @Override
        public SolderingRecipe read(Identifier id, JsonObject json) {
            String group = JsonHelper.getString(json, "group", "");
            DefaultedList<Ingredient> ingredients = itemsFromJson(JsonHelper.getArray(json, "ingredients"));

            if (ingredients.isEmpty())
                throw new JsonParseException("No ingredients for soldering recipe");
            if (ingredients.size() > SolderingRecipe.MAX_INGREDIENTS)
                throw new JsonParseException("Too many ingredients for soldering recipe. The maximum is " + SolderingRecipe.MAX_INGREDIENTS);

            ItemStack result = getItemStack(JsonHelper.getObject(json, "result"), true, true);
            return new SolderingRecipe(id, group, result, ingredients);
        }

        private static DefaultedList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            DefaultedList<Ingredient> ingredients = DefaultedList.of();

            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                if (!ingredient.isEmpty()) {
                    ingredients.add(ingredient);
                }
            }

            return ingredients;
        }

        public static Item getItem(String itemName, boolean disallowsAirInRecipe)
        {
            Identifier itemKey = new Identifier(itemName);
            if (!Registry.ITEM.containsId(itemKey))
                throw new JsonSyntaxException("Unknown item '" + itemName + "'");

            Item item = Registry.ITEM.get(itemKey);
            if (disallowsAirInRecipe && item == Items.AIR)
                throw new JsonSyntaxException("Invalid item: " + itemName);
            return Objects.requireNonNull(item);
        }

        public static NbtCompound getNBT(JsonElement element)
        {
            try
            {
                if (element.isJsonObject())
                    return StringNbtReader.parse(GSON.toJson(element));
                else
                    return StringNbtReader.parse(JsonHelper.asString(element, "nbt"));
            }
            catch (CommandSyntaxException e)
            {
                throw new JsonSyntaxException("Invalid NBT Entry: " + e);
            }
        }

        public static ItemStack getItemStack(JsonObject json, boolean readNBT, boolean disallowsAirInRecipe)
        {
            String itemName = JsonHelper.getString(json, "item");
            Item item = getItem(itemName, disallowsAirInRecipe);
            if (readNBT && json.has("nbt"))
            {
                NbtCompound nbt = getNBT(json.get("nbt"));
                NbtCompound tmp = new NbtCompound();
                if (nbt.contains("ForgeCaps"))
                {
                    tmp.put("ForgeCaps", nbt.get("ForgeCaps"));
                    nbt.remove("ForgeCaps");
                }

                tmp.put("tag", nbt);
                tmp.putString("id", itemName);
                tmp.putInt("Count", JsonHelper.getInt(json, "count", 1));

                return ItemStack.fromNbt(tmp);
            }

            return new ItemStack(item, JsonHelper.getInt(json, "count", 1));
        }

        @Override
        public SolderingRecipe read(Identifier id, PacketByteBuf buf) {
            String group = buf.readString();
            int size = buf.readVarInt();
            DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(size, Ingredient.EMPTY);

            ingredients.replaceAll(ignored -> Ingredient.fromPacket(buf));

            ItemStack result = buf.readItemStack();
            return new SolderingRecipe(id, group, result, ingredients);
        }

        @Override
        public void write(PacketByteBuf buf, SolderingRecipe recipe) {
            buf.writeString(recipe.group);
            buf.writeVarInt(recipe.ingredients.size());

            for(Ingredient ingredient : recipe.ingredients)
                ingredient.write(buf);

            buf.writeItemStack(recipe.result);
        }
    }
}
