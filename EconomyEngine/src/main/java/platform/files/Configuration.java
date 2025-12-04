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

package platform.files;

import lib.abstractions.IConfiguration;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Configuration implements IConfiguration {
    private Map<Object, Object> config;
    private Map<Object, Object> configButtons;
    private final String databasePath = "/database";
    private final String templatePath = "config-template.yaml";
    private final String configName = "config.yaml";
    private final String configButtonsName = "GUIConfig.yaml";
    private final String logsPath = "/logs";
    private final File configFile;
    private final File buttonsFile;
    private final File rootDirectory;

    public Configuration(File rootDirectory) {
        this.config = new HashMap<>();
        this.configButtons = new HashMap<>();
        this.rootDirectory = rootDirectory;
        this.configFile = new File(rootDirectory, configName);
        this.buttonsFile = new File(rootDirectory, configButtonsName);
        File databaseDir = new File(rootDirectory, databasePath);
        if (!databaseDir.exists()) {
            databaseDir.mkdirs();
        }
        if (!this.configFile.exists()) {
            this.config = createNewConfigFile(configFile, templatePath);
        } else {
            this.config = loadConfig(configFile, templatePath);
        }
        if(!this.buttonsFile.exists()) {
            this.configButtons = createNewConfigFile(buttonsFile,configButtonsName);
        } else {
            this.configButtons = loadConfig(buttonsFile,configButtonsName);
        }
    }

    @Override
    public void saveButtonConfig(int slot, boolean value){
        Map<Integer, Boolean> bankConfig = (Map<Integer, Boolean>) this.configButtons.get("bank");
        if (bankConfig != null) {
            bankConfig.put(slot, value);
            writeConfigFile(buttonsFile, this.configButtons);
        }
    }

    @Override
    public Map<Integer, Boolean> getButtonsConfig(){
        Map<Integer, Boolean> bankConfig = (Map<Integer, Boolean>) this.configButtons.get("bank");
        if(bankConfig != null)return bankConfig;
        return new HashMap<>();
    }

    private void writeConfigFile(File file, Map<Object, Object> configAct) {
        try {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setProcessComments(true);
            options.setPrettyFlow(true);
            options.setIndent(2);
            Yaml yamlWriter = new Yaml(options);
            String yamlString = yamlWriter.dump(configAct);
            Files.write(file.toPath(), yamlString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write config buttons file", e);
        }
    }

    private Map<Object,Object> createNewConfigFile(File file, String templatePath) {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath)) {
                if (inputStream == null) {
                    throw new IOException("Template resource not found: " + templatePath);
                }
                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            return loadConfig( file, templatePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create config file", e);
        }
    }

    private Map<Object,Object> loadConfig(File fileConfig, String templatePath) {
        try {
            Yaml yaml = new Yaml();
            Map<Object,Object> configAct = yaml.load(Files.newInputStream(fileConfig.toPath()));

            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath)) {
                if (inputStream == null) {
                    throw new IOException("Template resource not found: " + templatePath);
                }
                Map<Object, Object> defaultConfig = yaml.load(inputStream);
                if (defaultConfig != null) {
                    if(checkIfNeedUpdate(defaultConfig, configAct)) {
                        mergeConfigs(defaultConfig, configAct);
                        writeConfigFile(configFile, configAct);
                    }
                }
            }
            return configAct;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + e.getMessage(), e);
        }
    }

    private boolean checkIfNeedUpdate(Map<Object, Object> defaultConfig, Map<Object, Object> configAct){
        for(Map.Entry<Object, Object> entry : defaultConfig.entrySet()) {
            Object key = entry.getKey();
            Object defaultValue = entry.getValue();

            if(!configAct.containsKey(key)){
                return true;
            }

            Object currentValue = configAct.get(key);
            if(defaultValue instanceof Map && currentValue instanceof Map){
                if(checkIfNeedUpdate((Map<Object, Object>) defaultValue, (Map<Object, Object>) currentValue)){
                    return true;
                }
            }
        }
        return false;
    }

    private void mergeConfigs(Map<Object, Object> defaultConfig, Map<Object, Object> configAct) {
        for (Map.Entry<Object, Object> entry : defaultConfig.entrySet()) {
            Object key = entry.getKey();
            Object defaultValue = entry.getValue();

            if (!configAct.containsKey(key)) {
                configAct.put(key, defaultValue);
            } else {
                Object currentValue = configAct.get(key);
                if (defaultValue instanceof Map && currentValue instanceof Map) {
                    mergeConfigs((Map<Object, Object>) defaultValue, (Map<Object, Object>) currentValue);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T get(String path, Class<T> type) {
        String[] parts = path.split("\\.");
        Map<Object, Object> current = config;

        for (int i = 0; i < parts.length - 1; i++) {
            if (!current.containsKey(parts[i])) return null;
            current = (Map<Object, Object>) current.get(parts[i]);
        }

        String lastPart = parts[parts.length - 1];
        if (!current.containsKey(lastPart)) return null;

        Object value = current.get(lastPart);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    public boolean getBoolean(String path) {
        Boolean value = get(path, Boolean.class);
        return value != null && value;
    }

    public String getString(String path) {
        String value = get(path, String.class);
        return value == null ? "Unknown" : value;
    }

    public int getInt(String path) {
        Integer value = get(path, Integer.class);
        return value != null ? value : 0;
    }

    public double getDouble(String path) {
        Double value = get(path, Double.class);
        return value != null ? value : 0.0;
    }

    public String getDatabasePath() {
        return rootDirectory.getAbsolutePath()+ databasePath;
    }

    public String getLogsPath() {
        return  rootDirectory.getAbsolutePath()+ logsPath;
    }
}