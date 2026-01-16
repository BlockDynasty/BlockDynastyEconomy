package com.BlockDynasty.hytale.adapters;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import lib.gui.components.IItemStack;

//adaptador de icono de gui
public class ItemStackAdapter implements IItemStack {
    private ItemStack itemStack;

    public ItemStackAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public Object getHandle() {
        return itemStack;
    }
}
