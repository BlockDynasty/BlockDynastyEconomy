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
import lib.gui.components.IEntityGUI;
import lib.gui.components.*;
import lib.util.colors.ChatColor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CurrencySelectorAndAmount extends PaginatedPanel<Currency> {
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final IEntityGUI player;
    private final ITextInput textInput;

    public CurrencySelectorAndAmount(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI parentGUI, ITextInput textInput) {
        super("Currency List", 5,player,parentGUI,21);
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.player = player;
        this.textInput = textInput;
        showCurrencies();
    }

    public CurrencySelectorAndAmount(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI parentGUI, Currency exceptCurrency, ITextInput textInput) {
        this(player, searchCurrencyUseCase, parentGUI, textInput);
        showCurrenciesExcluding(exceptCurrency);
    }

    private void showCurrenciesExcluding(Currency exceptCurrency) {
        List<Currency> currencies = searchCurrencyUseCase.getCurrencies().stream()
                .filter(c -> !c.equals(exceptCurrency))
                .collect(Collectors.toList());

        //testing purposes
        //for (int i = 0; i < 45; i++) {
        //   currencies.add(new Currency(UUID.randomUUID(),"test","test"));
        //}

        showItemsPage(currencies);
    }

    private void showCurrencies() {
        List<Currency> currencies = searchCurrencyUseCase.getCurrencies();

        //testing purposes
        //for (int i = 0; i < 45; i++) {
        //    currencies.add(new Currency(UUID.randomUUID(),"test","test"));
        //}

        showItemsPage(currencies);
    }

    @Override
    protected IItemStack createItemFor(Currency currency) {
        String color = ChatColor.stringValueOf(currency.getColor());
        return createItem(Materials.GOLD_INGOT,
                color + currency.getSingular(),
                "Singular: " + color + currency.getSingular(),
                "Plural: " + color + currency.getPlural(),
                "Transferable: " + (currency.isTransferable() ? "Yes" : "No"),
                "Exchange Rate: "+ color+ currency.getExchangeRate()
        );
    }

    @Override
    protected void functionLeftItemClick(Currency currency) {
        textInput.open(this,player,"Amount to "+currency.getSingular(),"0", s->{
            try {
                BigDecimal amount = new BigDecimal(s);
                return execute(player, currency, amount);
            } catch (NumberFormatException e) {
                return "Invalid Format";
            }
        });
    }

    protected String execute(IEntityGUI sender, Currency currency, BigDecimal amount){return "execute not implement";};
}
