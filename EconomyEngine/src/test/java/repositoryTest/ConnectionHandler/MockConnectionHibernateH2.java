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

package repositoryTest.ConnectionHandler;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import repository.ConnectionHandler.Hibernate.Connection;
import repository.Models.AccountDb;
import repository.Models.BalanceDb;
import repository.Models.CurrencyDb;
import repository.Models.WalletDb;

public class MockConnectionHibernateH2 implements Connection {
    private SessionFactory sessionFactory;
    private Configuration configuration;

    public MockConnectionHibernateH2() {
        this.configuration = new Configuration();

        // Configure H2 in-memory database
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:testdb");
        configuration.setProperty("hibernate.connection.username", "");
        configuration.setProperty("hibernate.connection.password", "");

        // Configure Hibernate properties
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.setProperty("hibernate.connection.autocommit", "true");
        configuration.setProperty("hibernate.cache.use_second_level_cache", "false");
        configuration.setProperty("hibernate.show_sql", "false");
        configuration.setProperty("hibernate.format_sql", "false");
        configuration.setProperty("hibernate.use_sql_comments", "false");
        configuration.addAnnotatedClass(CurrencyDb.class);
        configuration.addAnnotatedClass(AccountDb.class);
        configuration.addAnnotatedClass(BalanceDb.class);
        configuration.addAnnotatedClass(WalletDb.class);
        // Initialize the session factory
        try {
            this.sessionFactory = this.configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + ex);
        }
    }

    @Override
    public SessionFactory getSession() {
        return this.sessionFactory;
    }

    @Override
    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}