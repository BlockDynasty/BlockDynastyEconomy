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

package BlockDynasty.Economy.domain.services;

import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;

import java.util.List;
import java.util.UUID;

public interface ICurrencyService {
    void add(ICurrency currency);
    void add(List<ICurrency> currencyList);
    void remove(ICurrency currency);
    List<ICurrency> getCurrencies();
    ICurrency getCurrency(String name);
    ICurrency getCurrency(UUID uuid);
    ICurrency getDefaultCurrency();
    boolean currencyExist(String name);
    void updateDefaultCurrency();
    boolean existsDefaultCurrency();
    void syncCurrency(UUID uuid);
}
