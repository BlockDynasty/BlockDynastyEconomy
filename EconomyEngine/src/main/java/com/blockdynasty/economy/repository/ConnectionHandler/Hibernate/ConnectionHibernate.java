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

package com.blockdynasty.economy.repository.ConnectionHandler.Hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.blockdynasty.economy.repository.Models.AccountDb;
import com.blockdynasty.economy.repository.Models.BalanceDb;
import com.blockdynasty.economy.repository.Models.CurrencyDb;
import com.blockdynasty.economy.repository.Models.WalletDb;

public abstract class ConnectionHibernate implements Connection {
    private SessionFactory sessionFactory;
    protected Configuration configuration = new Configuration();

    public ConnectionHibernate() {
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.connection.autocommit", "true");
        configuration.setProperty("hibernate.cache.use_second_level_cache", "false");
        configuration.setProperty("hibernate.show_sql", "false"); //todo: setup for debug
        configuration.setProperty("hibernate.format_sql", "false");  //todo: setup for debug
        configuration.setProperty("hibernate.use_sql_comments", "false");//todo: setup for debug
        configuration.addAnnotatedClass(CurrencyDb.class);
        configuration.addAnnotatedClass(AccountDb.class);
        configuration.addAnnotatedClass(BalanceDb.class);
        configuration.addAnnotatedClass(WalletDb.class);
    }

    protected void init(){
        try {
            this.sessionFactory = this.configuration.buildSessionFactory();
            //sessionFactory = new Configuration().configure().buildSessionFactory(); //bildea la sesion con los parametros del archivo hibernate.cfg.xml
        } catch (Exception ex) {
            throw new RuntimeException("Initial SessionFactory creation failed: " + ex);
        }
    }

    @Override
    public SessionFactory getSession() {
        return sessionFactory;
    }

    @Override
    public void close() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
        stopServer();
    }

    protected abstract void stopServer();

}