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

package lib.gui.templates.users.Exchange;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IExchangeUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.util.materials.Materials;
import lib.gui.components.abstractions.CurrencySelectorAndAmount;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.util.colors.Message;

import java.math.BigDecimal;
import java.util.Map;

public class ExchangeSecondPanel extends CurrencySelectorAndAmount {
    private final Currency currencyFrom;
    private final IExchangeUseCase exchangeUseCase;

    public ExchangeSecondPanel(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, IExchangeUseCase exchangeUseCase,
                               Currency currencyFrom, ExchangeFirstPanel parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI,currencyFrom, textInput);
        this.currencyFrom = currencyFrom;
        this.exchangeUseCase = exchangeUseCase;
    }

    @Override
    protected String execute(IEntityGUI sender, Currency currencyTo, BigDecimal amountTo){
        Result<BigDecimal> result= exchangeUseCase.execute(sender.getUniqueId(),currencyFrom.getSingular(),currencyTo.getSingular(),null, amountTo);
        if(result.isSuccess()){
            return "success";
        }else {
            return result.getErrorMessage();
        }
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)),"Exchange.button1.nameItem"),
                Message.processLines(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)),"Exchange.button1.lore")),
                null);
    }
}