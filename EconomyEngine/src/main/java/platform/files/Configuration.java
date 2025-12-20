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
import platform.files.yaml.YamlConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Configuration extends YamlConfig implements IConfiguration {
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
            this.config = createNewFile(configFile, templatePath);
        } else {
            this.config = loadFile(configFile, templatePath);
        }
        if(!this.buttonsFile.exists()) {
            this.configButtons = createNewFile(buttonsFile,configButtonsName);
        } else {
            this.configButtons = loadFile(buttonsFile,configButtonsName);
        }
    }

    @Override
    public void saveButtonConfig(int slot, boolean value){
        Map<Integer, Boolean> bankConfig = (Map<Integer, Boolean>) this.configButtons.get("bank");
        if (bankConfig != null) {
            bankConfig.put(slot, value);
            writeFile(buttonsFile, this.configButtons);
        }
    }

    @Override
    public Map<Integer, Boolean> getButtonsConfig(){
        Map<Integer, Boolean> bankConfig = (Map<Integer, Boolean>) this.configButtons.get("bank");
        if(bankConfig != null)return bankConfig;
        return new HashMap<>();
    }

    public boolean getBoolean(String path) {
        Boolean value = get(path, config, Boolean.class);
        return value != null && value;
    }

    public String getString(String path) {
        String value = get(path,config, String.class);
        return value == null ? "Unknown" : value;
    }

    public int getInt(String path) {
        Integer value = get(path,config,Integer.class);
        return value != null ? value : 0;
    }

    public double getDouble(String path) {
        Double value = get(path,config, Double.class);
        return value != null ? value : 0.0;
    }

    public String getDatabasePath() {
        return rootDirectory.getAbsolutePath()+ databasePath;
    }

    public String getLogsPath() {
        return  rootDirectory.getAbsolutePath()+ logsPath;
    }
}