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

package lib.gui.templates.users.Offers;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.gui.components.IGUI;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.Materials;
import lib.gui.components.abstractions.CurrencySelectorAndAmount;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.util.colors.Message;

import java.math.BigDecimal;
import java.util.Map;

public class CreateOfferFirstPanel extends CurrencySelectorAndAmount {
    private final BlockDynasty.Economy.domain.entities.account.Player target;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    protected final CreateOfferUseCase createOfferUseCase;
    private Currency currency;
    private BigDecimal amount;
    private final ITextInput textInput;

    public CreateOfferFirstPanel(IEntityGUI player, BlockDynasty.Economy.domain.entities.account.Player target,
                                 SearchCurrencyUseCase searchCurrencyUseCase, CreateOfferUseCase createOfferUseCase, IGUI parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI, textInput);
        this.target = target;
        this.textInput = textInput;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.createOfferUseCase = createOfferUseCase;
    }

    protected Currency getCurrency() {
        return currency;
    }

    protected BigDecimal getAmount() {
        return amount;
    }

    @Override
    protected String execute(IEntityGUI sender, Currency currency, java.math.BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
        new CreateOfferSecondPanel(sender, target, searchCurrencyUseCase,createOfferUseCase, this,textInput).open();
        return null;
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)),"OfferListPlayer.button2.nameItem"),
                        Message.processLines(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)),"OfferListPlayer.button2.lore")),
                null);

    }
}