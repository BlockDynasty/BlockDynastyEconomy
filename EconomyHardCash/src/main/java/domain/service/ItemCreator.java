package domain.service;

import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import domain.entity.currency.ItemStackCurrency;

import java.math.BigDecimal;

public interface ItemCreator {
    ItemStackCurrency create(ICurrency currency, BigDecimal amount);
}
