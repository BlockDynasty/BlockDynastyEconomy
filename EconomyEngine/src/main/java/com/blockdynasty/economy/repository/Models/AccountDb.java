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

@Entity
@Table(name = "account")
public class AccountDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "nickname")
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private WalletDb wallet;

    @Column(name = "can_receive_currency")
    private boolean canReceiveCurrency;

    @Column(name = "block") //default false
    @org.hibernate.annotations.ColumnDefault("false")
    private boolean block;

    public AccountDb() {
    }

    //public void updateFromEntity(Account account) {
    //  // No need to update balances directly here since wallet is now separate
    //this.setNickname(account.getNickname());
    //this.setCanReceiveCurrency(account.canReceiveCurrency());
    //this.block = account.isBlocked();
    // UUID shouldn't change
    //}

    public void setWallet(WalletDb wallet) {
        this.wallet = wallet;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public WalletDb getWallet() {
        return this.wallet;
    }

    public boolean isCanReceiveCurrency() {
        return this.canReceiveCurrency;
    }
    public boolean isBlocked() {
        return this.block;
    }
    public void setBlock(boolean block) {
        this.block = block;
    }
    public void setCanReceiveCurrency(boolean canReceiveCurrency) {
        this.canReceiveCurrency = canReceiveCurrency;
    }
}
