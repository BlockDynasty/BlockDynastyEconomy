package net.blockdynasty.economy.minestom.commons.adapters;

import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.minestom.server.item.ItemStack;

public interface IMaterialAdapter {
    ItemStack createItem(RecipeItem recipeItem);
}
