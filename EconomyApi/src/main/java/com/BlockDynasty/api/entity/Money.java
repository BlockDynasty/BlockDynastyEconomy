package com.BlockDynasty.api.entity;

import java.math.BigDecimal;

public class Money {
    private Currency currency;
    private BigDecimal amount;

    public Money(Currency currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }
    public BigDecimal getAmount() {
        return amount;
    }
}
