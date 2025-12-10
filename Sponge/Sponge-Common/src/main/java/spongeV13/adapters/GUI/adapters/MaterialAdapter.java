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

package spongeV13.adapters.GUI.adapters;

import lib.gui.components.recipes.RecipeItem;
import lib.util.materials.Materials;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.property.ProfileProperty;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MaterialAdapter {
    private static final Map<String, GameProfile> profileCacheHeadsTextures = new ConcurrentHashMap<>();
    private static final Map<Materials, ItemType> MATERIAL_MAP = new HashMap<>();
    private static final ItemType FALLBACK = ItemTypes.STONE.get();

    static {
        for (Materials material : Materials.values()) {
            try {
                Field field = ItemTypes.class.getField(material.name());
                Object supplier = field.get(null);
                ItemType itemType = (ItemType) supplier.getClass().getMethod("get").invoke(supplier);
                MATERIAL_MAP.put(material, itemType);
            } catch (Exception e) {
                MATERIAL_MAP.put(material, FALLBACK);
            }
        }
    }

    public static ItemType toSpongeMaterial(Materials material) {
        return MATERIAL_MAP.getOrDefault(material, FALLBACK);
    }

    public static void applyItemName(ItemStack item, String displayName){
        if(displayName != null){
            if(item.type().equals(ItemTypes.PLAYER_HEAD.get())){
                Optional<ServerPlayer> optional= Sponge.server().player(displayName);
                optional.ifPresent(serverPlayer -> item.offer(Keys.GAME_PROFILE, serverPlayer.profile()));
            }
            item.offer(Keys.CUSTOM_NAME,  MiniMessage.miniMessage().deserialize(displayName));
        }
    }

    public static void applyItemLore(ItemStack item, List<String> lore){
        if(lore != null){
            List<Component> loreComponents = lore.stream()
                    .map(m ->  MiniMessage.miniMessage().deserialize(m))
                    .collect(Collectors.toList());
            item.offer(Keys.LORE, loreComponents);
        }
    }

    public static void applyTexture(ItemStack item, String texture){
        if (texture == null || texture.isEmpty()) return;
        GameProfile profile = crearGameProfileDesdeTextureURL(texture);
        item.offer(Keys.GAME_PROFILE, profile);
    }

    private static GameProfile crearGameProfileDesdeTextureURL(String textureURL) {
        return profileCacheHeadsTextures.computeIfAbsent(textureURL, url -> {
            GameProfile profile = GameProfile.of(UUID.randomUUID(), "CustomHead");
            String textureData = crearTextureData(textureURL);

            ProfileProperty textureProperty = ProfileProperty.of(
                    ProfileProperty.TEXTURES,
                    textureData
            );
            return profile.withProperty(textureProperty);
        });
    }

    private static String crearTextureData(String textureURL) {
        String jsonTexture = String.format(
                "{\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}",
                textureURL
        );
        return Base64.getEncoder().encodeToString(jsonTexture.getBytes());
    }

    public static ItemStack createItemStack(RecipeItem recipeItem){
        ItemStack itemStack = null;
        if(recipeItem.getMaterial() != null){
            ItemType itemType = toSpongeMaterial(recipeItem.getMaterial());
            if(recipeItem.getTexture() != null && !recipeItem.getTexture().isEmpty()){
                itemType = ItemTypes.PLAYER_HEAD.get();
            }

            itemStack = ItemStack.of(itemType);
            applyItemName(itemStack, recipeItem.getName());
            applyItemLore(itemStack, List.of(recipeItem.getLore()));
            applyTexture(itemStack, recipeItem.getTexture());
        }
        return itemStack;
    }
}
