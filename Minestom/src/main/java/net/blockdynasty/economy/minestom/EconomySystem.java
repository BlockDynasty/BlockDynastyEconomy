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

package net.blockdynasty.economy.minestom;

import net.blockdynasty.economy.api.DynastyEconomy;
import net.blockdynasty.economy.engine.Economy;
import net.blockdynasty.economy.minestom.services.PermissionsService;
import net.blockdynasty.economy.minestom.adapters.PermsService;
import net.blockdynasty.economy.minestom.adapters.PlatformAdapter;
import net.blockdynasty.economy.minestom.adapters.commands.Commands;
import net.blockdynasty.economy.minestom.adapters.events.ClickInventoryEvent;
import net.blockdynasty.economy.minestom.adapters.events.playerExitEvent;
import net.blockdynasty.economy.minestom.adapters.events.playerJoinEvent;
import net.blockdynasty.economy.minestom.services.PermsServiceDefault;

import java.util.Optional;

public class EconomySystem {
    private static Economy economy;

    /**
    * Start the economy system
    * @param onlineModeServer whether the server is in online mode or not "AuthMode"
    * @param permissionsService the permissions service implementation for the server to handle permissions players
     *@implNote  PermsServiceDefault class is provided as default implementation
     * **/
    public static void start(boolean onlineModeServer, PermissionsService permissionsService) {
        PermsService.setPermissionsService(permissionsService);
        EconomySystem.economy= Economy.init(new PlatformAdapter(onlineModeServer));
        playerJoinEvent.register(Economy.getPlayerJoinListener());
        playerExitEvent.register(Economy.getPlayerJoinListener());
        Commands.register();
        ClickInventoryEvent.register();
    }

    /**
     * Start the economy system
     * @param onlineModeServer whether the server is in online mode or not "AuthMode"
     *@implNote  PermsServiceDefault class is provided as default implementation
     * **/
    public static void start(boolean onlineModeServer) {
        PermsService.setPermissionsService(new PermsServiceDefault());
        EconomySystem.economy= Economy.init(new PlatformAdapter(onlineModeServer));
        playerJoinEvent.register(Economy.getPlayerJoinListener());
        playerExitEvent.register(Economy.getPlayerJoinListener());
        Commands.register();
        ClickInventoryEvent.register();
    }

    /**
     * Stop the economy system and save all data
     * **/
    public static void stop(){
        Economy.shutdown();
    }

    /** Check if the economy system is started
     *
     * @return true if the economy system is started, false otherwise
     */
    public static boolean isStarted(){
        return EconomySystem.economy!=null;
    }


    /**
     * Get the economy api instance
     * @return Optional of DynastyEconomy api instance, empty if the economy system is not started
     * **/
    public static Optional<DynastyEconomy> getApi(){
        return Economy.getApi();
    }

}
