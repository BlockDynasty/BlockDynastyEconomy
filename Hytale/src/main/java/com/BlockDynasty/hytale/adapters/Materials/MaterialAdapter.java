package com.BlockDynasty.hytale.adapters.Materials;

import abstractions.platform.recipes.RecipeItem;
import com.BlockDynasty.hytale.adapters.ItemStackAdapter;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.RecipeItemCurrency;
import lib.gui.components.IItemStack;

public class MaterialAdapter {


    //icono personalizado para lagui
    public static IItemStack createItemStack(RecipeItem recipeItem) {
        /*recipeItem.getMaterial();
        recipeItem.getName();
        recipeItem.getLore();
        recipeItem.getTexture();*/
        //todo la textura puede ser la referencia a algun asset en memoria

        return new ItemStackAdapter(recipeItem);
    }
    public static ItemStackCurrency createItemStack(RecipeItemCurrency recipeItemCurrency){
        return null;
    }


}
