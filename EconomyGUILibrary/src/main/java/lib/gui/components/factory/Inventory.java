package lib.gui.components.factory;

import lib.abstractions.PlatformAdapter;
import lib.gui.components.IInventory;
import lib.gui.components.recipes.RecipeInventory;

public class Inventory {
    private static PlatformAdapter platformAdapter;

    public static void init(PlatformAdapter adapter) {
        platformAdapter = adapter;
    }

    public static IInventory of(RecipeInventory recipeInventory){
        return platformAdapter.createInventory(recipeInventory);
    }
}
