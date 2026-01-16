package com.BlockDynasty.hytale.adapters.listener;


import com.BlockDynasty.hytale.adapters.PlayerAdapter;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import platform.listeners.IPlayerJoin;

public class PlayerQuitListener {
    private IPlayerJoin playerJoin;

    public PlayerQuitListener(IPlayerJoin playerJoin) {
        this.playerJoin = playerJoin;
    }

    public void onPlayerQuit(PlayerConnectEvent event) {
        playerJoin.offLoadPlayerAccount(new PlayerAdapter(event.getPlayerRef()));
    }
}
