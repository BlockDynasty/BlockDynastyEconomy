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

import java.math.BigDecimal;

@Entity
@Table(name = "balance")
public class BalanceDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private WalletDb wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", referencedColumnName = "uuid") // Referenciar UUID
    private CurrencyDb currency;

    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;

    public BalanceDb() {
    }

    public Long getId() {
        return this.id;
    }

    public void setWallet(WalletDb wallet) {
        this.wallet = wallet;
    }
    public WalletDb getWallet() {
        return this.wallet;
    }
    public void setCurrency(CurrencyDb currency) {
        this.currency = currency;
    }
    public CurrencyDb getCurrency() {
        return this.currency;
    }
    public  BigDecimal getAmount() {
        return this.amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}