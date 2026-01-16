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

package lib.commands.templates.users;

import BlockDynasty.Economy.aplication.useCase.transaction.balance.GetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import util.colors.ChatColor;
import services.messages.MessageService;

import java.util.List;
import java.util.Map;

public class BalanceCommand extends AbstractCommand {
    private final GetBalanceUseCase balance;

    public BalanceCommand(GetBalanceUseCase balance) {
        super("balance","BlockDynastyEconomy.players.balance");
        this.balance = balance;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        Result<List<Money>> resultBalances = balance.getBalances(sender.getName());
        if (!resultBalances.isSuccess()) {
            sender.sendMessage(resultBalances.getErrorMessage()+ " "+resultBalances.getErrorCode());
        }else{
            for (Money entry : resultBalances.getValue()) {
                ICurrency currency = entry.getCurrency();
                sender.sendMessage(MessageService.getMessage("balance.list", Map.of("currencycolor", ChatColor.stringValueOf(currency.getColor()),"format",entry.format()) ));
            }
        }
        return true;
    }
}
