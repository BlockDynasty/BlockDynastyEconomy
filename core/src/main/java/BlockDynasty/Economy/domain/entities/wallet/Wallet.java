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

package BlockDynasty.Economy.domain.entities.wallet;

import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Wallet implements IWallet {
    private List<Money> balances;

    public Wallet() {
        this.balances = new ArrayList<>();
    }

    public Wallet(List<Money> monies) {
        this.balances = monies;
    }

    public Wallet(Wallet wallet) {
        this.balances = new ArrayList<>();
        for (Money money : wallet.getBalances()) {
            this.balances.add(new Money(money));
        }
    }

    public boolean hasCurrency( String currencyName){
        return balances.stream().anyMatch(b ->
                b.getCurrency().getSingular().equals(currencyName) || b.getCurrency().getPlural().equals(currencyName));
    }

    public Money getMoney(ICurrency currency) {
        return balances.stream()
                .filter(b -> b.getCurrency().equals(currency))
                .findFirst()
                .orElse(null);
    }

    public Money getMoney(){
        return balances.stream()
                .filter(b -> b.getCurrency().isDefaultCurrency())
                .findFirst()
                .orElse(null);
    }

    public Money getMoney(String currencyName){
        return balances.stream()
                .filter(b -> b.getCurrency().getSingular().equalsIgnoreCase(currencyName) || b.getCurrency().getPlural().equalsIgnoreCase(currencyName))
                .findFirst()
                .orElse(null);
    }

    public void setBalances(List<Money> monies) {
        this.balances = monies;
    }

    public void createBalance(ICurrency currency, BigDecimal amount) {
        Money money = new Money(currency, amount);
        balances.add(money);
    }

    public List<Money> getBalances() {
        return balances;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "balances=" + balances +
                '}';
    }
}
