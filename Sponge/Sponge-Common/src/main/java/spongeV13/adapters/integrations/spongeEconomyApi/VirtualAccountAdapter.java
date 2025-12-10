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

import net.kyori.adventure.text.Component;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.VirtualAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransferResult;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class VirtualAccountAdapter implements VirtualAccount {
    private UniqueAccountAdapter uniqueAccountAdapter;

    public VirtualAccountAdapter(UniqueAccountAdapter uniqueAccountAdapter) {
        this.uniqueAccountAdapter = uniqueAccountAdapter;
    }

    @Override
    public Component displayName() {
        return uniqueAccountAdapter.displayName();
    }

    @Override
    public BigDecimal defaultBalance(Currency currency) {
        return uniqueAccountAdapter.defaultBalance(currency);
    }

    @Override
    public boolean hasBalance(Currency currency, Set<Context> contexts) {
        return uniqueAccountAdapter.hasBalance(currency, contexts);
    }

    @Override
    public boolean hasBalance(Currency currency, Cause cause) {
        return uniqueAccountAdapter.hasBalance(currency, cause);
    }

    @Override
    public BigDecimal balance(Currency currency, Set<Context> contexts) {
        return uniqueAccountAdapter.balance(currency, contexts);
    }

    @Override
    public BigDecimal balance(Currency currency, Cause cause) {
        return uniqueAccountAdapter.balance(currency, cause);
    }

    @Override
    public Map<Currency, BigDecimal> balances(Set<Context> contexts) {
        return uniqueAccountAdapter.balances(contexts);
    }

    @Override
    public Map<Currency, BigDecimal> balances(Cause cause) {
        return uniqueAccountAdapter.balances(cause);
    }

    @Override
    public TransactionResult setBalance(Currency currency, BigDecimal amount, Set<Context> contexts) {
        return uniqueAccountAdapter.setBalance(currency, amount, contexts);
    }

    @Override
    public TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause) {
        return uniqueAccountAdapter.setBalance(currency, amount, cause);
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(Set<Context> contexts) {
        return uniqueAccountAdapter.resetBalances(contexts);
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(Cause cause) {
        return uniqueAccountAdapter.resetBalances(cause);
    }

    @Override
    public TransactionResult resetBalance(Currency currency, Set<Context> contexts) {
        return uniqueAccountAdapter.resetBalance(currency, contexts);
    }

    @Override
    public TransactionResult resetBalance(Currency currency, Cause cause) {
        return uniqueAccountAdapter.resetBalance(currency, cause);
    }

    @Override
    public TransactionResult deposit(Currency currency, BigDecimal amount, Set<Context> contexts) {
        return uniqueAccountAdapter.deposit(currency, amount, contexts);
    }

    @Override
    public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause) {
        return uniqueAccountAdapter.deposit(currency, amount, cause);
    }

    @Override
    public TransactionResult withdraw(Currency currency, BigDecimal amount, Set<Context> contexts) {
        return uniqueAccountAdapter.withdraw(currency, amount, contexts);
    }

    @Override
    public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause) {
        return uniqueAccountAdapter.withdraw(currency, amount, cause);
    }

    @Override
    public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Set<Context> contexts) {
        return uniqueAccountAdapter.transfer(to, currency, amount, contexts);
    }

    @Override
    public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Cause cause) {
        return uniqueAccountAdapter.transfer(to, currency, amount, cause);
    }

    @Override
    public String identifier() {
        return uniqueAccountAdapter.identifier();
    }
}
