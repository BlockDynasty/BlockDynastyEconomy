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

import platform.files.yaml.YamlConfig;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Languages extends YamlConfig {
    private final Configuration config;
    private final String languagePath = "/languages";
    private String[] languagesFiles = {"EN.yaml", "ES.yaml","RU.yaml","ZH.yaml","DE.yaml","FR.yaml","IT.yaml"};
    //string nombre EN, ES, etc y File objeto File
    private Map<String,File> languageFileMap = new HashMap<>();
    private static Map<Object, Object> mensajes=new HashMap<>();
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
        mensajes = loadFile(langFile, "languages/"+langFile.getName());
    }


    public String getMessage(String key) {
        return get(key, mensajes, String.class);
    }

}
