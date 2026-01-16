package lib.gui.components.factory;

import lib.gui.components.PlatformGUI;
import lib.gui.components.IInventory;
import abstractions.platform.recipes.RecipeInventory;

public class Inventory {
    private static PlatformGUI platformAdapter;

    public static void init(PlatformGUI adapter) {
        platformAdapter = adapter;
    }

    public static IInventory of(RecipeInventory recipeInventory){
        return platformAdapter.createInventory(recipeInventory);
    }
}
