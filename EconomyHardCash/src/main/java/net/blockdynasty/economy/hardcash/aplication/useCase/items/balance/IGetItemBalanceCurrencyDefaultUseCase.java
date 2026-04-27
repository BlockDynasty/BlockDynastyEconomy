package net.blockdynasty.economy.hardcash.aplication.useCase.items.balance;

import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.result.Result;

import java.util.UUID;

public interface IGetItemBalanceCurrencyDefaultUseCase {
    Result<Money> execute(String playerName);
    Result<Money> execute(UUID playerUUID);
}
