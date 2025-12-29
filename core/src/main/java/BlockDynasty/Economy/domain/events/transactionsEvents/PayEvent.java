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

public class PayEvent  extends Event {
    private final Currency currency;
    private final Player payer;
    private final Player received;
    private final BigDecimal amount;

    public PayEvent(Player player, Player received, ICurrency currency, BigDecimal amount ) {
            this.currency = Currency.builder().copy(currency).build();;
            this.payer = new Player(player);
            this.received = new Player(received);
            this.amount = amount;
        }

    public ICurrency getCurrency() {
        return currency;
    }

    public Player getPayer() {
        return payer;
    }

    public Player getReceived() {
        return received;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "PayEvent{" +
                "currency=" + currency.getSingular() +
                ", payer sender=" + payer.getNickname() +
                ", player receiver=" + received.getNickname() +
                ", amount=" + amount.toString() +
                '}';
    }

}
