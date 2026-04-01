/**
 * Copyright 2026 Federico Barrionuevo "@federkone"
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

package net.blockdynasty.economy.engine.apiImplement;

import net.blockdynasty.economy.core.aplication.useCase.UseCaseFactory;
import net.blockdynasty.economy.core.aplication.useCase.account.CreateAccountUseCase;
import net.blockdynasty.economy.core.aplication.useCase.account.DeleteAccountUseCase;
import net.blockdynasty.economy.core.aplication.useCase.account.EditAccountUseCase;
import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetOfflineAccountsUseCase;
import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetTopAccountsUseCase;
import net.blockdynasty.economy.core.aplication.useCase.currency.CreateCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.currency.DeleteCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.currency.EditCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.balance.GetBalanceUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.*;
import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.core.domain.services.log.Log;
import net.blockdynasty.economy.api.DynastyEconomy;
import net.blockdynasty.economy.api.EconomyResponse;
import net.blockdynasty.economy.api.entity.Account;
import net.blockdynasty.economy.api.entity.Currency;
import net.blockdynasty.economy.api.entity.Wallet;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

class DynastyEconomyApi implements DynastyEconomy {
    private UUID id;
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private GetBalanceUseCase getBalanceUseCase;
    private CreateAccountUseCase createAccountUseCase;
    private GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    private GetAccountByNameUseCase getAccountByNameUseCase;
    private GetOfflineAccountsUseCase getOfflineAccountsUseCase;
    private IWithdrawUseCase withdrawUseCase;
    private IDepositUseCase depositUseCase;
    private ISetBalanceUseCase setBalanceUseCase;
    private ITradeUseCase tradeCurrenciesUseCase;
    private ITransferUseCase transferFundsUseCase;
    private IExchangeUseCase exchangeUseCase;
    private DeleteAccountUseCase deleteAccountUseCase;
    private EditAccountUseCase editAccountUseCase;
    private CreateCurrencyUseCase createCurrencyUseCase;
    private DeleteCurrencyUseCase deleteCurrencyUseCase;
    private GetTopAccountsUseCase topAccounts;
    private EditCurrencyUseCase editCurrencyUseCase;

    public DynastyEconomyApi(UseCaseFactory factory,UUID id) {
        this.id = id;
        editCurrencyUseCase = factory.editCurrency();
        searchCurrencyUseCase = factory.searchCurrency();
        getBalanceUseCase = factory.getBalance();
        createAccountUseCase = factory.createAccount();
        getAccountByNameUseCase = factory.searchAccountByName();
        getAccountByUUIDUseCase = factory.searchAccountByUUID();
        getOfflineAccountsUseCase = factory.searchOfflineAccounts();
        deleteAccountUseCase = factory.deleteAccount();
        editAccountUseCase = factory.editAccount();
        createCurrencyUseCase = factory.createCurrency();
        deleteCurrencyUseCase = factory.deleteCurrency();
        topAccounts = factory.topAccounts();

        withdrawUseCase = factory.withdraw();
        depositUseCase = factory.deposit();
        setBalanceUseCase = factory.setBalance();
        tradeCurrenciesUseCase = factory.tradeCurrencies();
        transferFundsUseCase = factory.transferFunds();
        exchangeUseCase = factory.exchange();
    }
    public DynastyEconomyApi(UseCaseFactory factory, Log log,UUID id) {
        this.id = id;
        editCurrencyUseCase = factory.editCurrency();
        searchCurrencyUseCase = factory.searchCurrency();
        getBalanceUseCase = factory.getBalance();
        createAccountUseCase = factory.createAccount();
        getAccountByNameUseCase = factory.searchAccountByName();
        getAccountByUUIDUseCase = factory.searchAccountByUUID();
        getOfflineAccountsUseCase = factory.searchOfflineAccounts();
        deleteAccountUseCase = factory.deleteAccount();
        editAccountUseCase = factory.editAccount();
        createCurrencyUseCase = factory.createCurrency();
        deleteCurrencyUseCase = factory.deleteCurrency();
        topAccounts = factory.topAccounts();

        withdrawUseCase = factory.withdraw(log);
        depositUseCase = factory.deposit(log);
        setBalanceUseCase = factory.setBalance(log);
        tradeCurrenciesUseCase = factory.tradeCurrencies(log);
        transferFundsUseCase = factory.transferFunds(log);
        exchangeUseCase = factory.exchange(log);
    }

    @Override
    public UUID getId() {
        return id;
    }

    private EconomyResponse handleResult(Result<Void> result){
        if (result.isSuccess()) {
            return EconomyResponse.success();
        } else {
            return EconomyResponse.failure(result.getErrorMessage());
        }
    }

    public EconomyResponse deposit(UUID uuid, BigDecimal amount){
        return handleResult(this.depositUseCase.execute(uuid, amount));
    }

    @Override
    public EconomyResponse deposit(String name, BigDecimal amount) {
        return handleResult(this.depositUseCase.execute(name, amount));
    }

    @Override
    public Currency getDefaultCurrency() {
        //format core currency to api currency
        ICurrency currency =this.searchCurrencyUseCase.getDefaultCurrency().getValue();
        return new Currency(
                currency.getUuid(),
                currency.getPlural(),
                currency.getSingular(),
                currency.getSymbol(),
                currency.getColor(),
                currency.getDefaultBalance(),
                currency.getExchangeRate(),
                currency.isDefaultCurrency()
        );
    }

    @Override
    public List<Account> getAccountsOffline() {
        Result<List<net.blockdynasty.economy.core.domain.entities.account.Account>> result = this.getOfflineAccountsUseCase.execute();
        if (!result.isSuccess()) {
            return List.of();
        }
        List<net.blockdynasty.economy.core.domain.entities.account.Account> accounts = result.getValue();
        return accounts.stream().map(coreA ->{
            List<net.blockdynasty.economy.api.entity.Money> moneyList =coreA.getWallet().getBalances().stream()
                    .map(coreMoney ->{
                        return new net.blockdynasty.economy.api.entity.Money(
                                new Currency(
                                        coreMoney.getCurrency().getUuid(),
                                        coreMoney.getCurrency().getPlural(),
                                        coreMoney.getCurrency().getSingular(),
                                        coreMoney.getCurrency().getSymbol(),
                                        coreMoney.getCurrency().getColor(),
                                        coreMoney.getCurrency().getDefaultBalance(),
                                        coreMoney.getCurrency().getExchangeRate(),
                                        coreMoney.getCurrency().isDefaultCurrency()

                                ),coreMoney.getAmount()
                        );
                    }).collect(Collectors.toList());
            return new Account(
                    coreA.getNickname(),
                    coreA.getUuid(),
                    new Wallet(moneyList),
                    coreA.canReceiveCurrency(),
                    coreA.isBlocked()
            );
        }).collect(Collectors.toList());
    }

    public EconomyResponse deposit(UUID uuid, BigDecimal amount, String currency){
        return handleResult(this.depositUseCase.execute(uuid,currency, amount));
    }

    @Override
    public EconomyResponse deposit(String name, BigDecimal amount, String currency) {
        return handleResult(this.depositUseCase.execute(name,currency, amount));
    }

    public EconomyResponse withdraw(UUID uuid, BigDecimal amount){
        return handleResult(this.withdrawUseCase.execute(uuid,  amount));
    }

    @Override
    public EconomyResponse withdraw(String name, BigDecimal amount) {
        return handleResult( this.withdrawUseCase.execute(name,  amount));
    }

    @Override
    public EconomyResponse setBalance(UUID uuid, BigDecimal amount) {
        return handleResult(this.setBalanceUseCase.execute(uuid,getDefaultCurrencyNamePlural(), amount));
    }

    @Override
    public EconomyResponse setBalance(String name, BigDecimal amount, String currency) {
        return handleResult(this.setBalanceUseCase.execute(name,currency, amount));
    }

    @Override
    public EconomyResponse setBalance(UUID uuid, BigDecimal amount, String currency) {
        return handleResult( this.setBalanceUseCase.execute(uuid,currency, amount));
    }

    @Override
    public EconomyResponse setBalance(String name, BigDecimal amount) {
        return handleResult(this.setBalanceUseCase.execute(name,getDefaultCurrencyNamePlural(), amount));
    }

    public EconomyResponse withdraw(UUID uuid, BigDecimal amount, String currency){
        return handleResult(this.withdrawUseCase.execute(uuid,currency, amount));
    }

    @Override
    public EconomyResponse withdraw(String name, BigDecimal amount, String currency) {
        return handleResult(this.withdrawUseCase.execute(name,currency, amount));
    }

    public BigDecimal getBalance(UUID uuid){
        Result<Money> balanceResult =  this.getBalanceUseCase.execute(uuid);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve default balance: " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().getAmount();
    }

    @Override
    public BigDecimal getBalance(String name) {
        Result<Money> balanceResult =  this.getBalanceUseCase.execute(name);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve default balance: " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().getAmount();
    }

    public BigDecimal getBalance(UUID uuid, String currency) {
        Result<Money> balanceResult =  this.getBalanceUseCase.execute(uuid, currency);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve balance for currency '" + currency + "': " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().getAmount();
    }

    @Override
    public BigDecimal getBalance(String name, String currency) {
        Result<Money> balanceResult =  this.getBalanceUseCase.execute(name, currency);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve balance for currency '" + currency + "': " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().getAmount();
    }

    public EconomyResponse exchange(UUID uuid, String currencyFrom, String currencyTo, BigDecimal amountFrom,BigDecimal amountTo){
        Result<BigDecimal> result =  this.exchangeUseCase.execute(uuid,currencyFrom,currencyTo,amountFrom, amountTo);
        if (result.isSuccess()) {
            return EconomyResponse.success();
        } else {
            return EconomyResponse.failure(result.getErrorMessage());
        }
    }

    public EconomyResponse transfer(UUID userFrom, UUID userTo, String currency, BigDecimal amount){
        return handleResult(this.transferFundsUseCase.execute(userFrom,userTo,currency,amount));
    }

    @Override
    public EconomyResponse transfer(String userFrom, String userTo, String currency, BigDecimal amount) {
        return handleResult(this.transferFundsUseCase.execute(userFrom,userTo,currency,amount));
    }

    public EconomyResponse trade(UUID userFrom, UUID userTo, String currencyFrom, String currencyTo, BigDecimal amountFrom, BigDecimal amountTo){
        return handleResult(this.tradeCurrenciesUseCase.execute(userFrom,userTo,currencyFrom,currencyTo,amountFrom,amountTo));
    }

    @Override
    public boolean hasAmount(UUID uuid, BigDecimal amount) {
        Result<Money> balanceResult =  this.getBalanceUseCase.execute(uuid);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve balance: " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().hasEnough(amount);
    }

    @Override
    public boolean hasAmount(String name, BigDecimal amount) {
        Result<Money> balanceResult =  this.getBalanceUseCase.execute(name);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve balance: " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().hasEnough(amount);
    }

    @Override
    public boolean hasAmount(UUID uuid, BigDecimal amount, String currency) {
        Result<Money> balanceResult =  this.getBalanceUseCase.execute(uuid , currency);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve balance: " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().hasEnough(amount);
    }

    @Override
    public boolean hasAmount(String name, BigDecimal amount, String currency) {
        Result<Money> balanceResult =  this.getBalanceUseCase.execute(name , currency);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve balance: " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().hasEnough(amount);
    }

    @Override
    public boolean existCurrency(String nameCurrency) {
        Result<ICurrency> currencyResult =  this.searchCurrencyUseCase.getCurrency(nameCurrency);
        return currencyResult.isSuccess();
    }

    @Override
    public boolean existAccount(UUID uuid) {
        Result<net.blockdynasty.economy.core.domain.entities.account.Account> accountResult =  getAccountByUUIDUseCase.execute(uuid);
        return accountResult.isSuccess();
    }

    @Override
    public boolean existAccount(String name) {
        Result<net.blockdynasty.economy.core.domain.entities.account.Account> accountResult =  getAccountByNameUseCase.execute(name);
        return accountResult.isSuccess();
    }

    @Override
    public String getDefaultCurrencyNamePlural() {
        Result<ICurrency> currencyResult =  this.searchCurrencyUseCase.getDefaultCurrency();
        if (currencyResult.isSuccess()) {
            return currencyResult.getValue().getPlural();
        }
        return "Unknown";
    }

    @Override
    public String getNameCurrencyPlural(String nameCurrency) {
        Result<ICurrency> currencyResult =  this.searchCurrencyUseCase.getCurrency(nameCurrency);
        if (currencyResult.isSuccess()) {
            return currencyResult.getValue().getPlural();
        }
        return "Unknown";
    }

    @Override
    public String getNameCurrencySingular(String nameCurrency) {
        Result<ICurrency> currencyResult =  this.searchCurrencyUseCase.getCurrency(nameCurrency);
        if (currencyResult.isSuccess()) {
            return currencyResult.getValue().getSingular();
        }
        return "Unknown";
    }

    @Override
    public String getDefaultCurrencyNameSingular() {
        Result<ICurrency> currencyResult =  this.searchCurrencyUseCase.getDefaultCurrency();
        if (currencyResult.isSuccess()) {
            return currencyResult.getValue().getSingular();
        }
        return "Unknown";
    }

    @Override
    public String format(BigDecimal amount) {
        Result<ICurrency> currencyResult =  this.searchCurrencyUseCase.getDefaultCurrency();
        if (currencyResult.isSuccess()) {
            ICurrency currency = currencyResult.getValue();
            return currency.format(amount);
        } else {
            return amount.toString();
        }
    }

    @Override
    public String format(BigDecimal amount, String currency) {
        Result<ICurrency> currencyResult =  this.searchCurrencyUseCase.getCurrency(currency);
        if (currencyResult.isSuccess()) {
            ICurrency curr = currencyResult.getValue();
            return curr.format(amount);
        } else {
            return amount.toString();
        }
    }

    @Override
    public List<String> getCurrenciesNamesList() {
        List<ICurrency> currenciesResult =  this.searchCurrencyUseCase.getCurrencies();
        return currenciesResult.stream().map(ICurrency::getPlural).collect(Collectors.toList());
    }

    @Override
    public List<Currency> getCurrencies() {
        //return api currencies
        return this.searchCurrencyUseCase.getCurrencies().stream().map(coreCurrency -> {
            return new Currency(
                    coreCurrency.getUuid(),
                    coreCurrency.getPlural(),
                    coreCurrency.getSingular(),
                    coreCurrency.getSymbol(),
                    coreCurrency.getColor(),
                    coreCurrency.getDefaultBalance(),
                    coreCurrency.getExchangeRate(),
                    coreCurrency.isDefaultCurrency()
            );
        }).collect(Collectors.toList());
    }

    @Override
    public EconomyResponse createAccount(UUID accountID, String name) {
        Result<net.blockdynasty.economy.core.domain.entities.account.Account> result =  this.createAccountUseCase.execute(accountID, name);
        if (result.isSuccess()) {
            return EconomyResponse.success();
        } else {
            return EconomyResponse.failure(result.getErrorMessage());
        }
    }

    @Override
    public EconomyResponse deleteAccount(UUID accountID) {
        Result<Void> result =  this.deleteAccountUseCase.execute(accountID);
        if (result.isSuccess()) {
            return EconomyResponse.success();
        } else {
            return EconomyResponse.failure(result.getErrorMessage());
        }
    }

    @Override
    public EconomyResponse deleteAccount(String name) {
        Result<Void> result =  this.deleteAccountUseCase.execute(name);
        if (result.isSuccess()) {
            return EconomyResponse.success();
        } else {
            return EconomyResponse.failure(result.getErrorMessage());
        }
    }

    @Override
    public EconomyResponse blockAccountTransactions(UUID accountID) {
        Result<Void> result =  this.editAccountUseCase.blockAccount(accountID);
        if (result.isSuccess()) {
            return EconomyResponse.success();
        } else {
            return EconomyResponse.failure(result.getErrorMessage());
        }
    }

    @Override
    public EconomyResponse unblockAccountTransactions(UUID accountID) {
        Result<Void> result =  this.editAccountUseCase.unblockAccount(accountID);
        if (result.isSuccess()) {
            return EconomyResponse.success();
        } else {
            return EconomyResponse.failure(result.getErrorMessage());
        }
    }

    @Override
    public EconomyResponse createCurrency(String plural, String singular) {
        try{
            this.createCurrencyUseCase.execute(plural, singular);
            return EconomyResponse.success();
        }
        catch (Exception e){
            return EconomyResponse.failure(e.getMessage());
        }
    }

    @Override
    public EconomyResponse deleteCurrency(String name) {
        try {
            this.deleteCurrencyUseCase.deleteCurrency(name);
            return EconomyResponse.success();
        }catch (Exception e){
            return EconomyResponse.failure(e.getMessage());
        }
    }

    @Override
    public Account getAccount(UUID uuid) {
        Result<net.blockdynasty.economy.core.domain.entities.account.Account> result = getAccountByUUIDUseCase.execute(uuid);
        if (!result.isSuccess()) {
            return null;
        }
        net.blockdynasty.economy.core.domain.entities.account.Account account = result.getValue();
        return new Account(
                account.getNickname(),
                account.getUuid(),
                new Wallet(
                        account.getWallet().getBalances().stream()
                                .map(coreMoney -> new net.blockdynasty.economy.api.entity.Money(
                                        new Currency(
                                                coreMoney.getCurrency().getUuid(),
                                                coreMoney.getCurrency().getPlural(),
                                                coreMoney.getCurrency().getSingular(),
                                                coreMoney.getCurrency().getSymbol(),
                                                coreMoney.getCurrency().getColor(),
                                                coreMoney.getCurrency().getDefaultBalance(),
                                                coreMoney.getCurrency().getExchangeRate(),
                                                coreMoney.getCurrency().isDefaultCurrency()
                                        ),
                                        coreMoney.getAmount()
                                )).collect(Collectors.toList())
                ),
                account.canReceiveCurrency(),
                account.isBlocked()
        );
    }

    @Override
    public Account getAccount(String name) {
        Result<net.blockdynasty.economy.core.domain.entities.account.Account> result = getAccountByNameUseCase.execute(name);
        if (!result.isSuccess()) {
            return null;
        }
        net.blockdynasty.economy.core.domain.entities.account.Account account = result.getValue();
        return new Account(
                account.getNickname(),
                account.getUuid(),
                new Wallet(
                        account.getWallet().getBalances().stream()
                                .map(coreMoney -> new net.blockdynasty.economy.api.entity.Money(
                                        new Currency(
                                                coreMoney.getCurrency().getUuid(),
                                                coreMoney.getCurrency().getPlural(),
                                                coreMoney.getCurrency().getSingular(),
                                                coreMoney.getCurrency().getSymbol(),
                                                coreMoney.getCurrency().getColor(),
                                                coreMoney.getCurrency().getDefaultBalance(),
                                                coreMoney.getCurrency().getExchangeRate(),
                                                coreMoney.getCurrency().isDefaultCurrency()
                                        ),
                                        coreMoney.getAmount()
                                )).collect(Collectors.toList())
                ),
                account.canReceiveCurrency(),
                account.isBlocked()
        );
    }

    @Override
    public List<Account> getTopAccounts(int limit, String currency) {
        List<net.blockdynasty.economy.core.domain.entities.account.Account> accounts = topAccounts.execute( currency, limit,0).getValue();
        return accounts.stream().map(coreA ->{
            List<net.blockdynasty.economy.api.entity.Money> moneyList =coreA.getWallet().getBalances().stream()
                    .map(coreMoney ->{
                        return new net.blockdynasty.economy.api.entity.Money(
                                new Currency(
                                        coreMoney.getCurrency().getUuid(),
                                        coreMoney.getCurrency().getPlural(),
                                        coreMoney.getCurrency().getSingular(),
                                        coreMoney.getCurrency().getSymbol(),
                                        coreMoney.getCurrency().getColor(),
                                        coreMoney.getCurrency().getDefaultBalance(),
                                        coreMoney.getCurrency().getExchangeRate(),
                                        coreMoney.getCurrency().isDefaultCurrency()

                                ),coreMoney.getAmount()
                        );
                    }).collect(Collectors.toList());
            return new Account(
                    coreA.getNickname(),
                    coreA.getUuid(),
                    new Wallet(moneyList),
                    coreA.canReceiveCurrency(),
                    coreA.isBlocked()
            );
        }).collect(Collectors.toList());
    }

    @Override
    public EconomyResponse setCurrencyStartBalance(String name, BigDecimal startBal) {
        try {
            editCurrencyUseCase.editStartBal(name, startBal.doubleValue());
            return EconomyResponse.success();
        }catch (Exception e){
            return EconomyResponse.failure(e.getMessage());
        }
    }

    @Override
    public EconomyResponse setCurrencyColor(String currencyName, String colorString) {
        try {
            editCurrencyUseCase.editColor(currencyName, colorString);
            return EconomyResponse.success();
        }catch (Exception e){
            return EconomyResponse.failure(e.getMessage());
        }
    }

    @Override
    public EconomyResponse setCurrencyRate(String currencyName, BigDecimal rate) {
        try {
            editCurrencyUseCase.setCurrencyRate(currencyName, rate.doubleValue());
            return EconomyResponse.success();
        }catch (Exception e){
            return EconomyResponse.failure(e.getMessage());
        }}

    @Override
    public EconomyResponse setCurrencyDecimalSupport(String currencyName, boolean supportDecimals) {
        try {
            Result<ICurrency> currency =searchCurrencyUseCase.getCurrency(currencyName);
            if (!currency.isSuccess()) {
                throw new IllegalStateException( "Currency not found: " + currency.getErrorMessage());
            }
            ICurrency curr = currency.getValue();
            if (curr.isDecimalSupported() != supportDecimals) {
                editCurrencyUseCase.toggleDecimals(currencyName);
            }
            return EconomyResponse.success();
        }catch (Exception e){
            return EconomyResponse.failure(e.getMessage());
        }
    }

    @Override
    public EconomyResponse setCurrencySymbol(String currencyName, String symbol) {
        try {
            editCurrencyUseCase.editSymbol(currencyName, symbol);
            return EconomyResponse.success();
        }catch (Exception e){
            return EconomyResponse.failure(e.getMessage());
        }
    }

    @Override
    public EconomyResponse setDefaultCurrency(String currencyName) {
        try {
            editCurrencyUseCase.setDefaultCurrency(currencyName);
            return EconomyResponse.success();
        }catch (Exception e){
            return EconomyResponse.failure(e.getMessage());
        }
    }

    @Override
    public EconomyResponse setSingularName(String currentName, String newName) {
        try {
            editCurrencyUseCase.setSingularName(currentName, newName);
            return EconomyResponse.success();
        } catch (Exception e){
            return EconomyResponse.failure(e.getMessage());
        }
    }

    @Override
    public EconomyResponse setPluralName(String currentName, String newName) {
        try {
            editCurrencyUseCase.setPluralName(currentName, newName);
            return EconomyResponse.success();
        } catch (Exception e){
            return EconomyResponse.failure(e.getMessage());
        }
    }

    @Override
    public EconomyResponse setPayable(String currencyName, boolean isPayable) {
        try {
            Result<ICurrency> currency =searchCurrencyUseCase.getCurrency(currencyName);
            if (!currency.isSuccess()) {
                throw new IllegalStateException( "Currency not found: " + currency.getErrorMessage());
            }
            ICurrency curr = currency.getValue();
            if (curr.isTransferable() != isPayable) {
                editCurrencyUseCase.togglePayable(currencyName);
            }
            return EconomyResponse.success();
        }catch (Exception e){
            return EconomyResponse.failure(e.getMessage());
        }
    }

    public EconomyResponse saveCurrency(Currency currency) {
        Result<ICurrency> result =  this.searchCurrencyUseCase.getCurrency(currency.getPlural());
        if (!result.isSuccess()) {
            return EconomyResponse.failure("Currency not found: " + result.getErrorMessage());
        }
        ICurrency coreCurrency = result.getValue();
        coreCurrency.setPlural(currency.getPlural());
        coreCurrency.setSingular(currency.getSingular());
        coreCurrency.setSymbol(currency.getSymbol());
        coreCurrency.setColor(currency.getColor());
        coreCurrency.setDefaultBalance(currency.getDefaultBalance());
        coreCurrency.setExchangeRate(currency.getExchangeRate());
        coreCurrency.setDefaultCurrency(currency.isDefaultCurrency());
        try {
            editCurrencyUseCase.saveCurrency(coreCurrency);
            return EconomyResponse.success();
        } catch (Exception e){
            return EconomyResponse.failure(e.getMessage());
        }
    }

    public Currency getCurrency(String name){
        Result<ICurrency> result = this.searchCurrencyUseCase.getCurrency(name);
        if (!result.isSuccess()){
            return null;
        }
        ICurrency currency = result.getValue();
        return new Currency(
                currency.getUuid(),
                currency.getPlural(),
                currency.getSingular(),
                currency.getSymbol(),
                currency.getColor(),
                currency.getDefaultBalance(),
                currency.getExchangeRate(),
                currency.isDefaultCurrency()
        );
    }
}

