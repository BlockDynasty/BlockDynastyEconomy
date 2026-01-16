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

package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.customTexture;

import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.Materials.MaterialProvider;
import BlockDynasty.BukkitImplementation.utils.Console;
import abstractions.platform.materials.Materials;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

@SuppressWarnings( "deprecation")
public class ItemTextureServiceVanilla implements ItemTextureService {
    private static final Map<String, com.mojang.authlib.GameProfile> profileCacheHeadsTextures = new ConcurrentHashMap<>();
    private boolean serviceEnabled = true;

    //tested on 1.8, 1.12, 1.17.1 ,1.18
    //require reflection to set the GameProfile on SkullMeta
    @Override
    public void applyTexture(ItemStack item,String textureUrl){
        if (!serviceEnabled || textureUrl == null || textureUrl.isEmpty()) return;

        try {
            item.setType(MaterialProvider.toBukkitMaterial(Materials.PLAYER_HEAD));
            item.setDurability((short) 3);

            SkullMeta meta = (SkullMeta) item.getItemMeta();
            com.mojang.authlib.GameProfile profile = getCustomHeadProfile(textureUrl);

            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
            profileField.setAccessible(false);

            item.setItemMeta(meta);
        }catch (NoSuchFieldException e) {
            serviceEnabled = false;
            Console.logError(e.getMessage()+ "Custom head textures are not supported on this server version.");
        } catch (Exception e) {
            Console.debug(e.getMessage());
        }
    }

    private com.mojang.authlib.GameProfile getCustomHeadProfile(String textureUrl) {
        return profileCacheHeadsTextures.computeIfAbsent(textureUrl, url -> {
            com.mojang.authlib.GameProfile profile = new GameProfile(UUID.randomUUID(), "customHead");
            profile.getProperties().put("textures", new Property("textures", crearTextureData(textureUrl)));
            return profile;
        });
    }

    private String crearTextureData(String textureURL) {
        String jsonTexture = String.format(
                "{\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}",
                textureURL
        );
        return Base64.getEncoder().encodeToString(jsonTexture.getBytes(StandardCharsets.UTF_8));
    }
}
