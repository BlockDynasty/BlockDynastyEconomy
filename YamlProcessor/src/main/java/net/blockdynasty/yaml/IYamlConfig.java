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

package net.blockdynasty.yaml;

import java.io.File;
import java.util.Map;

/**
 * Interface for handling YAML configuration files, providing methods for creating, loading,
 * updating, and writing configurations based on templates.
 */
public interface IYamlConfig {

    /**
     * Creates a new file based on the provided template and returns its content as a Map.
     *
     * @param file The file to be created.
     * @param templatePath The path to the template resource.
     * @return A Map containing the content of the newly created file.
     */
    Map<Object,Object> createNewFile(File file, String templatePath);

    /**
     * Loads the content of an existing file and checks it against a template for updates.
     *
     * @param fileConfig The file to be loaded.
     * @param templatePath The path to the template resource.
     * @return A Map containing the content of the loaded file, potentially updated with missing keys from the template.
     */
    Map<Object,Object> loadFile(File fileConfig, String templatePath);

    /**
     * Loads the content of an existing file without requiring a bundled template resource.
     * Use this for user-created files that do not have a matching classpath resource.
     * Optionally, missing keys can be filled from a fallback template resource if provided.
     *
     * @param fileConfig   The file to be loaded.
     * @param fallbackTemplatePath The classpath path to a fallback template resource used to fill
     *                             missing keys, or {@code null} to skip key-filling entirely.
     * @return A Map containing the content of the loaded file, with any missing keys filled from
     *         the fallback template when one is provided.
     */
    Map<Object,Object> loadFileRaw(File fileConfig, String fallbackTemplatePath);

    /**
     * Checks if the current configuration needs to be updated based on the template configuration.
     * new keys in the template that are missing in the current configuration will trigger an update.
     * @param currentConfig The current configuration to be checked.
     * @param templateConfig The template configuration to compare against.
     * @return true if the current configuration needs to be updated, false otherwise.
     */
    boolean checkIfNeedsUpdate(Map<Object,Object> currentConfig, Map<Object,Object> templateConfig);

    /**
     * Fills the current configuration with missing keys from the template configuration.
     * This method will add any keys that are present in the template but missing in the current configuration, using the default values from the template.
     *
     * @param currentConfig The current configuration to be updated.
     * @param templateConfig The template configuration to use for filling missing keys.
     */
    void fillMissingKeys(Map<Object,Object> currentConfig, Map<Object,Object> templateConfig);

    /**
     * Updates the current configuration with new values from the provided configuration map.
     * This method will overwrite existing keys in the current configuration with values from the new configuration.
     *
     * @param newConfig The new configuration containing updated values.
     * @param configAct The current configuration to be updated.
     */
    void updateConfig(Map<Object,Object> newConfig, Map<Object,Object> configAct);

    /**
     * Writes the provided configuration map to the specified file.
     *
     * @param file The file to which the configuration should be written.
     * @param configAct The configuration map to be written to the file.
     */
    void writeFile(File file, Map<Object, Object> configAct);

    /**
     * Retrieves a value from the configuration based on the provided path and casts it to the specified type.
     *
     * @param path The path to the desired configuration value, using dot notation for nested keys.
     * @param config The configuration map from which to retrieve the value.
     * @param type The class type to which the retrieved value should be cast.
     * @param <T> The generic type parameter representing the expected return type.
     * @return The value from the configuration cast to the specified type, or null if the path does not exist or cannot be cast.
     */
    <T> T get(String path, Map<Object,Object> config, Class<T> type);
}
