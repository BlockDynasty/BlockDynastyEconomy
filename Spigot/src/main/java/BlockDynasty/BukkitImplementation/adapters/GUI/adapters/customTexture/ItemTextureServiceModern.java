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

import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.MaterialAdapter;
import BlockDynasty.BukkitImplementation.utils.Console;
import lib.util.materials.Materials;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings( "deprecation")
public class ItemTextureServiceModern implements ItemTextureService {
    private static final Map<String, org.bukkit.profile.PlayerProfile> profileCacheHeadsTextures = new ConcurrentHashMap<>();

    @Override
    public void applyTexture(ItemStack item,String texture){
        if (texture == null || texture.isEmpty()) return;

        try {
            URL url = new URL(texture);

            ItemStack clone = item.clone();

            clone.setType(MaterialAdapter.toBukkitMaterial(Materials.PLAYER_HEAD));
            SkullMeta meta = (SkullMeta) clone.getItemMeta();

            org.bukkit.profile.PlayerProfile profile = getCustomHeadProfile(texture);

            meta.setOwnerProfile(profile);
            clone.setItemMeta(meta);

            item.setType(clone.getType());
            item.setItemMeta(clone.getItemMeta());
        } catch (Exception e) {
            Console.logError("Process Custom head texture failed, cause: "+e.getMessage());
        }
    }

    private org.bukkit.profile.PlayerProfile getCustomHeadProfile(String textureUrl) {
        return profileCacheHeadsTextures.computeIfAbsent(textureUrl, url -> {
            org.bukkit.profile.PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID(), "CustomHead");
            PlayerTextures textures = profile.getTextures();
            try {
                textures.setSkin(new URL(url), PlayerTextures.SkinModel.CLASSIC);
                profile.setTextures(textures);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return profile;
        });
    }
}
