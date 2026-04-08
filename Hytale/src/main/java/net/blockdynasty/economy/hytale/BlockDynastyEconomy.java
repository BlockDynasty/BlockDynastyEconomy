package net.blockdynasty.economy.hytale;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import net.blockdynasty.economy.engine.Economy;
import net.blockdynasty.economy.hytale.adapters.Gui.HudService;
import net.blockdynasty.economy.hytale.adapters.HytaleAdapter;
import net.blockdynasty.economy.hytale.adapters.commands.CommandRegister;
import net.blockdynasty.economy.hytale.adapters.listener.PlayerJoinListener;
import net.blockdynasty.economy.hytale.adapters.listener.PlayerQuitListener;
import net.blockdynasty.economy.hytale.adapters.listener.PlayerReadyListener;
import net.blockdynasty.economy.hytale.integrations.vaultUnlocked.Vault;
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
        PlayerReadyListener playerReadyListener = new PlayerReadyListener();
        this.getEventRegistry().registerGlobal(PlayerConnectEvent.class, playerJoinListener::onPlayerConnect);
        this.getEventRegistry().registerGlobal(PlayerDisconnectEvent.class, playerQuitListener::onPlayerQuit);
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, playerReadyListener::onPlayerReady);
        Vault.register();
        HudService.start();
    }

    public static BlockDynastyEconomy getInstance() {
        return instance;
    }
}