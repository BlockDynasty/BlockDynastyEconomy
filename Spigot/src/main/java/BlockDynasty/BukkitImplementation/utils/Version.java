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

package BlockDynasty.BukkitImplementation.utils;
import java.util.Arrays;

/**
 * this class save the current server version and provide some utilities to check versions
 * **/
public class Version {
    private static final String currentVersion = org.bukkit.Bukkit.getBukkitVersion();
    public static final String[] UNSUPPORTED_VERSIONS = {"1.13", "1.14", "1.15", "1.16"};
    public static final String[] LEGACY_VERSIONS = {"1.8","1.9","1.10","1.11","1.12"};
    private static final boolean hasAdventure = JavaUtil.classExists("net.kyori.adventure.text.Component")&&JavaUtil.classExists("net.kyori.adventure.text.minimessage.MiniMessage");
    public static final boolean isFolia = JavaUtil.classExists("io.papermc.paper.threadedregions.RegionizedServer");
    public static final boolean isCanvas = JavaUtil.classExists("io.canvasmc.canvas.server.ThreadedServer");
    public static final boolean supportCustomProfile = isHigherThan("1.20") && JavaUtil.classExists("org.bukkit.profile.PlayerProfile");
    public static final boolean hasMojangAuthLib = JavaUtil.classExists("com.mojang.authlib.GameProfile") && JavaUtil.classExists("com.mojang.authlib.properties.Property");
    public static final boolean hasSupportHardCash = isHigherThan("1.8.7");

    //this dependency is critical, because the library AnvilGUI works directly with NMS code.
    private static final boolean hasSupportAnvilGUI = !isMohist() && !isHigherThan("1.21.11");

    public static boolean hasSupportHardCash() {
        return hasSupportHardCash;
    }
    public static boolean match(String version) {
        return currentVersion.startsWith(version);
    }

    public static boolean match(String... versions) {
        return Arrays.stream(versions).anyMatch(currentVersion::startsWith);
    }

    public static boolean isMohist() {
        return JavaUtil.classExists("com.mohistmc.MohistMC");
    }

    /**
     * Check if the server supports AnvilGUI
     */
    public static boolean hasSupportAnvilGUI() {
        return hasSupportAnvilGUI;
    }

    /**
     * Check if the server supports Adventure Text API
     */
    public static boolean hasSupportAdventureText() {
        return hasAdventure;
    }

    public static boolean hasMojangAuthLib() {
        return hasMojangAuthLib;
    }

    public static boolean hasSupportCustomProfile() {
        return supportCustomProfile;
    }

    /**
     * Check if the server supports Folia Scheduler
     */
    public static boolean hasFoliaScheduler() {
        return isFolia || isCanvas;
    }
    /**
     *  this is useful for item Creations
     **/
    public static boolean isLegacy() {
        return Version.match(LEGACY_VERSIONS);
    }

    public static boolean isUnsupportedVersion() {
        return Version.match(UNSUPPORTED_VERSIONS);
    }

    public static boolean isHigherThan(String version) {
        String currentClean = currentVersion.split("-")[0]; // Remove any suffixes like "-R0.1-SNAPSHOT"

        String[] currentParts = currentClean.split("\\.");
        String[] compareParts = version.split("\\.");

        // Compare major version
        int length = Math.max(currentParts.length, compareParts.length);
        for (int i = 0; i < length; i++) {
            int currentNum = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int compareNum = i < compareParts.length ? Integer.parseInt(compareParts[i]) : 0;

            if (currentNum > compareNum) {
                return true;
            } else if (currentNum < compareNum) {
                return false;
            }
            // If equal, continue to next version component
        }

        // All components are equal
        return false;
    }
}