package dev.sterner.coopperative.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;

public class SolderingScreen implements Inventory, RecipeInputProvider {

    private final DefaultedList<ItemStack> items;

    private final int width;
    private final int height;
    private final ScreenHandler menu;

    public SolderingScreen(ScreenHandler menu, int width, int height) {
        this.items = DefaultedList.ofSize(width * height, ItemStack.EMPTY);
        this.menu = menu;
        this.width = width;
        this.height = height;
    }


    @Override
    public int size() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot >= this.size() ? ItemStack.EMPTY : this.items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemstack = Inventories.splitStack(this.items, slot, amount);
        if (!itemstack.isEmpty()) {
            this.menu.onContentChanged(this);
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.items, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.items.set(slot, stack);
        this.menu.onContentChanged(this);
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void provideRecipeInputs(RecipeMatcher finder) {
        for(ItemStack itemstack : this.items) {
            finder.addInput(itemstack);
        }
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
}
