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
import BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IWithdrawUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.components.IGUI;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.abstractions.CurrencySelectorAndAmount;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

import java.math.BigDecimal;

public class WithdrawPanel extends CurrencySelectorAndAmount {
    private final IWithdrawUseCase withdrawUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;

    public WithdrawPanel(IEntityGUI player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                         SearchCurrencyUseCase searchCurrencyUseCase, IWithdrawUseCase withdrawUseCase,
                         IGUI parentGUI, ITextInput textInput) {
        super( player, searchCurrencyUseCase, parentGUI,textInput);
        this.targetPlayer = targetPlayer;
        this.withdrawUseCase = withdrawUseCase;
        //this.messageService = BlockDynastyEconomy.getInstance().getMessageService();
    }

    @Override
    public String execute(IEntityGUI sender, ICurrency currency, BigDecimal amount) {
        Result<Void> result = withdrawUseCase.execute(targetPlayer.getUuid(), currency.getSingular(), amount, Context.COMMAND);
        if (result.isSuccess()) {
            sender.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Success withdraw " + ChatColor.stringValueOf(currency.getColor()) + currency.format(amount) + ChatColor.stringValueOf(Colors.GRAY)+" from " + targetPlayer.getNickname() + "'s account.");
            this.openParent();
            return null;
        } else {
            return result.getErrorMessage();
        }
    }
}