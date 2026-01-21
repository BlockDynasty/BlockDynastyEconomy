package com.BlockDynasty.hytale;

import Main.Economy;
import com.BlockDynasty.hytale.adapters.HytaleAdapter;
import com.BlockDynasty.hytale.adapters.commands.CommandRegister;
import com.BlockDynasty.hytale.adapters.listener.PlayerJoinListener;
import com.BlockDynasty.hytale.adapters.listener.PlayerQuitListener;
import com.BlockDynasty.hytale.integrations.vaultUnlocked.Vault;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import javax.annotation.Nonnull;

public class BlockDynastyEconomy extends JavaPlugin {
    private static BlockDynastyEconomy instance;
    private Economy economy;

    public BlockDynastyEconomy(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    @Override
    protected void setup() {
        economy= Economy.init(new HytaleAdapter());
        CommandRegister.registerCommands();
        PlayerJoinListener playerJoinListener = new PlayerJoinListener(economy.getPlayerJoinListener());
        PlayerQuitListener playerQuitListener = new PlayerQuitListener(economy.getPlayerJoinListener());
        this.getEventRegistry().registerGlobal(PlayerConnectEvent.class, playerJoinListener::onPlayerConnect);
        this.getEventRegistry().registerGlobal(PlayerDisconnectEvent.class, playerQuitListener::onPlayerQuit);
        Vault.register();

    }

    public static BlockDynastyEconomy getInstance() {
        return instance;
    }
}