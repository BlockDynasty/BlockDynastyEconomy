package EngineTest.mocks;

import EngineTest.mocks.utils.Color;
import lib.gui.components.IItemStack;
import abstractions.platform.recipes.RecipeItem;

public class ItemStack implements IItemStack {
    private RecipeItem recipeItem;

    public ItemStack(RecipeItem recipeItem){
        this.recipeItem = recipeItem;
    }

    @Override
    public Object getHandle() {
        return this;
    }

    @Override
    public String toString(){
        return Color.parse(this.recipeItem.getName());
    }
}
