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

package api;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.services.IAccountService;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IApi {

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the default currency.
     */
    EconomyResponse deposit(UUID uuid, BigDecimal amount);
    EconomyResponse deposit(String name, BigDecimal amount);

    ICurrency getDefaultCurrency();

    EventManager getEventManager();

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the default currency.
     */
    EconomyResponse withdraw(UUID uuid, BigDecimal amount);
    EconomyResponse withdraw(String name, BigDecimal amount);

    EconomyResponse setBalance(UUID uuid, BigDecimal amount);
    EconomyResponse setBalance(String name, BigDecimal amount, String currency);

    EconomyResponse setBalance(UUID uuid, BigDecimal amount, String currency);
    EconomyResponse setBalance(String name, BigDecimal amount);
    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of a currency
     * @param currency - A specified currency.
     */
    EconomyResponse deposit(UUID uuid, BigDecimal amount, String currency);
    EconomyResponse deposit(String name, BigDecimal amount, String currency);
    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the currency.
     * @param currency - The currency you withdraw from.
     */
    EconomyResponse withdraw(UUID uuid, BigDecimal amount, String currency);
    EconomyResponse withdraw(String name, BigDecimal amount, String currency);

    /**
     *
     * @param uuid - The users unique ID.
     * @return - The default currency balance of the user.
     */
    BigDecimal getBalance(UUID uuid);
    BigDecimal getBalance(String name);

    /**
     *
     * @param uuid - The users unique ID.
     * @param currency - An amount of the default currency.
     * @return - The balance of the specified currency.
     */
    BigDecimal getBalance(UUID uuid, String currency);
    BigDecimal getBalance(String name, String currency);

    /**
     *
     * @param uuid - The users unique ID.
     * @param currencyFrom - String name currencyFrom.
     * @param currencyTo - String name currencyTo.
     * @param amountFrom - double mount amountFrom.
     * @param amountTo - double mount ammountTo.
     */
    EconomyResponse exchange(UUID uuid, String currencyFrom, String currencyTo, BigDecimal amountFrom,BigDecimal amountTo);

    /**
     *
     * @param userFrom - The userFrom unique ID.
     * @param userTo - The userTO unique ID.
     * @param currency- String name currency.
     * @param amount - double mount amount.
     */
    EconomyResponse transfer(UUID userFrom, UUID userTo, String currency, BigDecimal amount);
    EconomyResponse transfer(String userFrom, String userTo, String currency, BigDecimal amount);

    /**
     *
     * @param userFrom - The userFrom unique ID.
     * @param userTo - The userTO unique ID.
     * @param currencyFrom- String name currency from.
     * @param amountFrom - double mount amount from.
     * @param currencyTo - String name currency to.
     * @param amountTo - double mount amount to.
     */
    EconomyResponse trade(UUID userFrom, UUID userTo, String currencyFrom, String currencyTo, BigDecimal amountFrom, BigDecimal amountTo);

    boolean hasAmount(UUID uuid, BigDecimal amount);
    boolean hasAmount(String name , BigDecimal amount);
    boolean hasAmount(UUID uuid, BigDecimal amount, String currency);
    boolean hasAmount(String name , BigDecimal amount, String currency);

    boolean existCurrency(String nameCurrency);
    boolean existAccount(UUID uuid);
    boolean existAccount(String name);

    String getDefaultCurrencyNamePlural();
    String getDefaultCurrencyNameSingular();

    String format(BigDecimal amount);
    String format(BigDecimal amount, String currency);

    List<String> getCurrenciesNamesList();
    List<ICurrency> getCurrencies();

    EconomyResponse createAccount(final UUID accountID, final String name);

    EconomyResponse deleteAccount(final UUID accountID);
    EconomyResponse deleteAccount(final String name);

    EconomyResponse blockAccountTransactions(final UUID accountID);
    EconomyResponse unblockAccountTransactions(final UUID accountID);

    EconomyResponse createCurrency(String plural,String singular);
    EconomyResponse deleteCurrency(String name);
    Account getAccount(UUID uuid);
    Account getAccount(String name);
    List<Account> getTopAccounts(int amount, String currency);

    EconomyResponse setCurrencyStartBalance(String name, BigDecimal startBal);
    EconomyResponse setCurrencyColor(String currencyName, String colorString);
    EconomyResponse setCurrencyRate(String currencyName, BigDecimal rate);
    EconomyResponse setCurrencyDecimalSupport(String currencyName, boolean supportDecimals);
    EconomyResponse setCurrencySymbol(String currencyName, String symbol);
    EconomyResponse setDefaultCurrency(String currencyName);
    EconomyResponse setSingularName(String currentName, String newName);
    EconomyResponse setPluralName(String currentName, String newName);
    EconomyResponse setPayable(String currencyName, boolean isPayable);

    ICurrency getCurrency(String name );

    IAccountService getAccountService();

}
