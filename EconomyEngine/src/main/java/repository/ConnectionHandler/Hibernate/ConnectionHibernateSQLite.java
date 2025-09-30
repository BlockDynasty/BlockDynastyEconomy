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

package repository.ConnectionHandler.Hibernate;

import Main.Console;
import org.h2.tools.Server;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class ConnectionHibernateSQLite  extends   ConnectionHibernate{
    public ConnectionHibernateSQLite(String dbFilePath,boolean enableServerConsole) {
        super();
        configuration.setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC");
        configuration.setProperty("hibernate.connection.url", "jdbc:sqlite:" + dbFilePath+ "/database.db");
        configuration.setProperty("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
        this.init();

        if (enableServerConsole) {
            startServerConsole(dbFilePath);
        }
    }

    // This method starts the H2 web console server.
    private void startServerConsole(String dbFilePath){
        /*try {
            //System.setProperty("h2.consoleForcePassword", "true");
            Server webServer = Server.createWebServer(
                    "-web",
                    "-webAllowOthers",
                    "-webPort", "8082",
                    "-baseDir",dbFilePath);
            webServer.start();

            Console.log("Console started at: http://localhost:8082"
                    + "\n                               ->  JDBC URL: jdbc:sqlite:" + dbFilePath + "/database.db"
                     );
        } catch (SQLException e) {
            Console.logError("Failed to start Web Console: " + e.getMessage());
        }*/
        try {
            // Skip password requirement
            System.setProperty("h2.consoleForcePassword", "false");

            // Set default settings for the console
            System.setProperty("h2.bindAddress", "localhost");
            System.setProperty("h2.webAllowOthers", "true");
            System.setProperty("h2.webAdminPassword", "");

            // Create web server with customized settings
            Server webServer = Server.createWebServer(
                    "-web",
                    "-webAllowOthers",
                    "-webPort", "8082",
                    "-baseDir", dbFilePath);
            webServer.start();

            // Generate direct access URL with pre-filled connection details
            String jdbcUrl = "jdbc:h2:file:" + dbFilePath + "/h2Database";
            String consoleUrl = "http://localhost:8082/";
            String directAccessUrl = consoleUrl + "login.jsp?jsessionid=autocomplete&url=" +
                    java.net.URLEncoder.encode(jdbcUrl, StandardCharsets.UTF_8) +
                    "&user=sa&password=";

            Console.log("  -> SQL WEB console started at: " + consoleUrl);
            Console.log("  -> Direct access URL: " + directAccessUrl);
            Console.log("  -> JDBC URL: " + jdbcUrl);
        } catch (Exception e) {
            Console.logError("Failed to start H2 console: " + e.getMessage());
        }
    }
}
