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

package lib.placeholder;

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetTopAccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.IEntityCommands;
import lib.util.colors.ChatColor;
import lib.messages.MessageService;

import java.util.List;
import java.util.Map;

public class PlaceHolder {
    private final GetTopAccountsUseCase getTopAccountsUseCase;
    private final GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;

    public PlaceHolder(UseCaseFactory useCaseFactory) {
        this.getAccountByUUIDUseCase = useCaseFactory.searchAccountByUUID();
        this.getTopAccountsUseCase = useCaseFactory.topAccounts();
        this.searchCurrencyUseCase = useCaseFactory.searchCurrency();
    }

    public String onRequest(IEntityCommands player, String s) {
        if (player == null || s == null) {
            return "";
        }

        // Normalizar la entrada del placeholder
        s = s.toLowerCase();

        // Placeholder para "top" (ejemplo: %blockdynastyeconomy_top_dinero_10%)
        if (s.startsWith("top_")) {
            return handleTopPlaceholder(s);
        }

        // Obtener la cuenta del jugador
        Result<Account> accountResult = getAccountByUUIDUseCase.execute(player.getUniqueId());
        if (!accountResult.isSuccess()) {
            return "Player data not found";
        }

        // Obtener la moneda predeterminada
        Result<ICurrency> defaultCurrencyResult = searchCurrencyUseCase.getDefaultCurrency();
        if (!defaultCurrencyResult.isSuccess()) {
            return "Default currency not found";
        }

        Account account = accountResult.getValue();
        ICurrency defaultCurrency = defaultCurrencyResult.getValue();

        // Placeholder para balance (ejemplo: %blockdynastyeconomy_balance_dinero%)
        if (s.startsWith("balance_")) {
            return handleBalancePlaceholder(s, account, defaultCurrency);
        }

        return null; // Placeholder desconocido
    }

    // Manejar el placeholder "top"
    //ejemplo de usos : %blockdynastyeconomy_top_dinero_10%
    // %blockdynastyeconomy_top_dinero_10_1%
    // %blockdynastyeconomy_top_dinero_10_2%
    // %blockdynastyeconomy_top_dinero_10_3%
    private String handleTopPlaceholder(String placeholder) {
        String[] parts = placeholder.split("_");
        if (parts.length < 3) {
            return "Invalid placeholder format";
        }

        String currencyName = parts[1];
        int limit;
        try {
            limit = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return "Invalid limit number";
        }

        // Determinar si se solicita una posición específica
        int position = -1; // -1 indica que no se solicitó una posición específica
        if (parts.length >= 4) {
            try {
                position = Integer.parseInt(parts[3]) - 1; // Ajustar a índice basado en 0
            } catch (NumberFormatException e) {
                return "Invalid position number";
            }
        }

        // Obtener las cuentas principales
        Result<List<Account>> result = getTopAccountsUseCase.execute(currencyName,limit,0);
        if (!result.isSuccess()) {
            return "No accounts found";
        }

        List<Account> accounts = result.getValue();


        if (position >= 0) {
            // Validar que la posición solicitada esté dentro del rango
            if (position >= accounts.size()) {
                return "Position out of range";
            }

            // Retornar solo la cuenta en la posición específica
            Account account = accounts.get(position);
            /*return (position + 1) + ". " + account.getNickname() + ": " +
                    account.getBalance(currencyName).getBalance() + " " + currencyName;*/

            Money money = account.getMoney(currencyName);
            //"&a&l-> {number}. &b{player} &7- {currencycolor}{balance}"
            return MessageService.getMessage("balance_top.balance",
                    Map.of(
                            "number", String.valueOf(position + 1),
                            "currencycolor", "" + ChatColor.formatColorToPlaceholder(money.getCurrency().getColor()),
                            "player", account.getNickname(),
                            "balance", money.format()
                    )
            );
        }

        // Construir el resultado completo si no se solicitó una posición específica
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            Money money = account.getMoney(currencyName);


            //"&a&l-> {number}. &b{player} &7- {currencycolor}{balance}"
            string.append(MessageService.getMessage("balance_top.balance",
                    Map.of(
                            "number", String.valueOf(i + 1),
                            "currencycolor", "" + ChatColor.formatColorToPlaceholder(money.getCurrency().getColor()),
                            "player", account.getNickname(),
                            "balance", money.format()
                    )
            ));

            /*result.append(i + 1).append(". ")
                    .append(account.getNickname()).append(": ")
                    //.append(balance.getBalance()).append(" ").append(balance.getCurrency().format());
                    .append(balance.getCurrency().format(balance.getBalance()));
                    //.append(account.getBalance(currencyName).getBalance()).append(" ").append(currencyName);*/
            if (i < accounts.size() - 1) {
                string.append("\n");
            }
        }
        return result.toString();
    }

    // Manejar el placeholder "balance"
    //ejemplo de usos : %blockdynastyeconomy_balance_dinero%
    // %blockdynastyeconomy_balance_dinero_formatted%
    private String handleBalancePlaceholder(String placeholder, Account account, ICurrency defaultCurrency) {
        if (placeholder.equals("balance_default")) {
            return String.valueOf(Math.round(account.getMoney(defaultCurrency).getAmount().doubleValue()));
        }

        if (placeholder.equals("balance_default_formatted")) {
            return defaultCurrency.format(account.getMoney(defaultCurrency).getAmount());
        }

        // Manejar balances de otras monedas (ejemplo: %blockdynastyeconomy_balance_dinero%)
        String[] parts = placeholder.split("_");
        if (parts.length < 2) {
            return "Invalid placeholder format";
        }

        String currencyName = parts[1];

        Result<ICurrency> result = searchCurrencyUseCase.getCurrency(currencyName);
        if (!result.isSuccess()) {
            return "Currency not found";
        }
        ICurrency currency = result.getValue();

        if (placeholder.equals("balance_" + currencyName + "_formatted")) {  //todo, permit use _symbol for formated with symbol
            return ChatColor.formatColorToPlaceholder(currency.getColor()) + currency.format(account.getMoney(currency).getAmount());//%BlockDynastyEconomy_balance_Dinero_formatted%
        } else {
            return String.valueOf(account.getMoney(currency).getAmount().doubleValue());   //%BlockDynastyEconomy_balance_Dinero%
        }
    }
}
