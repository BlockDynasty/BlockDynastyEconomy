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

package BlockDynasty.adapters.GUI.adapters;

import lib.gui.components.RecipeItem;
import lib.util.materials.Materials;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class MaterialAdapter {
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
            if(item.type().equals( ItemTypes.PLAYER_HEAD.get())){
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
        //nothing to do here
    }

    public static ItemStack createItemStack(RecipeItem recipeItem){
        ItemStack itemStack = null;
        if(recipeItem.getMaterial() != null){
            ItemType itemType = toSpongeMaterial(recipeItem.getMaterial());
            itemStack = ItemStack.of(itemType);
            applyItemName(itemStack, recipeItem.getName());
            applyItemLore(itemStack, List.of(recipeItem.getLore()));
            applyTexture(itemStack, recipeItem.getTexture());
        }
        return itemStack;
    }
}
