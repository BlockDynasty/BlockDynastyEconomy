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

package lib.commands.templates.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import util.colors.ChatColor;
import util.colors.Colors;

import java.util.List;

public class ViewCommand extends AbstractCommand {
    private final SearchCurrencyUseCase searchCurrencyUseCase;

    public ViewCommand(SearchCurrencyUseCase searchCurrencyUseCase) {
        super("view","", List.of("currency"));
        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        Result<ICurrency> resultCurrency = searchCurrencyUseCase.getCurrency(args[0]);
        if (!resultCurrency.isSuccess()) {
            sender.sendMessage(resultCurrency.getErrorMessage()+ " "+resultCurrency.getErrorCode());
            return false;
        }

        ICurrency currency = resultCurrency.getValue();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.stringValueOf(Colors.GREEN)+"--- Currency Info ---")
                .append("\n")
                     .append(ChatColor.stringValueOf(Colors.GRAY)+"ID: "+ChatColor.stringValueOf(Colors.RED) + currency.getUuid().toString())
                .append("\n")
                     .append(ChatColor.stringValueOf(Colors.GRAY)+"Singular: "+ currency.getSingular() + ", Plural: " + currency.getPlural())
                .append("\n")
                     .append(ChatColor.stringValueOf(Colors.GRAY)+"Color: " + ChatColor.stringValueOf(currency.getColor()) +currency.getColor())
                .append("\n")
                     .append(ChatColor.stringValueOf(Colors.GRAY)+"Symbol: "+ currency.getSymbol())
                .append("\n")
                     .append(ChatColor.stringValueOf(Colors.GRAY)+"Start Balance: "  + currency.format(currency.getDefaultBalance()) )
                .append("\n")
                     .append(ChatColor.stringValueOf(Colors.GRAY)+"Decimals: " + (currency.isDecimalSupported() ? ChatColor.stringValueOf(Colors.GREEN)+"Yes" : ChatColor.stringValueOf(Colors.RED)+"No"))
                .append("\n")
                     .append(ChatColor.stringValueOf(Colors.GRAY)+"Default: " + (currency.isDefaultCurrency() ? ChatColor.stringValueOf(Colors.GREEN)+"Yes" : ChatColor.stringValueOf(Colors.RED)+"No"))
                .append("\n")
                     .append(ChatColor.stringValueOf(Colors.GRAY)+"Payable: " + (currency.isTransferable() ? ChatColor.stringValueOf(Colors.GREEN)+"Yes" : ChatColor.stringValueOf(Colors.RED)+"No"))
                .append("\n")
                     .append(ChatColor.stringValueOf(Colors.GRAY)+"Rate: "+ currency.getExchangeRate());

        sender.sendMessage(stringBuilder.toString());
        return true;
    }
}
