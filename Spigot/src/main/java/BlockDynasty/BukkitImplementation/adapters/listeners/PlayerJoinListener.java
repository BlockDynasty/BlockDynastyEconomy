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

package BlockDynasty.BukkitImplementation.adapters.listeners;

import BlockDynasty.BukkitImplementation.adapters.platformAdapter.EntityPlayerAdapter;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;

import abstractions.platform.scheduler.ContextualTask;
import com.blockdynasty.economy.platform.listeners.IPlayerJoin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {
    private IPlayerJoin playerJoin;

    public PlayerJoinListener(IPlayerJoin playerJoin) {
        this.playerJoin = playerJoin;
    }

    /**
     * PlayerLoginEvent Deprecated on paper > 1.21.7
     * Not Critical for this plugin,recommended to use PlayerJoinEvent only.
     * **/
    //@Deprecated
    //@EventHandler(priority = EventPriority.LOWEST)
    //public void onLogin(PlayerLoginEvent event) {
    //    Player player = event.getPlayer();
    //    Scheduler.run(ContextualTask.build(() -> loadPlayerAccount(player)));
    //}

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Scheduler.run(ContextualTask.build(() -> loadPlayerAccount(player)));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        removePlayerCache(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        removePlayerCache(event.getPlayer());
    }

    private void removePlayerCache(Player player) {
        playerJoin.offLoadPlayerAccount(  EntityPlayerAdapter.of(player));
    }

    //si se comienza a trabajar en online se van a buscar las cuentas por uuid y se va a preguntar si cambio el nombre para actualizar en sistema.
    protected void loadPlayerAccount(Player player) {
        playerJoin.loadPlayerAccount( EntityPlayerAdapter.of(player));
    }
}


