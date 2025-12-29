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

package Main;

import BlockDynasty.Economy.Core;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;
import api.Api;
import api.IApi;
import lib.abstractions.IProxySubscriber;
import lib.gui.GUISystem;
import platform.files.Configuration;
import platform.files.Languages;
import platform.files.logs.EconomyLogger;
import platform.files.logs.VaultLogger;
import lib.commands.CommandService;
import lib.abstractions.PlatformAdapter;
import lib.placeholder.PlaceHolder;
import lib.util.colors.ChatColor;
import platform.listeners.EventListener;
import platform.listeners.IPlayerJoin;
import platform.listeners.PlayerJoinListener;
import MessageChannel.proxy.ProxySubscriber;
import MessageChannel.proxy.ProxyPublisher;
import MessageChannel.redis.RedisPublisher;
import MessageChannel.redis.RedisData;
import MessageChannel.redis.RedisSubscriber;
import repository.ConnectionHandler.Hibernate.*;
import repository.Repository;
import services.Message;
import utils.Console;

public class Economy {
    private Core core;
    private static IRepository repository;
    private PlayerJoinListener playerJoinListener;
    private IApi api;
    private PlaceHolder placeHolder;
    private static RedisSubscriber subscriber;
    private Configuration configuration;
    private Languages languages;
    private PlatformAdapter platformAdapter;

    private Economy(PlatformAdapter platformAdapter){
        this.platformAdapter=platformAdapter;
        this.configuration= new Configuration(platformAdapter.getDataFolder());
        ChatColor.setupSystem(platformAdapter.hasSupportAdventureText(),configuration.getBoolean("forceVanillaColorsSystem") );

        this.languages = new Languages(platformAdapter.getDataFolder(),configuration);
        Message.addLang(languages);
        Console.setConsole(platformAdapter.getConsole(),configuration);

        this.initDatabase(configuration);

        this.core=new Core(repository,60,createPublisher(configuration,platformAdapter),new EconomyLogger( configuration,platformAdapter.getScheduler()));
        this.createSubscriber(configuration,platformAdapter);
        this.api = new Api(core.getUseCaseFactory(),core.getServicesManager().getAccountService());
        this.placeHolder = new PlaceHolder(core.getUseCaseFactory());
        this.playerJoinListener = new PlayerJoinListener(core.getUseCaseFactory(),core.getServicesManager().getAccountService(),configuration.getBoolean("online"),platformAdapter.isOnlineMode());
        CommandService.init(platformAdapter,core.getUseCaseFactory());
        GUISystem.init(core.getUseCaseFactory(),platformAdapter,new Message(),configuration);
        EventListener.register(core.getServicesManager().getEventManager(),platformAdapter);
    }

    public static Economy init( PlatformAdapter platformAdapter){
        return new Economy(platformAdapter);
    }

    private void initDatabase(Configuration configuration){
        try{
            Connection connection = getConnectionDatabase(configuration);
            repository = new Repository(connection);
            Console.log("Database connected successfully.");
        }catch (Exception e){
            Console.logError("Error connection database, check your credentials.");
            throw new RuntimeException(e.getMessage());
        }
    }
    private Connection getConnectionDatabase(Configuration configuration){
        switch (configuration.getString("sql.type")){
            case "mysql":
                return new ConnectionHibernateMysql(configuration.getString("sql.host"), configuration.getInt("sql.port"), configuration.getString("sql.database"), configuration.getString("sql.username"), configuration.getString("sql.password"));
            case "mariadb":
                return new ConnectionHibernateMariaDb(configuration.getString("sql.host"), configuration.getInt("sql.port"), configuration.getString("sql.database"), configuration.getString("sql.username"), configuration.getString("sql.password"));
            case "h2":
                return new ConnectionHibernateH2(configuration.getDatabasePath(),configuration.getBoolean("sql.EnableWebEditorSqlServer"));
            case "sqlite":
                return new ConnectionHibernateSQLite(configuration.getDatabasePath(),configuration.getBoolean("sql.EnableWebEditorSqlServer"));
            default:
                throw new IllegalArgumentException("Unsupported database type: " + configuration.getString("sql.type"));
        }
    }

    private Courier createPublisher(Configuration configuration, PlatformAdapter platformAdapter){
        if(configuration.getBoolean("redis.enabled")){
            Console.log("Redis Enabled");
            return new RedisPublisher( new RedisData(configuration),platformAdapter);
        }else{
            Console.log("Redis Disable - Using Proxy System");
            return new ProxyPublisher(platformAdapter);
        }
    }
    private void createSubscriber(Configuration configuration, PlatformAdapter platformAdapter){
        if(configuration.getBoolean("redis.enabled")){
            subscriber = new RedisSubscriber(new RedisData(configuration),platformAdapter,
                    core.getServicesManager().getOfferService(),
                    core.getServicesManager().getCurrencyService(),
                    core.getServicesManager().getAccountService(),
                    core.getServicesManager().getEventManager());
        }else{
            IProxySubscriber proxySubscriber = new ProxySubscriber(platformAdapter,
                    core.getServicesManager().getOfferService(),
                    core.getServicesManager().getCurrencyService(),
                    core.getServicesManager().getAccountService(),
                    core.getServicesManager().getEventManager());
        }
    }

    public IPlayerJoin getPlayerJoinListener(){
        return playerJoinListener;
    }

    public IApi getApi(){
        return api;
    }
    public PlaceHolder getPlaceHolder(){
        return placeHolder;
    }

    public IApi getApiWithLog(Log log){
        return new Api(core.getUseCaseFactory(),core.getServicesManager().getAccountService(), log);
    }

    public Configuration getConfiguration(){
        return configuration;
    }

    public Log getVaultLogger(){
        return VaultLogger.build(configuration,platformAdapter.getScheduler());
    }

    public static void shutdown(){
        if (repository != null) {
            repository.close();
        }
        if (subscriber != null) {
            subscriber.stopListening();
        }
    }
}
