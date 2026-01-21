package com.BlockDynasty.hytale.adapters;

import abstractions.platform.recipes.RecipeItem;
import lib.gui.components.IItemStack;

//adaptador de icono de gui
public class ItemStackAdapter implements IItemStack {
    private RecipeItem recipeItem;

    public ItemStackAdapter(RecipeItem recipeItem) {
        this.recipeItem = recipeItem;
    }

    public RecipeItem getRecipeItem() {
        return this.recipeItem;
    }

    @Override
    public Object getHandle() {
        return this;
    }
}
