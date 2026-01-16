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

package BlockDynasty.BukkitImplementation.Integrations.vault2;

import BlockDynasty.BukkitImplementation.Integrations.vault.VaultHook;
import com.BlockDynasty.api.DynastyEconomy;
import com.BlockDynasty.api.DynastyEconomyWithoutLogger;
import com.BlockDynasty.api.ServiceProvider;
import com.BlockDynasty.api.entity.Currency;
import net.milkbowl.vault.economy.EconomyMultiCurrency;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.List;

public class Vault2Hook extends VaultHook implements EconomyMultiCurrency {
    private static DynastyEconomyWithoutLogger api;

    public Vault2Hook() {
        Vault2Hook.api = ServiceProvider.get(DynastyEconomyWithoutLogger.class);
    }

    @Override
    public List<String> getCurrencies() {
        return api.getCurrencies().stream().map(Currency::getSingular).toList();
    }

    @Override
    public int fractionalDigits(String currencyName) {
        return -1;
    }

    @Override
    public String format(double amount, String currencyName) {
        return api.format(new BigDecimal(amount),currencyName);
    }

    @Override
    public String currencyNamePlural(String currencyName) {
        return api.getNameCurrencyPlural(currencyName);
    }

    @Override
    public String currencyNameSingular(String currencyName) {
        return api.getNameCurrencySingular(currencyName);
    }
    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String world, String currencyName) {
        return api.getBalance(offlinePlayer.getName(), currencyName).doubleValue();
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount, String currencyName) {
        return api.hasAmount(offlinePlayer.getName(), BigDecimal.valueOf(amount), currencyName);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String world, double amount, String currencyName) {
        return this.has(offlinePlayer, amount, currencyName);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount, String currencyName) {
        com.BlockDynasty.api.EconomyResponse resultWithdraw = api.withdraw(offlinePlayer.getName(), BigDecimal.valueOf(amount), currencyName);
        if(resultWithdraw.isSuccess()){
            return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, "withdraw success for "+ offlinePlayer.getPlayer());
        }
        return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultWithdraw.getErrorMessage());
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String world, double amount, String currencyName) {
        return this.withdrawPlayer(offlinePlayer, amount, currencyName);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount, String currencyName) {
        com.BlockDynasty.api.EconomyResponse resultDeposit = api.deposit(offlinePlayer.getName(), BigDecimal.valueOf(amount),currencyName);
        if(resultDeposit.isSuccess()){
            return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, "Deposit success for "+ offlinePlayer.getPlayer());
        }
        return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultDeposit.getErrorMessage());

    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String world, double amount, String currencyName) {
        return this.depositPlayer(offlinePlayer, amount, currencyName);
    }

    @Override
    public EconomyResponse bankBalance(String name, String currencyName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount, String currencyName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount, String currencyName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount, String currencyName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }
}
