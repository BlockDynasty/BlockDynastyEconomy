/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.blockdynasty.economy.minestom.commons.adapters;

import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.services.Console;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.HeadProfile;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MaterialAdapter implements IMaterialAdapter {
    private Map<Materials, Material> materialMap = new HashMap<>();
    protected Map<String,HeadProfile> profileCacheHeadsTextures = new HashMap<>();

    public MaterialAdapter() {
        for (Materials material : Materials.values()) {
            try {
                String minecraftKey = "minecraft:" + material.name().toLowerCase();
                Material minestomMaterial = Material.fromKey(minecraftKey);
                if (minestomMaterial ==null) continue;
                materialMap.put(material, minestomMaterial);
            } catch (IllegalArgumentException e) {
                Console.logError("Material " + material.name() + " not found in Minestom, using default STONE");
            }
        }
    }

    public Material convertMaterial(Materials materials) {
        return materialMap.getOrDefault(materials, Material.STONE);
    }

    public ItemStack createItem(RecipeItem recipeItem) {
        List<Component> loreComponents = Stream.of(recipeItem.getLore())
                .map(line -> Component.text(line))
                .collect(Collectors.toList());

        String textureURL = recipeItem.getTexture();
        if(textureURL != null && !textureURL.isEmpty()) {
            return this.applyTexture(recipeItem,loreComponents);
        }
        return ItemStack.builder(convertMaterial(recipeItem.getMaterial()))
                .set(DataComponents.CUSTOM_NAME, Component.text(recipeItem.getName()))
                .set(DataComponents.LORE, loreComponents)
                .build();
    }

    public ItemStack applyTexture(RecipeItem recipeItem,List<Component> loreComponents) {
        HeadProfile profile = HeadProfileFromURL(recipeItem.getTexture());
        return ItemStack.builder(Material.PLAYER_HEAD)
                .set(DataComponents.CUSTOM_NAME, Component.text(recipeItem.getName()))
                .set(DataComponents.LORE,loreComponents)
                .set(DataComponents.PROFILE, profile)
                .build();
    }

    private HeadProfile HeadProfileFromURL(String textureURL) {
        return profileCacheHeadsTextures.computeIfAbsent(textureURL, url -> {
            PlayerSkin playerSkin = new PlayerSkin(jsonTextureData(textureURL)," ");
            return new HeadProfile(playerSkin);
        });
    }

    protected String jsonTextureData(String textureURL) {
        String jsonTexture = String.format(
                "{\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}",
                textureURL
        );
        return Base64.getEncoder().encodeToString(jsonTexture.getBytes());
    }
}
