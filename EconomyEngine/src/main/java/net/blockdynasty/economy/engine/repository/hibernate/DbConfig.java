package net.blockdynasty.economy.engine.repository.hibernate;

import net.blockdynasty.economy.engine.platform.files.IConfigurationEngine;

public class DbConfig {
    public enum TYPE{
        MYSQL,
        MARIADB,
        H2,
        SQLITE
    }

    private final String databasePath;
    private final String host;
    private final TYPE type;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final boolean enableWebEditorSqlServer;

    public DbConfig(IConfigurationEngine fileConfig) {
        this.databasePath = fileConfig.getDatabasePath();
        this.host = fileConfig.getString("sql.host");

        String typeStr = fileConfig.getString("sql.type").toUpperCase();
        try {
            this.type= TYPE.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported database type: " + typeStr);
        }

        this.port = fileConfig.getInt("sql.port");
        this.database = fileConfig.getString("sql.database");
        this.username = fileConfig.getString("sql.username");
        this.password = fileConfig.getString("sql.password");
        this.enableWebEditorSqlServer = fileConfig.getBoolean("sql.EnableWebEditorSqlServer");
    }

    public String getDatabasePath() {
        return databasePath;
    }

    public String getHost() {
        return host;
    }

    public TYPE getType() {
        return type;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnableWebEditorSqlServer() {
        return enableWebEditorSqlServer;
    }
}
