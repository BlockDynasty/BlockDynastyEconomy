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

package BlockDynasty.BukkitImplementation.Integrations.vaultUnloked;


import api.IApi;
import net.milkbowl.vault2.economy.AccountPermission;
import net.milkbowl.vault2.economy.Economy;
import net.milkbowl.vault2.economy.EconomyResponse;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;

public class VaultUnlockedHook implements Economy {
    IApi api;

    public VaultUnlockedHook(IApi api) {
        this.api = api;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public @NotNull String getName() {
        return "BlockDynastyEconomy";
    }

    @Override
    public boolean hasSharedAccountSupport() {
        return false;
    }

    @Override
    public boolean hasMultiCurrencySupport() {
        return true;
    }

    @Override
    public @NotNull int fractionalDigits(@NotNull String pluginName) {
        return 0;
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal amount) {
        return api.format(amount);
    }

    @Override
    public @NotNull String format(@NotNull String pluginName, @NotNull BigDecimal amount) {
        return format(amount);
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal amount, @NotNull String currency) {
        return api.format(amount, currency);
    }

    @Override
    public @NotNull String format(@NotNull String pluginName, @NotNull BigDecimal amount, @NotNull String currency) {
        return format(amount, currency);
    }

    @Override
    public boolean hasCurrency(@NotNull String currency) {
        return api.existCurrency(currency);
    }

    @Override
    public @NotNull String getDefaultCurrency(@NotNull String pluginName) {
        return api.getDefaultCurrencyNameSingular();
    }

    @Override
    public @NotNull String defaultCurrencyNamePlural(@NotNull String pluginName) {
        return api.getDefaultCurrencyNamePlural();
    }

    @Override
    public @NotNull String defaultCurrencyNameSingular(@NotNull String pluginName) {
        return api.getDefaultCurrencyNameSingular();
    }

    @Override
    public @NotNull Collection<String> currencies() {
        return api.getCurrenciesNamesList();
    }

    @Override
    public boolean createAccount(@NotNull UUID accountID, @NotNull String name) {
        return api.createAccount( accountID, name).isSuccess();
    }

    @Override
    public boolean createAccount(@NotNull UUID accountID, @NotNull String name, boolean player) {
        return createAccount(accountID, name);
    }

    @Override
    public boolean createAccount(@NotNull UUID accountID, @NotNull String name, @NotNull String worldName) {
        return createAccount(accountID, name);
    }

    @Override
    public boolean createAccount(@NotNull UUID accountID, @NotNull String name, @NotNull String worldName, boolean player) {
        return createAccount(accountID, name);
    }

    @Override
    public @NotNull Map<UUID, String> getUUIDNameMap() {
        //Result<List<Account>> accountsResult = getAccountsUseCase.getAllAccounts();
        return Map.of();
    }

    @Override
    public Optional<String> getAccountName(@NotNull UUID accountID) {
        String accountResult;
        try {
             accountResult = api.getAccount(accountID).getNickname();
        }catch (NullPointerException e){
            return Optional.empty();
        }
        return Optional.ofNullable(accountResult);
    }

    @Override
    public boolean hasAccount(@NotNull UUID accountID) {
        return api.existAccount(accountID);
    }

    @Override
    public boolean hasAccount(@NotNull UUID accountID, @NotNull String worldName) {
        return hasAccount(accountID);
    }

    @Override
    public boolean renameAccount(@NotNull UUID accountID, @NotNull String name) {
        return false;
    }

    @Override
    public boolean renameAccount(@NotNull String plugin, @NotNull UUID accountID, @NotNull String name) {
        return false;
    }

    @Override
    public boolean deleteAccount(@NotNull String plugin, @NotNull UUID accountID) {
        return false;
    }

    @Override
    public boolean accountSupportsCurrency(@NotNull String plugin, @NotNull UUID accountID, @NotNull String currency) {
        return true;
    }

    @Override
    public boolean accountSupportsCurrency(@NotNull String plugin, @NotNull UUID accountID, @NotNull String currency, @NotNull String world) {
        return true;
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull String pluginName, @NotNull UUID accountID) {
        return api.getBalance(accountID);
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String world) {
        return api.getBalance(accountID);
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String world, @NotNull String currency) {
        return api.getBalance(accountID,currency);
    }

    @Override
    public @NotNull BigDecimal balance(@NotNull String pluginName, @NotNull UUID accountID) {
        return api.getBalance(accountID);
    }

    @Override
    public @NotNull BigDecimal balance(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String world) {
        return api.getBalance(accountID);
    }

    @Override
    public @NotNull BigDecimal balance(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String world, @NotNull String currency) {
        return api.getBalance(accountID,currency);
    }

    @Override
    public boolean has(@NotNull String pluginName, @NotNull UUID accountID, @NotNull BigDecimal amount) {
        return api.hasAmount(accountID, amount);
    }

    @Override
    public boolean has(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull BigDecimal amount) {
        return api.hasAmount(accountID, amount);
    }

    @Override
    public boolean has(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull String currency, @NotNull BigDecimal amount) {
        return api.hasAmount(accountID, amount, currency);
    }

    //todo: implementar
    @Override
    public EconomyResponse set(@NotNull String pluginName, @NotNull UUID accountID, @NotNull BigDecimal amount) {
        api.EconomyResponse response = api.setBalance(accountID, amount);
        if(response.isSuccess()){
            return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.SUCCESS, "set balance success for "+accountID);
        }else {
            return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.FAILURE, "Transaction error"+response.getErrorMessage());
        }
    }

    @Override
    public EconomyResponse set(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull BigDecimal amount) {
        api.EconomyResponse response = api.setBalance(accountID, amount);
        if(response.isSuccess()){
            return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.SUCCESS, "set balance success for "+accountID);
        }else {
            return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.FAILURE, "Transaction error"+response.getErrorMessage());
        }
    }

    @Override
    public EconomyResponse set(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull String currency, @NotNull BigDecimal amount) {
        api.EconomyResponse response = api.setBalance(accountID, amount,currency);
        if(response.isSuccess()){
            return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.SUCCESS, "set balance success for "+accountID);
        }else {
            return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.FAILURE, "Transaction error"+response.getErrorMessage());
        }
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull String pluginName, @NotNull UUID accountID, @NotNull BigDecimal amount) {
        api.EconomyResponse resultWithdraw = api.withdraw(accountID, amount);
        if(resultWithdraw.isSuccess()){
            return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.SUCCESS, "withdraw success for "+accountID);
        }
        return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultWithdraw.getErrorMessage());
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull BigDecimal amount) {
        return withdraw(pluginName, accountID, amount);
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull String currency, @NotNull BigDecimal amount) {
        api.EconomyResponse resultWithdraw = api.withdraw(accountID, amount,currency);
        if(resultWithdraw.isSuccess()){
            return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.SUCCESS, "withdraw success for "+accountID);
        }
        return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultWithdraw.getErrorMessage());
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull String pluginName, @NotNull UUID accountID, @NotNull BigDecimal amount) {
        api.EconomyResponse resultDeposit = api.deposit(accountID, amount);
        if(resultDeposit.isSuccess()){
            return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.SUCCESS, "deposit success for "+accountID);
        }
        return new EconomyResponse(amount, getBalance("",accountID), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultDeposit.getErrorMessage());
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull BigDecimal amount) {
        return deposit(pluginName, accountID, amount);
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String worldName, @NotNull String currency, @NotNull BigDecimal amount) {
        api.EconomyResponse resultDeposit = api.deposit(accountID, amount,currency);
        if(resultDeposit.isSuccess()){
            return new EconomyResponse(amount, getBalance("",accountID,"",currency), EconomyResponse.ResponseType.SUCCESS, "deposit success for "+accountID);
        }
        return new EconomyResponse(amount, getBalance("",accountID,"",currency), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultDeposit.getErrorMessage());
    }

    @Override
    public boolean createSharedAccount(@NotNull String pluginName, @NotNull UUID accountID, @NotNull String name, @NotNull UUID owner) {
        return false;
    }

    @Override
    public boolean isAccountOwner(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean setOwner(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean isAccountMember(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean addAccountMember(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean addAccountMember(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid, @NotNull AccountPermission... initialPermissions) {
        return false;
    }

    @Override
    public boolean removeAccountMember(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean hasAccountPermission(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid, @NotNull AccountPermission permission) {
        return false;
    }

    @Override
    public boolean updateAccountPermission(@NotNull String pluginName, @NotNull UUID accountID, @NotNull UUID uuid, @NotNull AccountPermission permission, boolean value) {
        return false;
    }
}
