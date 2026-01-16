package com.BlockDynasty.hytale.adapters;

import com.hypixel.hytale.server.core.inventory.Inventory;
import lib.gui.components.IInventory;
import lib.gui.components.IItemStack;

//gui representation of an inventory...
public class InventoryAdapter implements IInventory {
    private Inventory inventory;
    public InventoryAdapter(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void set(int i, IItemStack iItemStack) {
        inventory.getStorage().addItemStackToSlot((short)i, (com.hypixel.hytale.server.core.inventory.ItemStack) iItemStack.getHandle());
    }

    @Override
    public Object getHandle() {
        return inventory.getStorage();
    }
}
