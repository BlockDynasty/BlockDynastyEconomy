package com.blockdynasty.economy.apiImplement;

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.EditAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetTopAccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.balance.GetBalanceUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.*;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.log.Log;
import com.BlockDynasty.api.DynastyEconomy;
import com.BlockDynasty.api.EconomyResponse;
import com.BlockDynasty.api.entity.Account;
import com.BlockDynasty.api.entity.Currency;
import com.BlockDynasty.api.entity.Wallet;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class DynastyEconomyApi implements DynastyEconomy {
    private final UUID id;
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private GetBalanceUseCase getBalanceUseCase;
    private CreateAccountUseCase createAccountUseCase;
    private GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    private GetAccountByNameUseCase getAccountByNameUseCase;
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
    private IAccountService accountService;

    public DynastyEconomyApi(UseCaseFactory factory, IAccountService accountService) {
        id = UUID.randomUUID();
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
        this.accountService = accountService;
    }
    public DynastyEconomyApi(UseCaseFactory factory, IAccountService accountService, Log log) {
        id = UUID.randomUUID();
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
        this.accountService = accountService;
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
                currency.getDefaultBalance(),
                currency.getExchangeRate(),
                currency.isDefaultCurrency()
        );
    }

    @Override
    public List<Account> getAccountsOffline() {
        //format core account to api account
        List<BlockDynasty.Economy.domain.entities.account.Account> accounts = accountService.getAccountsOffline();
        return accounts.stream().map(coreA ->{
            List<com.BlockDynasty.api.entity.Money> moneyList =coreA.getWallet().getBalances().stream()
                    .map(coreMoney ->{
                        return new com.BlockDynasty.api.entity.Money(
                                new Currency(
                                        coreMoney.getCurrency().getUuid(),
                                        coreMoney.getCurrency().getPlural(),
                                        coreMoney.getCurrency().getSingular(),
                                        coreMoney.getCurrency().getSymbol(),
                                        coreMoney.getCurrency().getDefaultBalance(),
                                        coreMoney.getCurrency().getExchangeRate(),
                                        coreMoney.getCurrency().isDefaultCurrency()

                                ),coreMoney.getAmount()
                        );
                    }).collect(Collectors.toList());
            return new com.BlockDynasty.api.entity.Account(
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
        Result<BlockDynasty.Economy.domain.entities.account.Account> accountResult =  getAccountByUUIDUseCase.execute(uuid);
        return accountResult.isSuccess();
    }

    @Override
    public boolean existAccount(String name) {
        Result<BlockDynasty.Economy.domain.entities.account.Account> accountResult =  getAccountByNameUseCase.execute(name);
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
                    coreCurrency.getDefaultBalance(),
                    coreCurrency.getExchangeRate(),
                    coreCurrency.isDefaultCurrency()
            );
        }).collect(Collectors.toList());
    }

    @Override
    public EconomyResponse createAccount(UUID accountID, String name) {
        Result<BlockDynasty.Economy.domain.entities.account.Account> result =  this.createAccountUseCase.execute(accountID, name);
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
    public com.BlockDynasty.api.entity.Account getAccount(UUID uuid) {
        BlockDynasty.Economy.domain.entities.account.Account account = getAccountByUUIDUseCase.execute(uuid).getValue();
        return new com.BlockDynasty.api.entity.Account(
                account.getNickname(),
                account.getUuid(),
                new Wallet(
                        account.getWallet().getBalances().stream()
                                .map(coreMoney -> new com.BlockDynasty.api.entity.Money(
                                        new Currency(
                                                coreMoney.getCurrency().getUuid(),
                                                coreMoney.getCurrency().getPlural(),
                                                coreMoney.getCurrency().getSingular(),
                                                coreMoney.getCurrency().getSymbol(),
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
    public com.BlockDynasty.api.entity.Account getAccount(String name) {
        BlockDynasty.Economy.domain.entities.account.Account account = getAccountByNameUseCase.execute(name).getValue();
        return new com.BlockDynasty.api.entity.Account(
                account.getNickname(),
                account.getUuid(),
                new Wallet(
                        account.getWallet().getBalances().stream()
                                .map(coreMoney -> new com.BlockDynasty.api.entity.Money(
                                        new Currency(
                                                coreMoney.getCurrency().getUuid(),
                                                coreMoney.getCurrency().getPlural(),
                                                coreMoney.getCurrency().getSingular(),
                                                coreMoney.getCurrency().getSymbol(),
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
    public List< com.BlockDynasty.api.entity.Account> getTopAccounts(int limit, String currency) {
        List<BlockDynasty.Economy.domain.entities.account.Account> accounts = topAccounts.execute( currency, limit,0).getValue();
        return accounts.stream().map(coreA ->{
            List<com.BlockDynasty.api.entity.Money> moneyList =coreA.getWallet().getBalances().stream()
                    .map(coreMoney ->{
                        return new com.BlockDynasty.api.entity.Money(
                                new Currency(
                                        coreMoney.getCurrency().getUuid(),
                                        coreMoney.getCurrency().getPlural(),
                                        coreMoney.getCurrency().getSingular(),
                                        coreMoney.getCurrency().getSymbol(),
                                        coreMoney.getCurrency().getDefaultBalance(),
                                        coreMoney.getCurrency().getExchangeRate(),
                                        coreMoney.getCurrency().isDefaultCurrency()

                                ),coreMoney.getAmount()
                        );
                    }).collect(Collectors.toList());
            return new com.BlockDynasty.api.entity.Account(
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
        return EconomyResponse.notImplemented();
    }

    @Override
    public EconomyResponse setCurrencyColor(String currencyName, String colorString) {
        return EconomyResponse.notImplemented();
    }

    @Override
    public EconomyResponse setCurrencyRate(String currencyName, BigDecimal rate) {
        return EconomyResponse.notImplemented();
    }

    @Override
    public EconomyResponse setCurrencyDecimalSupport(String currencyName, boolean supportDecimals) {
        return EconomyResponse.notImplemented();
    }

    @Override
    public EconomyResponse setCurrencySymbol(String currencyName, String symbol) {
        return null;
    }

    @Override
    public EconomyResponse setDefaultCurrency(String currencyName) {
        return EconomyResponse.notImplemented();
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
        return EconomyResponse.notImplemented();
    }

    public Currency getCurrency(String name){
        ICurrency currency = this.searchCurrencyUseCase.getCurrency(name).getValue();
        return new Currency(
                currency.getUuid(),
                currency.getPlural(),
                currency.getSingular(),
                currency.getSymbol(),
                currency.getDefaultBalance(),
                currency.getExchangeRate(),
                currency.isDefaultCurrency()
        );
    }
}

