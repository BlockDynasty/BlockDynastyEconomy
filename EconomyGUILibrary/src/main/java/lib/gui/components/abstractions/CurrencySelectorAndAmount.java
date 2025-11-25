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

package lib.gui.components.abstractions;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import lib.gui.components.IEntityGUI;
import lib.gui.components.*;
import lib.util.colors.ChatColor;
import lib.util.colors.Message;
import lib.util.materials.Materials;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class CurrencySelectorAndAmount extends PaginatedPanel<ICurrency> {
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final IEntityGUI player;
    private final ITextInput textInput;

    public CurrencySelectorAndAmount(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI parentGUI, ITextInput textInput) {
        super(Message.process("CurrencySelector.title"), 5,player,parentGUI,21);
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.player = player;
        this.textInput = textInput;
        showCurrencies();
    }

    public CurrencySelectorAndAmount(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI parentGUI, ICurrency exceptCurrency, ITextInput textInput) {
        this(player, searchCurrencyUseCase, parentGUI, textInput);
        showCurrenciesExcluding(exceptCurrency);
    }

    private void showCurrenciesExcluding(ICurrency exceptCurrency) {
        List<ICurrency> currencies = searchCurrencyUseCase.getCurrencies().stream()
                .filter(c -> !c.equals(exceptCurrency))
                .collect(Collectors.toList());

        //testing purposes
        //for (int i = 0; i < 45; i++) {
        //   currencies.add(new Currency(UUID.randomUUID(),"test","test"));
        //}

        showItemsPage(currencies);
    }

    private void showCurrencies() {
        List<ICurrency> currencies = searchCurrencyUseCase.getCurrencies();

        //testing purposes
        //for (int i = 0; i < 45; i++) {
        //    currencies.add(new Currency(UUID.randomUUID(),"test","test"));
        //}

        showItemsPage(currencies);
    }

    @Override
    protected IItemStack createItemFor(ICurrency currency) {
        String color = ChatColor.stringValueOf(currency.getColor());
        return createItem(RecipeItem.builder()
                .setMaterial(Materials.GOLD_INGOT)
                .setName(Message.process(Map.of("currency",color+currency.getSingular()),"CurrencySelector.button1.nameItem"))
                .setTexture(currency.getTexture())
                .setLore(Message.processLines(Map.of(
                        "singular",color + currency.getSingular(),
                        "plural",color + currency.getPlural(),
                        "transferable",(currency.isTransferable() ? "Yes" : "No"),
                        "exchangeRate",color+ currency.getExchangeRate()),"CurrencySelector.button1.lore"))
                .build());
    }

    @Override
    protected void functionLeftItemClick(ICurrency currency) {
        textInput.open(this,player,Message.process(Map.of("currency",currency.getSingular()),"CurrencySelector.button2.nameItem"),Message.process("CurrencySelector.button2.lore"), s->{
            try {
                BigDecimal amount = new BigDecimal(s);
                return execute(player, currency, amount);
            } catch (NumberFormatException e) {
                return "Invalid Format";
            }
        });
    }

    protected String execute(IEntityGUI sender, ICurrency currency, BigDecimal amount){return "execute not implement";};
}
