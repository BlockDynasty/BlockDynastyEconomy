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

package BlockDynasty.BukkitImplementation;

import BlockDynasty.BukkitImplementation.Integrations.treasuryEconomy.TreasuryHook;
import BlockDynasty.BukkitImplementation.adapters.GUI.listener.ClickListener;
import BlockDynasty.BukkitImplementation.adapters.GUI.listener.CloseListener;
import BlockDynasty.BukkitImplementation.Integrations.Placeholder.PlaceHolder;
import BlockDynasty.BukkitImplementation.adapters.platformAdapter.EntityPlayerAdapter;
import BlockDynasty.BukkitImplementation.adapters.proxy.ChannelRegister;
import BlockDynasty.BukkitImplementation.Integrations.vault.Vault;

import BlockDynasty.BukkitImplementation.adapters.platformAdapter.BukkitAdapter;
import BlockDynasty.BukkitImplementation.adapters.commands.CommandRegister;
import BlockDynasty.BukkitImplementation.adapters.listeners.PlayerJoinListener;

import BlockDynasty.BukkitImplementation.utils.Console;
import BlockDynasty.BukkitImplementation.utils.Updater;
import BlockDynasty.BukkitImplementation.utils.Version;
import Main.Economy;
import api.IApi;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import platform.files.Configuration;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockDynastyEconomy extends JavaPlugin {
    private static BlockDynastyEconomy instance;
    private static Economy economy;
    private static Configuration configuration;
    private Metrics metrics;

    @Override
    public void onEnable() {
        if (Version.isUnsupportedVersion()){
            getLogger().severe("Unsupported Minecraft version detected: " + org.bukkit.Bukkit.getBukkitVersion());
            getLogger().severe("Disabling plugin to prevent issues.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        instance = this;
        try {
            registerEconomyCore();
            registerCommands();
            registerEvents();
            registerIntegrations();
            Console.log("§aPlugin enabled successfully!");
        } catch (Exception e) {
            getLogger().severe("An error occurred during plugin initialization: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }

        try {
            metrics= new Metrics(this, 27470);
            Updater.check(this,129308);
        }catch (Exception e) {}
    }

    public void reload() {
        Console.log("Reloading plugin...");
        HandlerList.unregisterAll();
        PlaceHolder.unregister();
        Vault.unhook();
        ChannelRegister.unhook(this);
        Economy.shutdown();
        try {
            registerEconomyCore();
            registerEvents();
            Bukkit.getOnlinePlayers().forEach(player -> {economy.getPlayerJoinListener().loadPlayerAccount(EntityPlayerAdapter.of(player));});
            registerIntegrations();
            Console.log("§aPlugin enabled successfully!");
        } catch (Exception e) {
            Console.logError("during plugin initialization: " + e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        Economy.shutdown();
        Vault.unhook();
        PlaceHolder.unregister();
        ChannelRegister.unhook(this);
        if (metrics != null) {
            metrics.shutdown();
        }
    }

    private void registerEconomyCore() {
        //int expireCacheTopMinutes = getConfig().getInt("expireCacheTopMinutes", 60);
        economy = Economy.init(new BukkitAdapter());
        configuration = economy.getConfiguration();
    }
    private void registerCommands(){
        CommandRegister.registerAllEconomySystem();
    }
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(economy.getPlayerJoinListener()), this);
        getServer().getPluginManager().registerEvents(new ClickListener(),this);
        getServer().getPluginManager().registerEvents(new CloseListener(),this);

    }
    private void registerIntegrations() {
        Vault.init(economy.getApiWithLog(economy.getVaultLogger()));
        PlaceHolder.register(economy.getPlaceHolder());
        TreasuryHook.register();
        ChannelRegister.init(this);
    }

    public static BlockDynastyEconomy getInstance() {
        return instance;
    }
    public static Configuration getConfiguration() {
        return configuration;
    }
    public static IApi getApi() {
        return economy.getApi();
    }
}