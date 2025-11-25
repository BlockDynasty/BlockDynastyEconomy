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
import BlockDynasty.Economy.domain.events.SerializableEvent;

import java.math.BigDecimal;

//accountFrom, accountTo., currencyFrom, currencyTo, amountFrom, amountTo
public class TradeEvent extends SerializableEvent {
    private final Player fromPlayer;
    private final Player toPlayer;
    private final ICurrency currencyFrom;
    private final ICurrency currencyTo;
    private final BigDecimal amountFrom;
    private final BigDecimal amountTo;

    public TradeEvent(Player fromPlayer, Player toPlayer, ICurrency currencyFrom, ICurrency currencyTo, BigDecimal amountFrom, BigDecimal amountTo) {
        this.fromPlayer = new Player(fromPlayer);
        this.toPlayer = new Player(toPlayer);
        this.currencyFrom = Currency.builder().copy(currencyFrom).build();;
        this.currencyTo = Currency.builder().copy(currencyTo).build();;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
    }

    public Player getFromPlayer() {
        return fromPlayer;
    }
    public Player getToPlayer() {
        return toPlayer;
    }
    public ICurrency getCurrencyFrom() {
        return currencyFrom;
    }
    public ICurrency getCurrencyTo() {
        return currencyTo;
    }
    public BigDecimal getAmountFrom() {
        return amountFrom;
    }
    public BigDecimal getAmountTo() {
        return amountTo;
    }


    @Override
    public String toString() {
        return "TradeEvent{" +
                "fromPlayer=" + fromPlayer.getNickname() +
                ", toPlayer=" + toPlayer.getNickname() +
                ", currencyFrom=" + currencyFrom.getSingular() +
                ", currencyTo=" + currencyTo.getSingular() +
                ", amountFrom=" + amountFrom.toString() +
                ", amountTo=" + amountTo.toString() +
                '}';
    }
}
