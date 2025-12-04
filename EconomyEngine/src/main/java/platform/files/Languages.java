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

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Languages{
    private final Configuration config;
    private final String languagePath = "/languages";
    private String[] languagesFiles = {"EN.yaml", "ES.yaml","RU.yaml","ZH.yaml"};
    //string nombre EN, ES, etc y File objeto File
    private Map<String,File> languageFileMap = new java.util.HashMap<>();
    private static Map<String, Object> mensajes=new HashMap<>();
    private File langDirectory;
    private File rootDirectory;

    public Languages(File rootDirectory,Configuration config) {
        this.config = config;
        this.rootDirectory = rootDirectory;
        this.langDirectory = new File(rootDirectory, languagePath);
        if (!this.langDirectory.exists()) {
            this.langDirectory.mkdirs();
        }

        copyMissingLanguageFiles();
        loadLanguagesFiles();

        String lang= config.getString("lang");
        loadMessages(Objects.requireNonNullElse(lang, "EN"));
    }

    //crear directorio y volcar los archivos de idiomas
    private void createLanguagesDirectory() {
        try {
            if (!langDirectory.exists()) {
                langDirectory.mkdirs();
            }
            for (String langFile : languagesFiles) {
                File file = new File(langDirectory, langFile);
                if (!file.exists()) {
                    try (var inputStream = getClass().getClassLoader().getResourceAsStream("languages/" + langFile)) {
                        if (inputStream == null) {
                            throw new RuntimeException("Language resource not found: " + langFile);
                        }
                        Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
            loadLanguagesFiles();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create languages directory", e);
        }
    }
    private void copyMissingLanguageFiles() {
        try {
            for (String langFile : languagesFiles) {
                File file = new File(langDirectory, langFile);
                if (!file.exists()) {
                    try (var inputStream = getClass().getClassLoader().getResourceAsStream("languages/" + langFile)) {
                        if (inputStream == null) {
                            throw new RuntimeException("Language resource not found: " + langFile);
                        }
                        Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy language files", e);
        }
    }

    private void loadLanguagesFiles() {
        File[] files = langDirectory.listFiles((dir, name) -> name.endsWith(".yaml") || name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                String langCode = fileName.substring(0, fileName.lastIndexOf('.')).toUpperCase();
                languageFileMap.put(langCode, file);
            }
        }
    }

    public void loadMessages(String langCode) {
        langCode = langCode.toUpperCase();
        File langFile = languageFileMap.get(langCode);
        if (langFile == null) {
            langFile = languageFileMap.get("EN"); // default to English if not found

            if (langFile == null) {
                throw new RuntimeException("No language files available");
            }
        }
            //buscar mensaje en el archivo encontrado basado en la key
            try {
                Yaml yaml = new Yaml();
                mensajes = yaml.load(Files.newInputStream(langFile.toPath()));
            }catch (Exception e) {
                throw new RuntimeException("Failed to load language file: " + langFile.getName(), e);
            }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path, Class<T> type) {
        String[] parts = path.split("\\.");
        Map<String, Object> current = mensajes;

        for (int i = 0; i < parts.length - 1; i++) {
            if (!current.containsKey(parts[i])) return null;
            current = (Map<String, Object>) current.get(parts[i]);
        }

        String lastPart = parts[parts.length - 1];
        if (!current.containsKey(lastPart)) return null;

        Object value = current.get(lastPart);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    public String getMessage(String key) {
        return get(key, String.class);
    }

}
