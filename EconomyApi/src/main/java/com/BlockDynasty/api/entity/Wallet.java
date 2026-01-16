package com.BlockDynasty.api.entity;

import java.util.List;

public class Wallet {
    private List<Money> balances;

    public Wallet(List<Money> balances) {
        this.balances = balances;
    }

    public Money getMoney(String CurrencyName){
        return balances.stream()
                .filter(b -> b.getCurrency().getSingular().equalsIgnoreCase(CurrencyName) || b.getCurrency().getPlural().equalsIgnoreCase(CurrencyName))
                .findFirst()
                .orElse(null);
    }

    public List<Money> getBalances() {
        return balances;
    }

}
