package com.BlockDynasty.api.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Account {
    private String name;
    private UUID uuid;
    private Wallet wallet;
    private boolean canReceiveCurrency;
    private boolean blocked;

    public Account(String name, UUID uuid, Wallet wallet, boolean canReceiveCurrency, boolean blocked) {
        this.name = name;
        this.uuid = uuid;
        this.wallet = wallet;
        this.canReceiveCurrency = canReceiveCurrency;
        this.blocked = blocked;
    }

    public BigDecimal getBalance(String currencyName) {
        Money money = wallet.getMoney(currencyName);
        if (money != null) {
            return money.getAmount();
        }
        return BigDecimal.ZERO;

    }
    public List<Money> getBalances() {
        return wallet.getBalances();
    }

    public String getName() {
        return name;
    }
    public UUID getUuid() {
        return uuid;
    }
    public Wallet getWallet() {
        return wallet;
    }
    public boolean canReceiveCurrency() {
        return canReceiveCurrency;
    }
    public boolean isBlocked() {
        return blocked;
    }
}
