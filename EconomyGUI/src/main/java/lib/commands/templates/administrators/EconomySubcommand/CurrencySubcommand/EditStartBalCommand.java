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

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.DecimalNotSupportedException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class EditStartBalCommand extends AbstractCommand {
    private final EditCurrencyUseCase editCurrencyUseCase;

    public EditStartBalCommand(EditCurrencyUseCase editCurrencyUseCase){
        super("startBal", "",List.of("currency","amount"));
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException ex) {
            sender.sendMessage("Invalid amount");
            return false;
        }

        String currencyName = args[0];
        double finalAmount = amount;

        try {
            editCurrencyUseCase.editStartBal(currencyName, finalAmount);
            sender.sendMessage("Starting balance for " + currencyName + " set: " + finalAmount);
        } catch (CurrencyNotFoundException e) {
            sender.sendMessage("unknown currency");
        } catch (TransactionException e) {
            sender.sendMessage( "Error: " + e.getMessage());
        } catch (DecimalNotSupportedException e) {
            sender.sendMessage("No decimal support for " + currencyName);
        }

        return true;
    }
}
