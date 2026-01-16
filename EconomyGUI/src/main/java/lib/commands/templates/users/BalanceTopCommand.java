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

import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetTopAccountsUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import util.colors.ChatColor;
import services.messages.MessageService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BalanceTopCommand extends AbstractCommand {
    private final GetTopAccountsUseCase getTopAccountsUseCase;

    public BalanceTopCommand(GetTopAccountsUseCase getTopAccountsUseCase) {
        super("baltop" ,"BlockDynastyEconomy.players.baltop", List.of("currency" ,"limit"));
        this.getTopAccountsUseCase = getTopAccountsUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String nameCurrency =  args[0];
        int limit = 10;
        if (args.length>1){
            try {
                limit = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("El segundo argumento debe ser un numero");
                return false;
            }
        }
        int finalLimit = limit;

        Result<List<Account>> resultAccounts = getTopAccountsUseCase.execute(nameCurrency, finalLimit,0);
        if (!resultAccounts.isSuccess()){
            sender.sendMessage(resultAccounts.getErrorMessage());

        }else{
            List<Account> accounts = resultAccounts.getValue();

            StringBuilder aux = new StringBuilder();
            aux.append("Top ").append(finalLimit).append(" ").append(nameCurrency).append(" : ").append("\n");
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                Money money = account.getMoney(nameCurrency);
                ICurrency currency = money.getCurrency();
                BigDecimal balanceValue = money.getAmount();
                String message = MessageService.getMessage("balance_top.balance",
                        Map.of(
                                "number", String.valueOf(i+1),
                                "currencycolor", ChatColor.stringValueOf(currency.getColor()),
                                "player",account.getNickname(),
                                "balance",currency.format(balanceValue))
                        );
                aux.append(message).append("\n");
            }
            sender.sendMessage(aux.toString());
        }

        return true;
    }

}
