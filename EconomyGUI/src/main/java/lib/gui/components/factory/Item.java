package lib.gui.components.factory;

import lib.gui.components.PlatformGUI;
import lib.gui.components.IItemStack;
import abstractions.platform.recipes.RecipeItem;

public class Item {
    private static PlatformGUI platformAdapter;

    public static void init(PlatformGUI adapter) {
        platformAdapter = adapter;
    }

    public static IItemStack of(RecipeItem recipeItem){
        return platformAdapter.createItemStack(recipeItem);
    }
}
