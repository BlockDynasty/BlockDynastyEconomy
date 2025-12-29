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

package BlockDynasty.Economy.domain.events.transactionsEvents;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Event;

import java.math.BigDecimal;

public class ExchangeEvent extends Event {
    private final Player player;
    private final Currency fromCurrency;
    private final Currency toCurrency;
    private final BigDecimal amount;
    private final double exchangeRate;
    private final BigDecimal exchangedAmount;

    public ExchangeEvent(Player player, ICurrency fromCurrency, ICurrency toCurrency, BigDecimal amount, double exchangeRate, BigDecimal exchangedAmount) {
        this.player = new Player(player);
        this.fromCurrency = Currency.builder().copy(fromCurrency).build();;
        this.toCurrency = Currency.builder().copy(toCurrency).build();;
        this.amount = amount;
        this.exchangeRate = exchangeRate;
        this.exchangedAmount = exchangedAmount;
    }

    public Player getPlayer() {
        return player;
    }
    public ICurrency getFromCurrency() {
        return fromCurrency;
    }
    public ICurrency getToCurrency() {
        return toCurrency;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public double getExchangeRate() {
        return exchangeRate;
    }
    public BigDecimal getExchangedAmount() {
        return exchangedAmount;
    }

    @Override
    public String toString() {
        return "ExchangeEvent{" +
                "player=" + player.getNickname() +
                ", fromCurrency=" + fromCurrency.getSingular() +
                ", toCurrency=" + toCurrency.getSingular() +
                ", amount=" + amount.toString() +
                ", exchangeRate=" + exchangeRate +
                ", exchangedAmount=" + exchangedAmount.toString() +
                '}';
    }
}
