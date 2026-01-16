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

package lib.gui.templates.administrators.subMenus.currencies;

import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import lib.gui.components.IGUI;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.generics.CurrencySelectorAndAmount;

public class CurrencyListDelete extends CurrencySelectorAndAmount {
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;
    private final IEntityGUI player;
    private final ITextInput textInput;

    public CurrencyListDelete(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase,
                              DeleteCurrencyUseCase deleteCurrencyUseCase, IGUI parent, ITextInput textInput)  {
        super( player, searchCurrencyUseCase,parent,textInput);
        this.deleteCurrencyUseCase =deleteCurrencyUseCase;
        this.player = player;
        this.textInput = textInput;

    }

    @Override
    public void functionLeftItemClick(ICurrency currency) {
        openAnvilConfirmation(currency);
    }

    public void openAnvilConfirmation(ICurrency currency) {
        textInput.open(this, player, "Confirm Deletion", "Type 'delete' to confirm", input -> {
            if ("delete".equalsIgnoreCase(input)) {
                return execute(currency);
            } else {
                //player.sendMessage(Message.getPrefix() + "§cDeletion cancelled. Type 'delete' to confirm.");
                this.open();
                return "Deletion cancelled.";
            }
        });
    }

    private String execute(ICurrency currency){
        try {
            deleteCurrencyUseCase.deleteCurrency(currency.getSingular());
            //player.sendMessage(Message.getPrefix() + "§7Deleted currency: §a" + currency.getSingular());
            this.openParent();
            return "Currency deleted successfully.";
        } catch (CurrencyNotFoundException e) {
            //player.sendMessage(Message.getPrefix()+"§7"+ e.getMessage()+" Make sure you have another default currency before deleting it.");
            return e.getMessage();
        } catch (TransactionException e) {
            //player.sendMessage(Message.getPrefix() + "§cError while deleting currency: §4" + e.getMessage());
            return e.getMessage();
        }
    }
}