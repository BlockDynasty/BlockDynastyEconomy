package net.blockdynasty.economy.engine.repository.hibernate;

import net.blockdynasty.economy.engine.platform.files.IConfigurationEngine;
import net.blockdynasty.economy.engine.repository.hibernate.ConnectionHandler.Hibernate.ConnectionHibernateH2;
import net.blockdynasty.economy.engine.repository.hibernate.ConnectionHandler.Hibernate.ConnectionHibernateMariaDb;
import net.blockdynasty.economy.engine.repository.hibernate.ConnectionHandler.Hibernate.ConnectionHibernateMysql;
import net.blockdynasty.economy.engine.repository.hibernate.ConnectionHandler.Hibernate.ConnectionHibernateSQLite;

import java.util.Map;
import java.util.function.Function;

public class ConnectionFactory{
    private Connection connection;
    private final Map<DbConfig.TYPE, Function<DbConfig, Connection>> connectionStrategies= Map.ofEntries(
            Map.entry(DbConfig.TYPE.MYSQL, ConnectionHibernateMysql::new),
            Map.entry(DbConfig.TYPE.MARIADB, ConnectionHibernateMariaDb::new),
            Map.entry(DbConfig.TYPE.H2, ConnectionHibernateH2::new),
            Map.entry(DbConfig.TYPE.SQLITE, ConnectionHibernateSQLite::new)
    );

    public ConnectionFactory(IConfigurationEngine fileConfig){
        DbConfig dbConfig = new DbConfig(fileConfig);
        this.connection = connectionStrategies.get(dbConfig.getType()).apply(dbConfig);
    }

    public Connection get(){
        return this.connection;
    }
}
