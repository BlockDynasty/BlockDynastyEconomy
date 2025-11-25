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
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyAlreadyExist;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.ICurrencyService;

import java.util.UUID;

//TODO :CREATE CURRENCY, NOTA IMPORTANTE, VALIDAR QUE SI YA EXISTE UNA CURRENCY POR DEFAULT NO SE PUEDA CREAR, Y EN EL CASO DE QUERER SETEAR UNA POR DEFECTO DEBEMOS DESASER LA ANTERIOR DEFAULT
public class CreateCurrencyUseCase {
    private final ICurrencyService currencyService;
    private final IRepository dataStore;
    private final Courier updateForwarder;
    private final IAccountService accountService;

    public CreateCurrencyUseCase(ICurrencyService currencyService, IAccountService accountService, Courier updateForwarder, IRepository dataStore) {
        this.currencyService = currencyService;
        this.accountService = accountService;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
    }

    public void execute(String singular, String plural){
        if (currencyService.currencyExist(singular) || currencyService.currencyExist(plural)){
            throw new CurrencyAlreadyExist("Currency already exist");
        }
        ICurrency currency = Currency.builder().setSingular(singular).setPlural(plural).build();
        currency.setExchangeRate(1.0);
        if(currencyService.getCurrencies().isEmpty()) {  //setear por defecto si es la unica moneda en el sistema
            currency.setDefaultCurrency(true);
        }
        try {
            dataStore.saveCurrency(currency);
            currencyService.add(currency);//cache
            accountService.syncDbWithOnlineAccounts();
            if (updateForwarder != null){
                updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
            }
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }
}
