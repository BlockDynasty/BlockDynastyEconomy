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

package BlockDynasty.Economy.domain.entities.account;

import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.entities.wallet.Wallet;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;
import java.util.*;

public class Account implements IAccount {
    private final Player player;
    private Wallet wallet;
    private boolean canReceiveCurrency;
    private boolean blocked;

    public Account(UUID uuid, String nickname) {
        this.player = new Player(uuid, nickname);
        this.wallet = new Wallet();
        this.blocked = false;
        this.canReceiveCurrency = true;
    }

    public Account(UUID uuid, String nickname, List<Money> moneyList, boolean canReceiveCurrency,boolean blocked) {
        this.player = new Player(uuid, nickname);
        this.wallet = new Wallet(moneyList);
        this.canReceiveCurrency = canReceiveCurrency;
        this.blocked = blocked;
    }

    public Account(UUID uuid, String nickname, Wallet wallet, boolean canReceiveCurrency, boolean blocked) {
        this.player = new Player(uuid, nickname);
        this.wallet = wallet;
        this.canReceiveCurrency = canReceiveCurrency;
        this.blocked = blocked;
    }

    public Account(Account account) {
        this.player = new Player(account.getPlayer());
        this.wallet = new Wallet(account.getWallet());
        this.canReceiveCurrency = account.canReceiveCurrency();
        this.blocked = account.isBlocked();
    }

    public Result<Void> subtract(ICurrency currency, BigDecimal amount){
        Money money = getMoney(currency);
        if (money == null) {
            return Result.failure("No balance found for currency" , ErrorCode.ACCOUNT_NOT_HAVE_BALANCE);
        }
        Result<Void> result = money.subtract(amount);
        if (!result.isSuccess()) {
            return result;
        }
        return Result.success(null);
    }

    public Result<Void> add(ICurrency currency, BigDecimal amount) {
        Money money = getMoney(currency);
        if (money == null) {
            return Result.failure("No balance found for currency" , ErrorCode.ACCOUNT_NOT_HAVE_BALANCE);
        } else {
            Result<Void> result = money.add(amount);
            if (!result.isSuccess()) {
                return result;
            }
        }
        return Result.success(null);
    }

    public Result<Void> setMoney(ICurrency currency, BigDecimal amount) {
        Money money = getMoney(currency);
        if (money == null) {
            createBalance(currency, amount);
        }else{
            Result<Void> result = money.setAmount(amount);
            if (!result.isSuccess()) {
                return result;
            }
        }
        return Result.success(null);
    }

    public void setBalances(List<Money> monies) {
        this.wallet.setBalances(monies);
    }
    public List<Money> getBalances() {
        return wallet.getBalances();
    }

    public boolean hasCurrency( String currencyName){
        return wallet.hasCurrency(currencyName);
    }

    public Money getMoney(ICurrency currency) {
        return wallet.getMoney(currency);
    }

    public Money getMoney(){
        return wallet.getMoney();
    }

    public Money getMoney(String currencyName){
        return wallet.getMoney(currencyName);
    }

    public boolean hasEnough(ICurrency currency, BigDecimal amount){
        Money money = getMoney(currency);
        if (money == null) {
            return false;
        }
        return money.hasEnough(amount);
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public boolean hasEnoughDefaultCurrency(BigDecimal amount){
        Money money = getMoney();
        if (money == null) {
            return false;
        }
        return money.hasEnough(amount);
    };

    private void createBalance(ICurrency currency, BigDecimal amount) {
        wallet.createBalance(currency, amount);
    }

    public void setUuid(UUID uuid) {
        this.player.setUuid(uuid);
    }
    public void setNickname(String nickname) {
        this.player.setNickname(nickname);
    }

    public String getNickname() {
        return player.getNickname();
    }
    public UUID getUuid() {
        return player.getUuid();
    }

    public void setCanReceiveCurrency(boolean canReceiveCurrency) {
        this.canReceiveCurrency = canReceiveCurrency;
    }
    public boolean canReceiveCurrency() {
        return canReceiveCurrency;
    }
    public boolean isBlocked() {
        return blocked;
    }
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
    public void block(){
        this.blocked = true;
    }
    public void unblock(){
        this.blocked = false;
    }
    public Player getPlayer() {
        return player;
    }
    @Override
    public String toString() {
        return "Account{" +
                "player=" + player +
                ", wallet=" + wallet +
                ", canReceiveCurrency=" + canReceiveCurrency +
                '}';
    }
}