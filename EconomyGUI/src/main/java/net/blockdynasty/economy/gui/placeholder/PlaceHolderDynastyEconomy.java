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

package net.blockdynasty.economy.gui.placeholder;

import net.blockdynasty.economy.core.aplication.useCase.UseCaseFactory;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.gui.placeholder.components.BalanceItemsAndVirtualPlaceHolder;
import net.blockdynasty.economy.gui.placeholder.components.BalanceItemsPlaceHolder;
import net.blockdynasty.economy.gui.placeholder.components.BalancePlaceHolder;
import net.blockdynasty.economy.gui.placeholder.components.TopPlaceHolder;
import java.util.UUID;


public class PlaceHolderDynastyEconomy implements IPlaceHolderDynastyEconomy {
    private final UUID id;
    private final TopPlaceHolder topHandler;
    private final BalancePlaceHolder balanceHandler;
    private final BalanceItemsPlaceHolder balanceItemsHandler;
    private final BalanceItemsAndVirtualPlaceHolder balanceItemsAndVirtualHandler;

    public PlaceHolderDynastyEconomy(UseCaseFactory useCaseFactory, UUID id) {
        this.id = id;
        this.topHandler = new TopPlaceHolder(useCaseFactory.topAccounts());
        this.balanceHandler = new BalancePlaceHolder(useCaseFactory.getBalance());
        this.balanceItemsHandler = new BalanceItemsPlaceHolder();
        this.balanceItemsAndVirtualHandler = new BalanceItemsAndVirtualPlaceHolder(useCaseFactory.getBalance());
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public String onRequest(IEntityCommands player, String s) {
        if (player == null || s == null) return "";

        String placeholder = s.toLowerCase();

        if (placeholder.startsWith("top_")) {
            return topHandler.handle(placeholder);
        }

        if (placeholder.startsWith("balanceitemsandvirtual_")) {
            return balanceItemsAndVirtualHandler.handle(player.getUniqueId(), placeholder);
        }

        if (placeholder.startsWith("balanceitems_")) {
            return balanceItemsHandler.handle(player.getUniqueId(), placeholder);
        }

        if (placeholder.startsWith("balance_")) {
            return balanceHandler.handle(player.getUniqueId(), placeholder);
        }

        return null;
    }
}