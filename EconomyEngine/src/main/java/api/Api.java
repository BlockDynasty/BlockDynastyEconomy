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

import BlockDynasty.Economy.Core;
import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.aplication.useCase.account.*;
import BlockDynasty.Economy.aplication.useCase.account.balance.GetBalanceUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetTopAccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.*;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.log.Log;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//todo : la api deberia proporcionar/exponer, todos los casos de usos existentes
public class Api implements IApi {
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final GetBalanceUseCase getBalanceUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    private final GetAccountByNameUseCase getAccountByNameUseCase;
    private final IWithdrawUseCase withdrawUseCase;
    private final IDepositUseCase depositUseCase;
    private final ISetBalanceUseCase setBalanceUseCase;
    private final ITradeUseCase tradeCurrenciesUseCase;
    private final ITransferUseCase transferFundsUseCase;
    private final IExchangeUseCase exchangeUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final EditAccountUseCase editAccountUseCase;
    private final CreateCurrencyUseCase createCurrencyUseCase;
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;
    private final GetTopAccountsUseCase topAccounts;

    private final IAccountService accountService;
    private final EventManager eventManager;

    public Api(Core core) {
        UseCaseFactory factory = core.getUseCaseFactory();
        accountService = core.getServicesManager().getAccountService();
        searchCurrencyUseCase = factory.searchCurrency();
        getBalanceUseCase = factory.getBalance();
        createAccountUseCase = factory.createAccount();
        getAccountByNameUseCase = factory.searchAccountByName();
        getAccountByUUIDUseCase = factory.searchAccountByUUID();
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

        this.eventManager= core.getServicesManager().getEventManager();
    }

    public Api(Core core, Log log){
        UseCaseFactory factory = core.getUseCaseFactory();
        accountService = core.getServicesManager().getAccountService();
        searchCurrencyUseCase = factory.searchCurrency();
        getBalanceUseCase = factory.getBalance();
        createAccountUseCase = factory.createAccount();
        getAccountByNameUseCase = factory.searchAccountByName();
        getAccountByUUIDUseCase = factory.searchAccountByUUID();
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

        this.eventManager= core.getServicesManager().getEventManager();
    }

    public EventManager getEventManager(){
        return eventManager;
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
    public ICurrency getDefaultCurrency() {
        return this.searchCurrencyUseCase.getDefaultCurrency().getValue();
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
        Result<Account> accountResult =  getAccountByUUIDUseCase.execute(uuid);
        return accountResult.isSuccess();
    }

    @Override
    public boolean existAccount(String name) {
        Result<Account> accountResult =  getAccountByNameUseCase.execute(name);
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
    public List<ICurrency> getCurrencies() {
        return this.searchCurrencyUseCase.getCurrencies();
    }

    @Override
    public EconomyResponse createAccount(UUID accountID, String name) {
        Result<Account> result =  this.createAccountUseCase.execute(accountID, name);
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
        return getAccountByUUIDUseCase.execute(uuid).getValue();
    }

    @Override
    public Account getAccount(String name) {
        return getAccountByNameUseCase.execute(name).getValue();
    }

    @Override
    public List<Account> getTopAccounts(int limit, String currency) {
        return topAccounts.execute( currency, limit,0).getValue();
    }

    @Override
    public EconomyResponse setCurrencyStartBalance(String name, BigDecimal startBal) {
        return null;
    }

    @Override
    public EconomyResponse setCurrencyColor(String currencyName, String colorString) {
        return null;
    }

    @Override
    public EconomyResponse setCurrencyRate(String currencyName, BigDecimal rate) {
        return null;
    }

    @Override
    public EconomyResponse setCurrencyDecimalSupport(String currencyName, boolean supportDecimals) {
        return null;
    }

    @Override
    public EconomyResponse setCurrencySymbol(String currencyName, String symbol) {
        return null;
    }

    @Override
    public EconomyResponse setDefaultCurrency(String currencyName) {
        return null;
    }

    @Override
    public EconomyResponse setSingularName(String currentName, String newName) {
        return null;
    }

    @Override
    public EconomyResponse setPluralName(String currentName, String newName) {
        return null;
    }

    @Override
    public EconomyResponse setPayable(String currencyName, boolean isPayable) {
        return null;
    }

    public ICurrency getCurrency(String name){
        return  this.searchCurrencyUseCase.getCurrency(name).getValue();
    }

    public IAccountService getAccountService(){
        return this.accountService;
    }
}
