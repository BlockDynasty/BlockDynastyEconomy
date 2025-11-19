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

package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.components.IGUI;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.util.materials.Materials;
import lib.gui.components.abstractions.CurrencySelectorAndAmount;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.util.colors.Message;

import java.math.BigDecimal;
import java.util.Map;

public class CurrencyListToPay extends CurrencySelectorAndAmount {
    private final PayUseCase payUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;

    public CurrencyListToPay(IEntityGUI player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                             SearchCurrencyUseCase searchCurrencyUseCase, PayUseCase payUseCase, IGUI parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI,textInput);
        this.payUseCase = payUseCase;
        this.targetPlayer = targetPlayer;
    }

    @Override
    public String execute(IEntityGUI sender, Currency currency, BigDecimal amount){
        Result<Void> result = payUseCase.execute(sender.getUniqueId(), targetPlayer.getUuid(), currency.getSingular(), amount);
        if (!result.isSuccess()) {
            return result.getErrorMessage();
        }else{
            return Message.process("CurrencyListToPay.response");
        }
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER,
                        Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)),"CurrencyListToPay.button1.nameItem"),
                        Message.processLines(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)),"CurrencyListToPay.button1.lore")),
                null);

    }
}