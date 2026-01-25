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

package BlockDynasty.BukkitImplementation.Integrations.vault;

import com.BlockDynasty.api.DynastyEconomy;
import com.blockdynasty.economy.Economy;
import net.blockdynasty.providers.services.ServiceProvider;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VaultHook extends AbstractEconomy {
    private static DynastyEconomy api;


    public VaultHook() {
        VaultHook.api = ServiceProvider.get(DynastyEconomy.class, service -> service.getId().equals(Economy.getApiWithVaultLoggerId()));
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "BlockDynastyEconomy";
    }

    @Override
    public String format(double amount) {
        return api.format(new BigDecimal(amount));
    }

    @Override
    public String currencyNamePlural() {
        return api.getDefaultCurrencyNamePlural();
    }

    @Override
    public String currencyNameSingular() {
        return api.getDefaultCurrencyNameSingular();
    }

    @Override
    public boolean has(String playerName, double amount) {  //El jugador X tiene X cantidad de "dinero default"
        return api.hasAmount(playerName, BigDecimal.valueOf(amount));
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return has(player.getName(), amount);
    }

    @Override
    public boolean hasAccount(String playerName) {
        return api.existAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return api.existAccount(player.getName());
    }

    //caso de uso obtener balance
    @Override
    public double getBalance(String playerName) {
        return api.getBalance(playerName).doubleValue();
    }

    //caso de uso obtener balance
    @Override
    public double getBalance(OfflinePlayer player) {
        return getBalance(player.getName());
    }

    //solo caso de uso withdraw
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdrawPlayer(player.getName(), amount);
    }

    //solo caso de uso deposit
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return  depositPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String player, double amount) {
        com.BlockDynasty.api.EconomyResponse resultWithdraw = api.withdraw(player, BigDecimal.valueOf(amount));
        if(resultWithdraw.isSuccess()){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "withdraw success for "+player);
        }
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultWithdraw.getErrorMessage());
    }

    @Override
    public EconomyResponse depositPlayer(String player, double amount) {
        com.BlockDynasty.api.EconomyResponse resultDeposit = api.deposit(player, BigDecimal.valueOf(amount));
        if(resultDeposit.isSuccess()){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "Deposit success for "+player);
        }
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultDeposit.getErrorMessage());
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return false;
        }
        return api.createAccount(player.getUniqueId(), playerName).isSuccess();
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }



    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }



    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    //crear cuenta bancaria
    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //borrar cuenta bancaria
    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //tiene x monto en el banco?
    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //extraer del banco
    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //depositar en el banco
    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //es propietario del banco?
    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //es miembro del banco?
    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //balance del banco de x persona
    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //obtener bancos o cuentas bancarias??????????
    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    //si soporta bancos
    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

}
