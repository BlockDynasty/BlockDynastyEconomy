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
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.IEntityCommands;
import util.colors.ChatColor;
import services.messages.MessageService;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
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

    private String handleBalancePlaceholder(String placeholder, Account account, ICurrency defaultCurrency) {
        if (placeholder.equals("balance_default")) {
            return String.valueOf(Math.round(account.getMoney(defaultCurrency).getAmount().doubleValue()));
        }

        if (placeholder.equals("balance_default_formatted")) {
            return defaultCurrency.format(account.getMoney(defaultCurrency).getAmount());
        }

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
        BigDecimal amountBD = account.getMoney(currency).getAmount();
        double amount = amountBD.doubleValue();

        Locale locale = Locale.US; // default
        boolean hasLocale = false;

        if (parts.length >= 3) {
            String possibleLocale = parts[parts.length - 1];
            if (isValidLocale(possibleLocale)) {
                locale = translateLocale(possibleLocale);
                hasLocale = true;
            }
        }

        if (placeholder.startsWith("balance_" + currencyName + "_formatted")) {
            if (hasLocale) {
                NumberFormat formatter = NumberFormat.getNumberInstance(locale);
                if (!currency.isDecimalSupported()) {
                    formatter.setMaximumFractionDigits(0);
                    formatter.setMinimumFractionDigits(0);
                } else {
                    formatter.setMaximumFractionDigits(2);
                    formatter.setMinimumFractionDigits(2);
                }
                return ChatColor.formatColorToPlaceholder(currency.getColor()) + formatter.format(amount);
            }
            return ChatColor.formatColorToPlaceholder(currency.getColor()) + currency.format(amountBD);
        } else {
            if (!currency.isDecimalSupported()) {
                int roundedAmount = (int) Math.floor(amount);
                if (hasLocale) {
                    NumberFormat formatter = NumberFormat.getNumberInstance(locale);
                    formatter.setMaximumFractionDigits(0);
                    formatter.setMinimumFractionDigits(0);
                    return formatter.format(roundedAmount);
                }
                return String.valueOf(roundedAmount);
            } else {
                if (hasLocale) {
                    NumberFormat formatter = NumberFormat.getNumberInstance(locale);
                    formatter.setMaximumFractionDigits(2);
                    formatter.setMinimumFractionDigits(2);
                    return formatter.format(amount);
                }
                return String.valueOf(amount);
            }
        }
    }

    private boolean isValidLocale(String localeString) {
        String lower = localeString.toLowerCase();
        return lower.equals("english") || lower.equals("en") ||
                lower.equals("french") || lower.equals("fr") ||
                lower.equals("german") || lower.equals("de") ||
                lower.equals("italian") || lower.equals("it") ||
                lower.equals("japanese") || lower.equals("ja") ||
                lower.equals("korean") || lower.equals("ko") ||
                lower.equals("chinese") || lower.equals("zh") ||
                lower.equals("simplified_chinese") || lower.equals("zh_cn") ||
                lower.equals("traditional_chinese") || lower.equals("zh_tw") ||
                lower.equals("france") || lower.equals("germany") ||
                lower.equals("italy") || lower.equals("japan") ||
                lower.equals("korea") || lower.equals("uk") ||
                lower.equals("us") || lower.equals("canada") ||
                lower.equals("canada_french");
    }

    private static Locale translateLocale(String localeString){
        switch (localeString.toLowerCase()){
            case "english":
            case "en":
                return Locale.ENGLISH;
            case "french":
            case "fr":
                return Locale.FRENCH;
            case "german":
            case "de":
                return Locale.GERMAN;
            case "italian":
            case "it":
                return Locale.ITALIAN;
            case "japanese":
            case "ja":
                return Locale.JAPANESE;
            case "korean":
            case "ko":
                return Locale.KOREAN;
            case "chinese":
            case "zh":
                return Locale.CHINESE;
            case "simplified_chinese":
            case "zh_cn":
                return Locale.SIMPLIFIED_CHINESE;
            case "traditional_chinese":
            case "zh_tw":
                return Locale.TRADITIONAL_CHINESE;
            case "france":
                return Locale.FRANCE;
            case "germany":
                return Locale.GERMANY;
            case "italy":
                return Locale.ITALY;
            case "japan":
                return Locale.JAPAN;
            case "korea":
                return Locale.KOREA;
            case "uk":
                return Locale.UK;
            case "canada":
                return Locale.CANADA;
            case "canada_french":
                return Locale.CANADA_FRENCH;
            case "us":
            default:
                return Locale.US;
        }
    }
}
