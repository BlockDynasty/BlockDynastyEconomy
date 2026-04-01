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

import com.mysql.cj.jdbc.MysqlDataSource;
import net.blockdynasty.economy.engine.repository.hibernate.DbConfig;

public class ConnectionHibernateMysql extends ConnectionHibernate {

    public ConnectionHibernateMysql(DbConfig dbConfig) {
        super();
        configuration.setProperty("hibernate.hikari.dataSourceClassName", MysqlDataSource.class.getName());
        configuration.setProperty("hibernate.hikari.dataSource.url", "jdbc:mysql://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + dbConfig.getDatabase());
        configuration.setProperty("hibernate.hikari.dataSource.user", dbConfig.getUsername());
        configuration.setProperty("hibernate.hikari.dataSource.password", dbConfig.getPassword());

        configuration.setProperty("hibernate.hikari.maximumPoolSize", "20");
        configuration.setProperty("hibernate.hikari.minimumIdle", "5");
        configuration.setProperty("hibernate.hikari.connectionTimeout", "30000"); // 30 segundos de espera máx.
        configuration.setProperty("hibernate.hikari.idleTimeout", "600000");     // 10 minutos
        configuration.setProperty("hibernate.hikari.maxLifetime", "1800000");    // 30 minutos

        configuration.setProperty("hibernate.hikari.dataSource.cachePrepStmts", "true");
        configuration.setProperty("hibernate.hikari.dataSource.prepStmtCacheSize", "250");
        configuration.setProperty("hibernate.hikari.dataSource.prepStmtCacheSqlLimit", "2048");
        this.init();
    }

    @Override
    protected void stopServer() {

    }
}