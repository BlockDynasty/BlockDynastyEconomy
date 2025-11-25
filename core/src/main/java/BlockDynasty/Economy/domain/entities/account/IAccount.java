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
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.entities.wallet.Wallet;
import BlockDynasty.Economy.domain.result.Result;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IAccount {
    Result<Void> subtract(ICurrency currency, BigDecimal amount);
    Result<Void> add(ICurrency currency, BigDecimal amount);
    Result<Void> setMoney(ICurrency currency, BigDecimal amount);

    void setBalances(List<Money> wallet);
    boolean hasCurrency( String currencyName);
    Money getMoney(ICurrency currency);
    Money getMoney();
    Money getMoney(String currencyName);
    boolean hasEnough(ICurrency currency, BigDecimal amount);
    Wallet getWallet();
    void setWallet(Wallet wallet);
    boolean hasEnoughDefaultCurrency(BigDecimal amount);
    void setUuid(UUID uuid);
    void setNickname(String nickname);
    String getNickname();
    UUID getUuid();
    void setCanReceiveCurrency(boolean canReceiveCurrency);
    boolean canReceiveCurrency();
    Player getPlayer();
}
