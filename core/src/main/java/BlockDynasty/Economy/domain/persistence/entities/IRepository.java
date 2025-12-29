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

package BlockDynasty.Economy.domain.persistence.entities;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.persistence.transaction.ITransactions;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;

import java.util.List;
import java.util.UUID;

public interface IRepository extends ITransactions {

    List<ICurrency> loadCurrencies();
    Result<ICurrency> loadCurrencyByName(String name);
    Result<ICurrency> loadCurrencyByUuid(String uuid);
    Result<ICurrency> loadDefaultCurrency();
    void saveCurrency(ICurrency currency);
    void deleteCurrency(ICurrency currency);

    List<Account> loadAccounts();
    Result<Account> loadAccountByUuid(UUID uuid);
    Result<Account> loadAccountByName(String name);
    Result<Account> loadAccountByPlayer(Player player);
    void createAccount(Account account);
    void saveAccount(Player player, Account account);
    void saveAccount(Account account);
    Result<Void> deleteAccount(Player account);

    List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset);

    boolean isTopSupported();
    String getName();
    void close();
    void clearAll();

}