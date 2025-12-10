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

package spongeV13;

import spongeV13.adapters.commands.CommandRegister;
import spongeV13.adapters.platformAdapter.SpongeAdapter;
import spongeV13.adapters.listeners.PlayerJoinListener;
import spongeV13.adapters.proxy.ProxyReceiverImp;
import spongeV13.adapters.integrations.spongeEconomyApi.EconomyServiceAdapter;
import spongeV13.utils.Console;
import Main.Economy;
import api.IApi;

import lib.commands.CommandService;
import org.apache.logging.log4j.Logger;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.lifecycle.*;
import org.spongepowered.api.network.channel.raw.RawDataChannel;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.plugin.PluginContainer;
import platform.proxy.ProxyData;
import spongeV13.utils.TestEconomyCommand;

import java.lang.invoke.MethodHandles;
import java.nio.file.Path;


public class SpongePluginCommon {
    private static PluginContainer container;
    private static Logger logger;
    private static Economy economy;
    private static Path configPath;
    private static RawDataChannel channel;
    //private final Metrics metrics;


    public SpongePluginCommon(final PluginContainer container, final Logger logger, @ConfigDir(sharedRoot = false) final Path configDir) {
        SpongePluginCommon.container = container;
        SpongePluginCommon.logger = logger;
        SpongePluginCommon.configPath = configDir;

        //metrics = factory.make(27472);
    }

    public void onEngineStarting(StartingEngineEvent<Server> event) {
        Console.log("Registering StartingEngineEvent...");
        try {
            economy = Economy.init(new SpongeAdapter());
            Console.log("Plugin constructed...");
        }catch (Exception e){
            Console.logError("Error during plugin construction: " + e.getMessage());
            throw new RuntimeException();
        }
        Sponge.eventManager().registerListeners(container, new PlayerJoinListener(economy.getPlayerJoinListener()), MethodHandles.lookup());
        CommandRegister.registerCommands(container, CommandService.getMainCommands());
        EconomyServiceAdapter.init(economy.getApi());
    }

    protected void registerTestEconomyCommand(){
        TestEconomyCommand.registerTestCommand(container); //<--- tester EconomyServiceAdapter
    }

    public void onRegisterChannel(final RegisterChannelEvent event) { //segundo
        Console.log("Registering RegisterChannelEvent...");
        channel = event.register(ResourceKey.resolve(ProxyData.getChannelName()),RawDataChannel.class);
        ProxyReceiverImp.register().addHandler(channel);
    }

    public void registerEconomyService(ProvideServiceEvent.EngineScoped<EconomyService> event) {
        event.suggest(EconomyServiceAdapter::new);
    }

    public void onServerStopping(final StoppingEngineEvent<Server> event) {
        Economy.shutdown();
    }

    public static Logger getLogger() {
        return logger;
    }
    public static PluginContainer getPlugin() {
        return container;
    }
    public static RawDataChannel getChannel() {
        return channel;
    }
    public static Path getConfigPath() {
        return configPath;
    }

    public IApi getApi() {
        return economy.getApi();
    }
}

