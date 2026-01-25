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

package spongeV13.adapters.integrations.spongeEconomyApi;
import com.BlockDynasty.api.EconomyResponse;
import com.BlockDynasty.api.DynastyEconomy;
import net.blockdynasty.providers.services.ServiceProvider;
import com.BlockDynasty.api.entity.Money;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;
import org.spongepowered.api.service.economy.transaction.TransferResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UniqueAccountAdapter implements UniqueAccount{
    private com.BlockDynasty.api.entity.Account account;
    private static DynastyEconomy api = ServiceProvider.get(DynastyEconomy.class);

    public UniqueAccountAdapter( com.BlockDynasty.api.entity.Account account) {
        this.account = account;
    }

    @Override
    public Component displayName() {
        return Component.text(account.getName());
    }

    @Override
    public BigDecimal defaultBalance(Currency currency) {
        return api.getBalance(account.getUuid());
    }

    @Override
    public boolean hasBalance(Currency currency, Set<Context> contexts) {
        try {
            api.getBalance(account.getUuid());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean hasBalance(Currency currency, Cause cause) {
        try {
            api.getBalance(account.getUuid());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public BigDecimal balance(Currency currency, Set<Context> contexts) {
        try {
            return api.getBalance(account.getUuid());
        }catch (Exception e){
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal balance(Currency currency, Cause cause) {
        try {
            return api.getBalance(account.getUuid());
        }catch (Exception e){
            return BigDecimal.ZERO;
        }
    }

    @Override
    public Map<Currency, BigDecimal> balances(Set<Context> contexts) {
        List<Money> moneyList = account.getWallet().getBalances();
        Map<Currency, BigDecimal> balances = new java.util.HashMap<>();
        for(Money money : moneyList){
            balances.put(new CurrencyAdapter(money.getCurrency()), money.getAmount());
        }
        return balances;
    }

    @Override
    public Map<Currency, BigDecimal> balances(Cause cause) {
        List<Money> moneyList = account.getWallet().getBalances();
        Map<Currency, BigDecimal> balances = new java.util.HashMap<>();
        for(Money money : moneyList){
            balances.put(new CurrencyAdapter(money.getCurrency()), money.getAmount());
        }
        return balances;
    }

    @Override
    public TransactionResult setBalance(Currency currency, BigDecimal amount, Set<Context> contexts) {
        EconomyResponse response = api.setBalance(account.getUuid(), amount, PlainTextComponentSerializer.plainText().serialize(currency.displayName()));
        return new TransactionResultAdapter(response, currency, this, amount,TransactionTypes.DEPOSIT.get());
    }

    @Override
    public TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause) {
        EconomyResponse response = api.setBalance(account.getUuid(), amount, PlainTextComponentSerializer.plainText().serialize(currency.displayName()));
        return new TransactionResultAdapter(response, currency, this, amount,TransactionTypes.DEPOSIT.get());
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(Set<Context> contexts) {
        return Map.of();
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(Cause cause) {
        return Map.of();
    }

    @Override
    public TransactionResult resetBalance(Currency currency, Set<Context> contexts) {
        return null;
    }

    @Override
    public TransactionResult resetBalance(Currency currency, Cause cause) {
        return null;
    }

    @Override
    public TransactionResult deposit(Currency currency, BigDecimal amount, Set<Context> contexts) {
        EconomyResponse response = api.deposit(account.getUuid(), amount, PlainTextComponentSerializer.plainText().serialize(currency.displayName()));
        return new TransactionResultAdapter(response, currency, this, amount,TransactionTypes.DEPOSIT.get());
    }

    @Override
    public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause) {
        EconomyResponse response = api.deposit(account.getUuid(), amount, PlainTextComponentSerializer.plainText().serialize(currency.displayName()));
        return new TransactionResultAdapter(response, currency, this, amount,TransactionTypes.DEPOSIT.get());
    }

    @Override
    public TransactionResult withdraw(Currency currency, BigDecimal amount, Set<Context> contexts) {
        EconomyResponse response = api.withdraw(account.getUuid(), amount, PlainTextComponentSerializer.plainText().serialize(currency.displayName()));
        return new TransactionResultAdapter(response, currency, this, amount,TransactionTypes.WITHDRAW.get());
    }

    @Override
    public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause) {
        EconomyResponse response = api.withdraw(account.getUuid(), amount, PlainTextComponentSerializer.plainText().serialize(currency.displayName()));
        return new TransactionResultAdapter(response, currency, this, amount, TransactionTypes.WITHDRAW.get());
    }

    @Override
    public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Set<Context> contexts) {
        EconomyResponse response = api.transfer(this.identifier(),to.identifier(), PlainTextComponentSerializer.plainText().serialize(currency.displayName()),amount);
        return new TransferResultAdapter(response, currency, this, to, amount);
    }

    @Override
    public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Cause cause) {
        EconomyResponse response = api.transfer(this.identifier(),to.identifier(), PlainTextComponentSerializer.plainText().serialize(currency.displayName()),amount);
        return new TransferResultAdapter(response, currency, this, to, amount);
    }

    @Override
    public String identifier() {
        return account.getName();
    }

    @Override
    public UUID uniqueId() {
        return account.getUuid();
    }
}
