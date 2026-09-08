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

package net.blockdynasty.economy.engine.repository.hibernate.ConnectionHandler.Hibernate;

import net.blockdynasty.economy.engine.repository.hibernate.DbConfig;
import org.mariadb.jdbc.MariaDbDataSource;

public class ConnectionHibernateMariaDb extends ConnectionHibernate {

    public ConnectionHibernateMariaDb(DbConfig dbConfig) {
        super();
        // Modo JDBC URL - HikariCP lo manejará correctamente
        configuration.setProperty("hibernate.connection.url",
                "jdbc:mariadb://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + dbConfig.getDatabase() +
                        "?useServerPrepStmts=true&cachePrepStmts=true");
        configuration.setProperty("hibernate.connection.username", dbConfig.getUsername());
        configuration.setProperty("hibernate.connection.password", dbConfig.getPassword());
        configuration.setProperty("hibernate.connection.driver_class", "org.mariadb.jdbc.Driver");

        // Propiedades de pool HikariCP (válidas en modo jdbcUrl)
        configuration.setProperty("hibernate.hikari.maximumPoolSize", "20");
        configuration.setProperty("hibernate.hikari.minimumIdle", "5");
        configuration.setProperty("hibernate.hikari.connectionTimeout", "30000");

        // Las opciones de preparación de statements van en la URL, no como propiedades separadas
        // (ya incluídas arriba como parámetros de URL)

        this.init();
    }

    @Override
    protected void stopServer() {

    }
}
