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

package platform.files.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public abstract class YamlConfig implements IYamlConfig {

    @Override
    public Map<Object,Object> loadFile(File fileConfig, String templatePath) {
        try {
            Yaml yaml = new Yaml();
            Map<Object,Object> configAct = yaml.load(Files.newInputStream(fileConfig.toPath()));

            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath)) {
                if (inputStream == null) {
                    throw new IOException("Template resource not found: " + templatePath);
                }
                Map<Object, Object> defaultConfig = yaml.load(inputStream);
                if (defaultConfig != null) {
                    if(checkIfNeedsUpdate(defaultConfig, configAct)) {
                        mergeFiles(defaultConfig, configAct);
                        writeFile(fileConfig, configAct);
                    }
                }
            }
            return configAct;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean checkIfNeedsUpdate(Map<Object, Object> defaultConfig, Map<Object, Object> configAct){
        for(Map.Entry<Object, Object> entry : defaultConfig.entrySet()) {
            Object key = entry.getKey();
            Object defaultValue = entry.getValue();

            if(!configAct.containsKey(key)){
                return true;
            }

            Object currentValue = configAct.get(key);
            if(defaultValue instanceof Map && currentValue instanceof Map){
                if(checkIfNeedsUpdate((Map<Object, Object>) defaultValue, (Map<Object, Object>) currentValue)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void mergeFiles(Map<Object, Object> defaultConfig, Map<Object, Object> configAct) {
        for (Map.Entry<Object, Object> entry : defaultConfig.entrySet()) {
            Object key = entry.getKey();
            Object defaultValue = entry.getValue();

            if (!configAct.containsKey(key)) {
                configAct.put(key, defaultValue);
            } else {
                Object currentValue = configAct.get(key);
                if (defaultValue instanceof Map && currentValue instanceof Map) {
                    mergeFiles((Map<Object, Object>) defaultValue, (Map<Object, Object>) currentValue);
                }
            }
        }
    }

    @Override
    public void writeFile(File file, Map<Object, Object> configAct) {
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

    @Override
    public Map<Object,Object> createNewFile(File file, String templatePath){
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

            return loadFile( file, templatePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create config file", e);
        }
    }

    @Override
    public <T> T get(String path, Map<Object,Object> config, Class<T> type) {
        String[] parts = path.split("\\.");

        for (int i = 0; i < parts.length - 1; i++) {
            if (!config.containsKey(parts[i])) return null;
            config = (Map<Object, Object>) config.get(parts[i]);
        }

        String lastPart = parts[parts.length - 1];
        if (!config.containsKey(lastPart)) return null;

        Object value = config.get(lastPart);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }
}
