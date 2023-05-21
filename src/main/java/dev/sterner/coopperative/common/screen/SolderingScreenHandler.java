package dev.sterner.coopperative.common.screen;

import dev.sterner.coopperative.common.registry.CScreenHandlerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class SolderingScreenHandler extends ScreenHandler {


    private final Inventory ingredientSlots = new SimpleInventory(4) {
        @Override
        public void markDirty() {
            SolderingScreenHandler.this.onContentChanged(this);
            super.markDirty();
        }
    };

    private final CraftingResultInventory resultSlots = new CraftingResultInventory() {
        @Override
        public void markDirty() {
            SolderingScreenHandler.this.onContentChanged(this);
            super.markDirty();
        }
    };

    public SolderingScreenHandler(int syncId, Inventory inventory) {
        super(CScreenHandlerTypes.SOLDERING_SCREEN_HANDLER, syncId);
        this.addSlot(new Slot(this.ingredientSlots, 0, 44, 29));
        this.addSlot(new Slot(this.ingredientSlots, 1, 44, 50));
        this.addSlot(new Slot(this.ingredientSlots, 2, 80, 50));
        this.addSlot(new Slot(this.ingredientSlots, 3, 116, 50));
        this.addSlot(new Slot(this.resultSlots, 4, 79, 21) {
            @Override
            public boolean canInsert(ItemStack itemStack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack itemStack) {
                SolderingScreenHandler.this.slots.get(0).takeStack(1);
                SolderingScreenHandler.this.slots.get(1).takeStack(1);
                SolderingScreenHandler.this.slots.get(2).takeStack(1);
                SolderingScreenHandler.this.slots.get(3).takeStack(1);
                super.onTakeItem(player, itemStack);
            }
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
