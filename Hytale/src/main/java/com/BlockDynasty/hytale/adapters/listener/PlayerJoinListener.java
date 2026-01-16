package com.BlockDynasty.hytale.adapters.listener;


import com.BlockDynasty.hytale.adapters.PlayerAdapter;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import platform.listeners.IPlayerJoin;

public class PlayerJoinListener {
    private IPlayerJoin playerJoin;

    public PlayerJoinListener(IPlayerJoin playerJoin) {
        this.playerJoin = playerJoin;
    }

    public void onPlayerConnect(PlayerConnectEvent event) {
        playerJoin.loadPlayerAccount(new PlayerAdapter(event.getPlayerRef()));
    }

}
