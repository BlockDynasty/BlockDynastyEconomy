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

public class ConnectionHibernateMariaDb extends ConnectionHibernate {

    public ConnectionHibernateMariaDb(String host, int port, String database, String username, String password) {
        super();
        configuration.setProperty("hibernate.connection.driver_class", "com.BlockDynasty.mariadb.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mariadb://" + host + ":" + port + "/" + database);
        configuration.setProperty("hibernate.connection.username", username);
        configuration.setProperty("hibernate.connection.password", password);
        this.init();
    }

    @Override
    protected void stopServer() {

    }
}
