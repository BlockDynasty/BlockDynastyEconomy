package lib.gui.components.factory;

import lib.abstractions.PlatformAdapter;
import lib.gui.components.IItemStack;
import lib.gui.components.recipes.RecipeItem;

public class Item {
    private static PlatformAdapter platformAdapter;

    public static void init(PlatformAdapter adapter) {
        platformAdapter = adapter;
    }

    public static IItemStack of(RecipeItem recipeItem){
        return platformAdapter.createItemStack(recipeItem);
    }
}
