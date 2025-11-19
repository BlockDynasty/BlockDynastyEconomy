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
package BlockDynasty.Economy.aplication.services;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AccountService implements IAccountService {
    private final IRepository dataStore;
    private final ICurrencyService currencyService;
    private final Map<String,List<Account>> accountsTopList;
    private final Map<UUID,Account> accountsOnlineUuid;
    private final Map<String,Account> accountsOnlineName;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final int expireCacheTopMinutes ;

    public AccountService(int expireCacheTopMinutes, IRepository dataStore , ICurrencyService currencyService) {
        this.currencyService = currencyService;
        this.dataStore = dataStore;
        this.accountsOnlineUuid = new HashMap<>();
        this.accountsOnlineName = new HashMap<>();
        this.accountsTopList = new HashMap<>();
        this.expireCacheTopMinutes = expireCacheTopMinutes;
        startClearingTask();
    }

    public void addAccountToOnline(Account account) {
        this.accountsOnlineUuid.put(account.getUuid(), account);
        this.accountsOnlineName.put(account.getNickname(), account);
    }
    public void removeAccountOnline(UUID uuid) {  //removeAccountFromCache
        Account account = accountsOnlineUuid.remove(uuid);
        if (account != null) {
            accountsOnlineName.remove(account.getNickname());
        }
    }
    public void removeAccountOnline(String name) {
        Account account = accountsOnlineName.remove(name);
        if (account != null) {
            accountsOnlineUuid.remove(account.getUuid());
        }
    }

    public List<Account> getAccountsOffline() {
        return this.dataStore.loadAccounts();
    }
    public List<Account> getAccountsOnline() {
        return new ArrayList<>(this.accountsOnlineUuid.values());
    }
    public Account getAccountOffline(String name) {
        return this.dataStore.loadAccountByName(name).getValue();
    }
    public Account getAccountOffline(UUID uuid) {
        return this.dataStore.loadAccountByUuid(uuid.toString()).getValue();
    }
    public Account getAccountOnline(String name){
        return  accountsOnlineName.getOrDefault(name,null);
    }
    public Account getAccountOnline(UUID uuid){
        return accountsOnlineUuid.getOrDefault(uuid,null);
    }

    public Account getAccount(String name) {
        Account account = this.getAccountOnline(name);
        if(account == null){
            account = this.getAccountOffline(name);
            if (account != null){
                syncWalletWithSystemCurrencies(account);
            }
        }
        return account;
    }
    public Account getAccount(UUID uuid) {
        Account account = this.getAccountOnline(uuid);
        if(account == null){
            account = this.getAccountOffline(uuid);
            if (account != null){
                syncWalletWithSystemCurrencies(account);
            }
        }
        return account;
    }

    //synchronization methods
    /*public void syncOnlineAccount(Account account){
        UUID uuid = account.getUuid();
        Account cachedAccount = this.getAccountOnline(uuid);
        if (cachedAccount != null) {
            for (Money updatedMoney : account.getBalances()) {
                Money cachedMoney = cachedAccount.getMoney(updatedMoney.getCurrency());
                if (cachedMoney != null) {
                    cachedMoney.setAmount(updatedMoney.getAmount());
                } else {
                    cachedAccount.getBalances().add(new Money(updatedMoney.getCurrency(), updatedMoney.getAmount()));
                }
            }
        }
    }*/

    public void syncOnlineAccount(Account account){
        UUID uuid = account.getUuid();
        Account cachedAccount = this.getAccountOnline(uuid);
        if (cachedAccount != null) {
            // Ensure incoming account balances use system Currency instances
            syncWalletWithSystemCurrencies(account);
            // Replace cached balances so cachedAccount uses canonical Currency objects too
            cachedAccount.setBalances(
                    account.getBalances().stream()
                            .map(m -> new Money(m.getCurrency(), m.getAmount()))
                            .collect(Collectors.toList())
            );
        }
    }

    public void syncOnlineAccount(UUID uuid){
        Account accountCache = this.getAccountOnline(uuid);
        if (accountCache != null){
            Result<Account> result =  this.dataStore.loadAccountByUuid(uuid.toString());
            if (result.isSuccess()){
                syncWalletWithSystemCurrencies(result.getValue());
                accountCache.setBalances(result.getValue().getBalances());
                accountCache.setBlocked(result.getValue().isBlocked());
                accountCache.setCanReceiveCurrency(result.getValue().canReceiveCurrency());
            }
        }

    }
    public void syncDbWithOnlineAccounts(){ //check delete this
        List<Account> accounts = this.getAccountsOnline();
        for (Account account : accounts) {
            syncWalletWithSystemCurrencies(account);
            try {
                dataStore.saveAccount(account);
            } catch (TransactionException e) {
                throw new TransactionException("Error in transaction", e);
            }
        }
    }
    /*private void syncWalletWithSystemCurrencies(Account account) {
        List<Money> updatedMonies =  this.currencyService.getCurrencies().stream()
                .map(systemCurrency ->
                        account.getBalances().stream()
                                .filter(balance -> balance.getCurrency().getUuid().equals(systemCurrency.getUuid()))
                                .findFirst() // Busca si ya existe el balance para esta moneda
                                .orElseGet(() -> { // Si no existe, crea un nuevo balance para esta moneda
                                    return new Money(systemCurrency);
                                }))
                .collect(Collectors.toList());
        account.setBalances(updatedMonies);
    }*/

    private void syncWalletWithSystemCurrencies(Account account) {
        List<Money> updatedMonies = this.currencyService.getCurrencies().stream()
                .map(systemCurrency -> {
                    Optional<Money> existing = account.getBalances().stream()
                            .filter(balance -> balance.getCurrency().getUuid().equals(systemCurrency.getUuid()))
                            .findFirst();
                    // Build a new Money that uses the canonical systemCurrency instance and preserves amount
                    return existing
                            .map(b -> new Money(systemCurrency, b.getAmount()))
                            .orElseGet(() -> new Money(systemCurrency));
                })
                .collect(Collectors.toList());
        account.setBalances(updatedMonies);
    }
    //---------------

    //validators methods

    public Result<Void> checkNameChange(Account account , String newName) {
        if (!account.getNickname().equalsIgnoreCase(newName)) {
            account.setNickname(newName);
            try {
                this.dataStore.saveAccount(account);
            } catch (TransactionException e) {
                return Result.failure( "Error saving account after name change", ErrorCode.DATA_BASE_ERROR);
            }
        }
        return Result.success(null);
    }
    public Result<Void> checkUuidChange(Account account, UUID newUuid) {
        if (!account.getUuid().equals(newUuid)) {
            account.setUuid(newUuid);
            try {
                this.dataStore.saveAccount(account);
            } catch (TransactionException e) {
                return Result.failure("Error saving account after UUID change", ErrorCode.DATA_BASE_ERROR);
            }
        }
        return Result.success(null);
    }

//top list cache
    public void addAccountToTopList(Account account,String currencyName) {
        List<Account> accounts = accountsTopList.computeIfAbsent(currencyName, k -> new ArrayList<>());
        accounts.add(account);
    }
    public List<Account> getAccountsTopList(String currency) {
        return accountsTopList.getOrDefault(currency, new ArrayList<>());
    }
    public void clearTopList() {
        accountsTopList.clear();
    }
    private void startClearingTask() {
        //System.out.println("Top list cache cleared");
        scheduler.scheduleAtFixedRate(this::clearTopList, 0,expireCacheTopMinutes, TimeUnit.MINUTES);
    }
}