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
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.ICurrencyService;

import java.util.ArrayList;
import java.util.List;

public class SearchCurrencyUseCase {
    private final ICurrencyService currencyService;
    private final IRepository datastore;

    public SearchCurrencyUseCase(ICurrencyService currencyService, IRepository datastore) {
        this.currencyService = currencyService;
        this.datastore = datastore;
    }

    public Result<ICurrency> getCurrency(String name) {
        ICurrency currency = currencyService.getCurrency(name);
        if (currency == null) {
            Result<ICurrency> result = datastore.loadCurrencyByName(name);
            if(result.isSuccess()){
                currency = result.getValue();
            }else {
                return Result.failure("Currency not found", ErrorCode.CURRENCY_NOT_FOUND);
            }
        }
        return Result.success(Currency.builder().copy(currency).build());
    }

    public Result<ICurrency>  getDefaultCurrency() {
        ICurrency defaultCurrency = currencyService.getDefaultCurrency();
        if(defaultCurrency == null){
            Result<ICurrency> result = datastore.loadDefaultCurrency();
            if(result.isSuccess()){
                defaultCurrency = result.getValue();
            }else {
                return Result.failure("Currency not found", ErrorCode.CURRENCY_NOT_FOUND);
            }
        }
        return Result.success(Currency.builder().copy(defaultCurrency).build());
    }

    public List<ICurrency> getCurrencies(){
        return new ArrayList<>(currencyService.getCurrencies());
    }
}
