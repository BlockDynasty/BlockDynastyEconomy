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
package BlockDynasty.BukkitImplementation.Integrations.treasuryEconomy.accounts;

import com.BlockDynasty.api.EconomyResponse;
import com.BlockDynasty.api.DynastyEconomy;
import com.BlockDynasty.api.entity.Account;
import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class NonPlayerAccountTreasury implements NonPlayerAccount {
    private DynastyEconomy api;
    private String nameAccount;

    public NonPlayerAccountTreasury(@NotNull DynastyEconomy api, String nameAccount) {
        this.api = api;
        this.nameAccount = nameAccount;
    }

    @Override
    public @NotNull NamespacedKey identifier() {
        //namespacekey???
        return NamespacedKey.of( "blockdynasty",nameAccount) ;
    }

    @Override
    public @NotNull Optional<String> getName() {
        return Optional.ofNullable(nameAccount);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> setName(@Nullable String name) {
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> retrieveBalance(@NotNull Currency currency) {
        return CompletableFuture.completedFuture(api.getBalance(nameAccount, currency.getIdentifier()));
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> doTransaction(@NotNull EconomyTransaction economyTransaction) {
        EconomyTransactionType type = economyTransaction.getType();
        //deposit
        switch (type) {
            case DEPOSIT -> {
                api.deposit(nameAccount, economyTransaction.getAmount() , economyTransaction.getCurrencyId());
            }
            case WITHDRAWAL -> {
                api.withdraw(nameAccount, economyTransaction.getAmount() , economyTransaction.getCurrencyId());
            }
            case SET -> {
                api.setBalance(nameAccount, economyTransaction.getAmount() , economyTransaction.getCurrencyId());
            }
        }
        return CompletableFuture.completedFuture(BigDecimal.ZERO);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> deleteAccount() {
        EconomyResponse response = api.deleteAccount(nameAccount);
        if (response.isSuccess()) {
            return CompletableFuture.completedFuture(true);
        }else {
            return CompletableFuture.completedFuture(false);
        }
    }

    @Override
    public @NotNull CompletableFuture<Collection<String>> retrieveHeldCurrencies() {
        Account account = api.getAccount(nameAccount);
        List<String> currencies= account.getBalances().stream().map(money -> money.getCurrency().getSingular()).toList();
        return CompletableFuture.completedFuture(currencies);
    }

    @Override
    public @NotNull CompletableFuture<Collection<EconomyTransaction>> retrieveTransactionHistory(int transactionCount, @NotNull Temporal from, @NotNull Temporal to) {
        return CompletableFuture.completedFuture(new ArrayList<>());
    }

    @Override
    public @NotNull CompletableFuture<Collection<UUID>> retrieveMemberIds() {
        return CompletableFuture.completedFuture(new ArrayList<>());
    }

    @Override
    public @NotNull CompletableFuture<Boolean> isMember(@NotNull UUID player) {
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> setPermissions(@NotNull UUID player, @NotNull Map<AccountPermission, TriState> permissionsMap) {
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public @NotNull CompletableFuture<Map<AccountPermission, TriState>> retrievePermissions(@NotNull UUID player) {
        return CompletableFuture.completedFuture( new HashMap<>() );
    }

    @Override
    public @NotNull CompletableFuture<Map<UUID, Map<AccountPermission, TriState>>> retrievePermissionsMap() {
        return CompletableFuture.completedFuture( new HashMap<>() );
    }

    @Override
    public @NotNull CompletableFuture<TriState> hasPermissions(@NotNull UUID player, @NotNull AccountPermission @NotNull ... permissions) {
        return null;
    }
}
