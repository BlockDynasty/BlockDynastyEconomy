package net.blockdynasty.economy.hytale.adapters.listener;

import net.blockdynasty.economy.hytale.adapters.HytaleAdapter;
import net.blockdynasty.economy.hytale.adapters.PlayerAdapter;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import net.blockdynasty.economy.engine.platform.listeners.IPlayerJoin;

public class PlayerJoinListener {
    private IPlayerJoin playerJoin;

    public PlayerJoinListener(IPlayerJoin playerJoin) {
        this.playerJoin = playerJoin;
    }

    public void onPlayerConnect(PlayerConnectEvent event) {
        HytaleAdapter.connectPlayer(event.getPlayerRef());
        playerJoin.loadPlayerAccount(new PlayerAdapter(event.getPlayerRef()));
    }

}
