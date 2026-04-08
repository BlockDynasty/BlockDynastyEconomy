package net.blockdynasty.economy.hytale.adapters.listener;

import net.blockdynasty.economy.hytale.adapters.Gui.HudService;
import net.blockdynasty.economy.hytale.adapters.HytaleAdapter;
import net.blockdynasty.economy.hytale.adapters.PlayerAdapter;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import net.blockdynasty.economy.engine.platform.listeners.IPlayerJoin;

public class PlayerQuitListener {
    private IPlayerJoin playerJoin;

    public PlayerQuitListener(IPlayerJoin playerJoin) {
        this.playerJoin = playerJoin;
    }

    public void onPlayerQuit(PlayerDisconnectEvent event) {
        HytaleAdapter.disconnectPlayer(event.getPlayerRef());
        playerJoin.offLoadPlayerAccount(new PlayerAdapter(event.getPlayerRef()));

        HudService.removeHud(event.getPlayerRef());
    }
}
