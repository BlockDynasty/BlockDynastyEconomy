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

package adapters;

import Main.Economy;
import com.BlockDynasty.api.DynastyEconomy;
import adapters.commands.Commands;
import adapters.events.ClickInventoryEvent;
import adapters.events.playerExitEvent;
import adapters.events.playerJoinEvent;

//build economy engine with dependency injection
public class EconomySystem {
    private static Economy economy;

    /**
    * Start the economy system
    * @param onlineModeServer whether the server is in online mode or not "AuthMode"
    * @param permissionsService the permissions service implementation for the server to handle permissions players
     *@implNote  PermsServiceDefault class is provided as default implementation
     * **/
    public static void start(boolean onlineModeServer,PermissionsService permissionsService) {
        PermsService.setPermissionsService(permissionsService);
        EconomySystem.economy= Economy.init(new PlatformAdapter(onlineModeServer));
        playerJoinEvent.register(economy.getPlayerJoinListener());
        playerExitEvent.register(economy.getPlayerJoinListener());
        Commands.register();
        ClickInventoryEvent.register();
    }

    public static void stop(){
        Economy.shutdown();
    }

    public static boolean isStarted(){
        return EconomySystem.economy!=null;
    }

    public static DynastyEconomy getApi(){
        return EconomySystem.economy.getApi();
    }
}
