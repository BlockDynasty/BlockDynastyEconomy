package net.blockdynasty.economy.minestom.adapters;

import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.player.ResolvableProfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialAdapter extends  net.blockdynasty.economy.minestom.commons.adapters.MaterialAdapter {
    protected Map<String,ResolvableProfile> profileCacheHeadsTextures = new HashMap<>();

    @Override
    public ItemStack applyTexture(RecipeItem recipeItem,List<Component> loreComponents) {
        ResolvableProfile profile = HeadProfileFromURL(recipeItem.getTexture());
        return ItemStack.builder(Material.PLAYER_HEAD)
                .set(DataComponents.CUSTOM_NAME, Component.text(recipeItem.getName()))
                .set(DataComponents.LORE,loreComponents)
                .set(DataComponents.PROFILE, profile)
                .build();
    }

    private ResolvableProfile HeadProfileFromURL(String textureURL) {
        return this.profileCacheHeadsTextures.computeIfAbsent(textureURL, url -> {
            PlayerSkin playerSkin = new PlayerSkin(super.jsonTextureData(textureURL)," ");
            return new ResolvableProfile(playerSkin);
        });
    }
}

