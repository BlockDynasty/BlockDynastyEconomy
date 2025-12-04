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

package adapters;

import lib.gui.components.recipes.RecipeItem;
import lib.util.materials.Materials;
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

public class MaterialAdapter {
    //map of materials between lib and minestom
    private static Map<Materials, Material> materialMap = new HashMap<>();
    private static Map<String,HeadProfile> profileCacheHeadsTextures = new HashMap<>();

    static {
        //initialize material map here
        //hacer el map a mano, donde haré Materials.X -> Material.X, tengo que hacerlo a mano uno por uno
        materialMap.put(Materials.GLASS_PANE, Material.GLASS_PANE);
        materialMap.put(Materials.BLUE_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS_PANE);
        materialMap.put(Materials.RED_CONCRETE, Material.RED_CONCRETE);
        materialMap.put(Materials.LIME_CONCRETE, Material.LIME_CONCRETE);
        materialMap.put(Materials.EMERALD_BLOCK, Material.EMERALD_BLOCK);
        materialMap.put(Materials.GOLD_NUGGET, Material.GOLD_NUGGET);
        materialMap.put(Materials.LIME_DYE, Material.LIME_DYE);
        materialMap.put(Materials.NETHER_STAR, Material.NETHER_STAR);
        materialMap.put(Materials.STONE, Material.STONE);
        materialMap.put(Materials.AIR, Material.AIR);
        materialMap.put(Materials.PAPER, Material.PAPER);
        materialMap.put(Materials.BOOK, Material.BOOK);
        materialMap.put(Materials.WRITABLE_BOOK, Material.WRITABLE_BOOK);
        materialMap.put(Materials.CHEST, Material.CHEST);
        materialMap.put(Materials.ENDER_CHEST, Material.ENDER_CHEST);
        materialMap.put(Materials.BARRIER, Material.BARRIER);
        materialMap.put(Materials.NAME_TAG, Material.NAME_TAG);
        materialMap.put(Materials.ARROW, Material.ARROW);
        materialMap.put(Materials.EMERALD, Material.EMERALD);
        materialMap.put(Materials.DIAMOND, Material.DIAMOND);
        materialMap.put(Materials.GOLD_INGOT, Material.GOLD_INGOT);
        materialMap.put(Materials.REDSTONE, Material.REDSTONE);
        materialMap.put(Materials.PLAYER_HEAD, Material.PLAYER_HEAD);
        materialMap.put(Materials.WHITE_WOOL, Material.WHITE_WOOL);
        materialMap.put(Materials.RED_WOOL, Material.RED_WOOL);
        materialMap.put(Materials.PURPLE_WOOL, Material.PURPLE_WOOL);
        materialMap.put(Materials.ORANGE_WOOL, Material.ORANGE_WOOL);
        materialMap.put(Materials.LIME_WOOL, Material.LIME_WOOL);
        materialMap.put(Materials.GRAY_WOOL, Material.GRAY_WOOL);
        materialMap.put(Materials.LIGHT_GRAY_WOOL, Material.LIGHT_GRAY_WOOL);
        materialMap.put(Materials.CYAN_WOOL, Material.CYAN_WOOL);
        materialMap.put(Materials.LIGHT_BLUE_WOOL, Material.LIGHT_BLUE_WOOL);
        materialMap.put(Materials.BLUE_WOOL, Material.BLUE_WOOL);
        materialMap.put(Materials.BROWN_WOOL, Material.BROWN_WOOL);
        materialMap.put(Materials.GREEN_WOOL, Material.GREEN_WOOL);
        materialMap.put(Materials.PINK_WOOL, Material.PINK_WOOL);
        materialMap.put(Materials.YELLOW_WOOL, Material.YELLOW_WOOL);
    }

    public static Material convertMaterial(Materials materials) {
        return materialMap.getOrDefault(materials, Material.STONE);
    }

    public static ItemStack createItem(RecipeItem recipeItem) {
        List<Component> loreComponents = Stream.of(recipeItem.getLore())
                .map(line -> Component.text(line))
                .collect(Collectors.toList());

        String textureURL = recipeItem.getTexture();
        if(textureURL != null && !textureURL.isEmpty()) {
            HeadProfile profile = HeadProfileFromURL(recipeItem.getTexture());
            return ItemStack.builder(Material.PLAYER_HEAD)
                    .set(DataComponents.CUSTOM_NAME, Component.text(recipeItem.getName()))
                    .set(DataComponents.LORE,loreComponents)
                    .set(DataComponents.PROFILE, profile)
                    .build();
        }

        return ItemStack.builder(convertMaterial(recipeItem.getMaterial()))
                .set(DataComponents.CUSTOM_NAME, Component.text(recipeItem.getName()))
                .set(DataComponents.LORE, loreComponents)
                .build();
    }

    private static HeadProfile HeadProfileFromURL(String textureURL) {
        return profileCacheHeadsTextures.computeIfAbsent(textureURL, url -> {
            PlayerSkin playerSkin = new PlayerSkin(jsonTextureData(textureURL)," ");
            return new HeadProfile(playerSkin);
        });
    }

    private static String jsonTextureData(String textureURL) {
        String jsonTexture = String.format(
                "{\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}",
                textureURL
        );
        return Base64.getEncoder().encodeToString(jsonTexture.getBytes());
    }
}
