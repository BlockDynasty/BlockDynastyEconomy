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

public class TransferEvent extends SerializableEvent {
    private final Player fromPlayer;
    private final Player toPlayer;
    private final Currency currency;
    private final BigDecimal amount;

    public TransferEvent(Player fromPlayer, Player toPlayer, ICurrency currency, BigDecimal amount) {
        this.fromPlayer = new Player(fromPlayer);
        this.toPlayer = new Player(toPlayer);
        this.currency = Currency.builder().copy(currency).build();;
        this.amount = amount;
    }

    public Player getFromPlayer() {
        return fromPlayer;
    }

    public Player getToPlayer() {
        return toPlayer;
    }

    public ICurrency getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "TransferEvent{" +
                "player sender=" + fromPlayer.getNickname() +
                ", player receiver=" + toPlayer.getNickname() +
                ", currency=" + currency.getSingular() +
                ", amount=" + amount.toString() +
                '}';
    }

}
