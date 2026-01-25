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

package com.blockdynasty.economy.repository.Models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wallet")
public class WalletDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private List<AccountDb> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BalanceDb> balances = new ArrayList<>();

    public WalletDb() {
    }

    // Helper method for managing the relationship
    public void addBalance(BalanceDb balance) {
        balances.add(balance);
        balance.setWallet(this);
    }

    public void removeBalance(BalanceDb balance) {
        balances.remove(balance);
        balance.setWallet(null);
    }

    public void setAccount(AccountDb account) {
        accounts.add(account);
        account.setWallet(this);
    }

    public void removeAccount(AccountDb account) {
        accounts.remove(account);
        account.setWallet(null);
    }

    public List<AccountDb> getAccounts() {
        return accounts;
    }

    public Long getId() {
        return this.id;
    }

    public List<BalanceDb> getBalances() {
        return this.balances;
    }

    public void setBalances(List<BalanceDb> balances) {
        this.balances = balances;
    }

}
