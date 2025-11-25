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

package BlockDynasty.Economy.aplication.useCase.currency;

import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.DecimalNotSupportedException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.ICurrencyService;

import java.math.BigDecimal;

public class EditCurrencyUseCase {
    private final ICurrencyService currencyService;
    private final IRepository dataStore;
    private final Courier updateForwarder;

    public EditCurrencyUseCase(ICurrencyService currencyService, Courier updateForwarder, IRepository dataStore) {
        this.currencyService = currencyService;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
    }

    public void editStartBal(String name, double startBal){
        ICurrency currency = currencyService.getCurrency(name);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        if (!currency.isDecimalSupported() && startBal % 1 != 0) {
            throw new DecimalNotSupportedException("Currency does not support decimals");
        }
        currency.setDefaultBalance(BigDecimal.valueOf(startBal));
        try {
            dataStore.saveCurrency(currency);
            //actualizar cache no hace falta por que ya traje la referencia de la moneda de currencymanager
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }

    public void setCurrencyRate(String currencyName, double rate){
        if (rate <= -1) {
            throw new IllegalArgumentException("Exchange rate must be greater than -1");
        }
        ICurrency currency = currencyService.getCurrency(currencyName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setExchangeRate(rate);
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }

    public void addInterchangeableCurrency(String currencyName, String currencyToAddName){
        if (currencyName.equalsIgnoreCase(currencyToAddName)){
            throw new IllegalArgumentException("Cannot add the same currency as interchangeable");
        }
        ICurrency currency = currencyService.getCurrency(currencyName);
        ICurrency currencyToAdd = currencyService.getCurrency(currencyToAddName);
        if (currency == null || currencyToAdd == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.addInterchangeableCurrency(currencyToAdd);
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }

    public void removeInterchangeableCurrency(String currencyName, String currencyToRemoveName){
        ICurrency currency = currencyService.getCurrency(currencyName);
        ICurrency currencyToRemove = currencyService.getCurrency(currencyToRemoveName);
        if (currency == null || currencyToRemove == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.removeInterchangeableCurrency(currencyToRemove);
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }

    public void editColor(String nameCurrency, String colorString){
        ICurrency currency = currencyService.getCurrency(nameCurrency);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }

        currency.setColor(colorString);
        try {
            dataStore.saveCurrency(currency);
            //actualizar cache no hace falta por que ya traje la referencia de la moneda de currencymanager
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }

    public void editSymbol(String nameCurrency,String symbol){
        ICurrency currency = currencyService.getCurrency(nameCurrency);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setSymbol(symbol);
        try {
            dataStore.saveCurrency(currency);
            //actualizar cache no hace falta por que ya traje la referencia de la moneda de currencymanager
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }

    public void editTexture(String nameCurrency,String texture){
        ICurrency currency = currencyService.getCurrency(nameCurrency);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setTexture(texture);
        try {
            dataStore.saveCurrency(currency);
            //actualizar cache no hace falta por que ya traje la referencia de la moneda de currencymanager
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }

    public void setDefaultCurrency(String currencyName){
        ICurrency currency = currencyService.getCurrency(currencyName);
        if (currency.isDefaultCurrency()){
            return;
        }
        currencyService.getCurrencies().forEach(c -> {
            if (c.isDefaultCurrency()){
                c.setDefaultCurrency(false);
                try {
                    dataStore.saveCurrency(c);
                    updateForwarder.sendUpdateMessage("currency", c.getUuid().toString());
                }catch (TransactionException e){
                    throw new TransactionException("Error save in setDefaultCurrency");
                }
            }
        });
        currency.setDefaultCurrency(true);
        currencyService.updateDefaultCurrency();
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error saving currency");
        }
    }

    public void setSingularName(String actualName, String newName){
        //todo: cambiar nombre de la moneda, verificar si existe el actualname tanto plural como singualr, y actualizar el mismo campo plural o singular
        ICurrency currency = currencyService.getCurrency(actualName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setSingular(newName);
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error saving currency");
        }
    }

    public void setPluralName(String actualName, String newName){
        ICurrency currency = currencyService.getCurrency(actualName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setPlural(newName);
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error saving currency");
        }
    }

    public void togglePayable(String currencyName){
        ICurrency currency = currencyService.getCurrency(currencyName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setTransferable(!currency.isTransferable());
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }

    public void toggleDecimals(String currencyName){
        ICurrency currency = currencyService.getCurrency(currencyName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setDecimalSupported(!currency.isDecimalSupported());
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }
}
