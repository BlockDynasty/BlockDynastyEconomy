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

package lib.gui.templates.administrators.subMenus.accounts;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.ISetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.components.IGUI;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.generics.CurrencySelectorAndAmount;
import util.colors.ChatColor;
import util.colors.Colors;

import java.math.BigDecimal;

public class SetBalancePanel extends CurrencySelectorAndAmount {
    private final ISetBalanceUseCase setBalanceUseCase;

    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;

    public SetBalancePanel(IEntityGUI player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                           SearchCurrencyUseCase searchCurrencyUseCase, ISetBalanceUseCase setBalanceUseCase, IGUI parentGUI, ITextInput textInput) {
        super( player, searchCurrencyUseCase, parentGUI, textInput);
        this.targetPlayer = targetPlayer;
        this.setBalanceUseCase = setBalanceUseCase;
        //this.messageService = BlockDynastyEconomy.getInstance().getMessageService();
    }

    @Override
    public String execute(IEntityGUI sender, ICurrency currency, BigDecimal amount) {
        Result<Void> result = setBalanceUseCase.execute(targetPlayer, currency.getSingular(), amount, Context.COMMAND);
        if (result.isSuccess()) {
            sender.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+" Set success");
            this.openParent();
            return null;
        } else {
            return result.getErrorMessage();
        }
    }
}
