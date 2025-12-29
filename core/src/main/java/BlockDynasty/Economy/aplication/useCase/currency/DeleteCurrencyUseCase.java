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
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.courier.Message;

import java.util.List;

public class DeleteCurrencyUseCase {
    private final ICurrencyService currencyService;
    private final IAccountService accountService;
    private final IRepository dataStore;
    private final Courier updateForwarder;

    //borrar una moneda del sistema implica que se tengan que borrar todos los balances de las cuentas que tengan esa moneda
    public DeleteCurrencyUseCase(ICurrencyService currencyService, IAccountService accountService, IRepository dataStore, Courier updateForwarder){
        this.currencyService = currencyService;
        this.accountService = accountService;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
    }

    public void deleteCurrency(String currencyName){
        ICurrency currency = currencyService.getCurrency(currencyName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        if (currency.isDefaultCurrency()){
            throw new CurrencyNotFoundException(currency.getSingular()+" is default");
        }
        try {
            List<ICurrency> allCurrencies = currencyService.getCurrencies();
            for (ICurrency curr : allCurrencies){
                if (curr.isInterchangeableWith(currency)){
                    curr.removeInterchangeableCurrency(currency);
                    dataStore.saveCurrency(curr);
                }
            }

            dataStore.deleteCurrency(currency);
            currencyService.remove(currency);
            accountService.syncDbWithOnlineAccounts();
            if (updateForwarder != null){
                updateForwarder.sendUpdateMessage(Message.builder()
                        .setType(Message.Type.CURRENCY)
                        .setTarget(currency.getUuid())
                        .build());
            }
        }catch (TransactionException e){
            throw new TransactionException("Error deleting currency");
        }
    }
}
