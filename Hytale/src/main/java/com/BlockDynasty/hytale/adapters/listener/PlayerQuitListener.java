package com.BlockDynasty.hytale.adapters.listener;

import com.BlockDynasty.hytale.adapters.HytaleAdapter;
import com.BlockDynasty.hytale.adapters.PlayerAdapter;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.blockdynasty.economy.platform.listeners.IPlayerJoin;

public class PlayerQuitListener {
    private IPlayerJoin playerJoin;

    public PlayerQuitListener(IPlayerJoin playerJoin) {
        this.playerJoin = playerJoin;
    }

    public void onPlayerQuit(PlayerDisconnectEvent event) {
        HytaleAdapter.disconnectPlayer(event.getPlayerRef());
        playerJoin.offLoadPlayerAccount(new PlayerAdapter(event.getPlayerRef()));
    }
}
