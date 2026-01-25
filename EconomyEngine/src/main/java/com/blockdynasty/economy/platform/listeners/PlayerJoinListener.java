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

package com.blockdynasty.economy.platform.listeners;

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.domain.services.IAccountService;
import lib.commands.abstractions.IEntityCommands;
import services.Console;

public class PlayerJoinListener implements IPlayerJoin {
    private final LoadAccount loadAccount;
    private final IAccountService accountService;

    public PlayerJoinListener(UseCaseFactory useCaseFactory, IAccountService accountService,boolean configOnlineMode,boolean platformOnlineMode) {
        if(configOnlineMode){
            this.loadAccount = new PlayerJoinListenerOnline(useCaseFactory.searchAccountByUUID(), useCaseFactory.createAccount(), accountService);
            if(!platformOnlineMode){
                Console.logError("THE SERVER IS IN OFFLINE MODE but the plugin is configured to work in ONLINE mode, please change the configuration to avoid issues.");
            }
            Console.log("Online mode is enabled. The plugin will use UUID to identify players.");
        }else{
            this.loadAccount = new PlayerJoinListenerOffline(useCaseFactory.searchAccountByName(), useCaseFactory.createAccount(), accountService);
            if(platformOnlineMode){
                Console.logError("THE SERVER IS IN ONLINE MODE but the plugin is configured to work in OFFLINE mode, please change the configuration to avoid issues.");
            }
            Console.log("Online mode is disabled, The plugin will use NICKNAME to identify players.");
        }
        this.accountService = accountService;
    }

    @Override
    public void loadPlayerAccount(IEntityCommands player) {
        this.loadAccount.loadAccount(player);
    }

    @Override
    public void offLoadPlayerAccount(IEntityCommands player) {
        accountService.removeAccountOnline(player.getUniqueId());
    }
}
