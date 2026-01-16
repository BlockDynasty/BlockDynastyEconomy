package platform.files;

import lib.abstractions.IConfigurationGUI;
import services.configuration.IConfiguration;

public interface IConfigurationEngine extends IConfiguration, IConfigurationGUI {
    String getDatabasePath();
    String getLogsPath();
}
