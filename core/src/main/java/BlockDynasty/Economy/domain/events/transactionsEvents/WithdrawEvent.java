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
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.events.SerializableEvent;

import java.math.BigDecimal;

public class WithdrawEvent extends SerializableEvent {
    private final Context context;
    private final Currency currency;
    private final Player player;
    private final BigDecimal amount;

    public WithdrawEvent(Player player, ICurrency currency, BigDecimal amount) {
        this.currency = Currency.builder().copy(currency).build();;
        this.player = new Player(player);
        this.amount = amount;
        this.context = Context.OTHER;
    }
    public WithdrawEvent(Player player, ICurrency currency, BigDecimal amount, Context context) {
        this.context = context;
        this.currency =Currency.builder().copy(currency).build();;
        this.player = new Player(player);
        this.amount = amount;
    }

    public ICurrency getCurrency() {
        return currency;
    }

    public Player getPlayer() {
        return player;
    }
    public Context getContext() {
        return context;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "WithdrawEvent{" +
                "currency=" + currency.getSingular() +
                ", player=" + player.getNickname() +
                ", amount=" + amount.toString() +
                '}';
    }

}
