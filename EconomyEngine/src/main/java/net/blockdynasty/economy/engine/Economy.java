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

package net.blockdynasty.economy.engine;

import net.blockdynasty.economy.core.Core;
import net.blockdynasty.economy.core.domain.persistence.entities.IRepository;
import net.blockdynasty.economy.core.domain.services.courier.Courier;
import net.blockdynasty.economy.core.domain.services.log.Log;
import net.blockdynasty.economy.engine.repository.hibernate.Connection;
import net.blockdynasty.economy.engine.repository.hibernate.ConnectionFactory;
import net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer;
import net.blockdynasty.economy.engine.apiImplement.ApiFactory;
import net.blockdynasty.economy.engine.configFromChannel.ProxyConfigRequest;
import net.blockdynasty.economy.engine.configFromChannel.ProxyConfigSubscriber;
import net.blockdynasty.economy.libs.abstractions.platform.IProxySubscriber;
import net.blockdynasty.economy.hardcash.aplication.HardCashService;
import net.blockdynasty.economy.engine.configFromChannel.PlayerConfigJoinListener;
import net.blockdynasty.economy.gui.gui.GUISystem;
import net.blockdynasty.economy.engine.platform.IPlatform;
import net.blockdynasty.economy.engine.platform.files.Configuration;
import net.blockdynasty.economy.engine.platform.files.IConfigurationEngine;
import net.blockdynasty.economy.engine.platform.files.Languages;
import net.blockdynasty.economy.engine.platform.files.logs.EconomyLogger;
import net.blockdynasty.economy.engine.platform.files.logs.VaultLogger;
import net.blockdynasty.economy.gui.commands.CommandService;
import net.blockdynasty.economy.gui.placeholder.IPlaceHolderDynastyEconomy;
import net.blockdynasty.economy.gui.placeholder.PlaceholderFactory;
import net.blockdynasty.economy.engine.repository.hibernate.ConnectionHandler.Hibernate.*;
import net.blockdynasty.providers.services.ServiceProvider;
import net.blockdynasty.economy.libs.services.configuration.IConfiguration;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.engine.platform.listeners.EventListener;
import net.blockdynasty.economy.engine.platform.listeners.IPlayerJoin;
import net.blockdynasty.economy.engine.platform.listeners.PlayerJoinListener;
import net.blockdynasty.economy.engine.MessageChannel.proxy.ProxySubscriber;
import net.blockdynasty.economy.engine.MessageChannel.proxy.ProxyPublisher;
import net.blockdynasty.economy.engine.MessageChannel.redis.RedisPublisher;
import net.blockdynasty.economy.engine.MessageChannel.redis.RedisData;
import net.blockdynasty.economy.engine.MessageChannel.redis.RedisSubscriber;
import net.blockdynasty.economy.engine.repository.hibernate.Repository;
import net.blockdynasty.economy.engine.services.Message;
import net.blockdynasty.economy.libs.services.Console;
import net.blockdynasty.economy.api.DynastyEconomy;

import java.util.Optional;

public class Economy {
    private Core core;
    private static final ApiFactory apiFactory=new ApiFactory();;
    private static final PlaceholderFactory placeholderFactory = new PlaceholderFactory();
    private static IRepository repository;
    private static IPlayerJoin playerJoinListener;
    private static RedisSubscriber subscriber;
    private static IConfigurationEngine configuration;
    private Languages languages;
    private IPlatform platformAdapter;

    private Economy(IPlatform platformAdapter){
        this.platformAdapter=platformAdapter;
        configuration= new Configuration(platformAdapter.getDataFolder());
        ChatColor.setupSystem(platformAdapter.hasSupportAdventureText(),configuration.getBoolean("forceVanillaColorsSystem") );

        this.languages = new Languages(platformAdapter.getDataFolder(),configuration);
        Message.addLang(languages);
        Console.setConsole(platformAdapter.getConsole(),configuration);

        if(configuration.getBoolean("sync-config-with-proxy")){
            platformAdapter.registerMessageChannel(new ProxyConfigSubscriber(this,configuration));
            Economy.playerJoinListener = new PlayerConfigJoinListener(this.platformAdapter);
            if (!platformAdapter.getOnlinePlayers().isEmpty()){
                IPlayer player = platformAdapter.getOnlinePlayers().iterator().next();
                ProxyConfigRequest.request(platformAdapter, player.getUniqueId());
            }else{
                Console.log("Waiting for player connection to sync configuration with proxy...");
            }
        }else{
            startServer(configuration);
        }
    }

    public void startServer(IConfigurationEngine configuration){
        this.initDatabase(configuration);

        this.core=new Core(repository,60,createPublisher(configuration,platformAdapter),new EconomyLogger( configuration,platformAdapter.getScheduler()));

        this.createSubscriber(configuration,platformAdapter);
        Economy.playerJoinListener = new PlayerJoinListener(core.getUseCaseFactory(),core.getServicesManager().getAccountService(),configuration.getBoolean("online"),platformAdapter.isOnlineMode());
        HardCashService.init(configuration, platformAdapter, core.getUseCaseFactory().deposit(),core.getUseCaseFactory().withdraw(),core.getUseCaseFactory().pay(),core.getUseCaseFactory().searchCurrency());
        CommandService.init(platformAdapter,core.getUseCaseFactory());
        GUISystem.init(core.getUseCaseFactory(),platformAdapter,new Message(),configuration);
        EventListener.register(core.getServicesManager().getEventManager(),platformAdapter);

        apiFactory.updateDependencies(core.getUseCaseFactory(), getVaultLogger(), configuration);
        placeholderFactory.updateDependencies(core.getUseCaseFactory());
        platformAdapter.startServer(configuration);
    }

    public static Economy init( IPlatform platformAdapter){
        return new Economy(platformAdapter);
    }

    private void initDatabase(IConfigurationEngine configuration){
        try{
            ConnectionFactory connectionFactory = new ConnectionFactory(configuration);
            repository = new Repository(connectionFactory.get());
            Console.log("Database connected successfully.");
        }catch (Exception e){
            Console.logError("Error connection database, check your credentials.");
            throw new RuntimeException(e.getMessage());
        }
    }

    private Courier createPublisher(IConfiguration configuration, IPlatform platformAdapter){
        if(configuration.getBoolean("redis.enabled")){
            Console.log("Redis Enabled");
            return new RedisPublisher( new RedisData(configuration),platformAdapter);
        }else{
            Console.log("Redis Disable - Using Proxy System");
            return new ProxyPublisher(platformAdapter);
        }
    }
    private void createSubscriber(IConfiguration configuration, IPlatform platformAdapter){
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

    public static IPlayerJoin getPlayerJoinListener(){
        return playerJoinListener;
    }

    public Log getVaultLogger(){
        return VaultLogger.build(configuration,platformAdapter.getScheduler());
    }

    public static void shutdown(){
        placeholderFactory.disableService();
        apiFactory.disableService();
        EventListener.unregisterAll();
        if (repository != null) {
            repository.close();
        }
        if (subscriber != null) {
            subscriber.stopListening();
        }
    }

    public static IConfiguration getConfiguration(){
        return configuration;
    }

    public static Optional<DynastyEconomy> getApi(){
        return ServiceProvider.get(DynastyEconomy.class, service -> service.getId().equals(apiFactory.getIDApiDynamicSupplier()));
    }

    public static Optional<IPlaceHolderDynastyEconomy> getPlaceholder(){
        return ServiceProvider.get(IPlaceHolderDynastyEconomy.class, service -> service.getId().equals(placeholderFactory.getId()));
    }
}
