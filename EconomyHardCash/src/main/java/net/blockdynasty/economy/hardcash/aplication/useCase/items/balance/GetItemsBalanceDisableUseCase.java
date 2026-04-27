package net.blockdynasty.economy.hardcash.aplication.useCase.items.balance;

import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.result.ErrorCode;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.hardcash.domain.entity.player.IEntityHardCash;

import java.util.UUID;

public class GetItemsBalanceDisableUseCase implements IGetItemsBalanceUseCase {
    @Override
    public Result<Money> execute(IEntityHardCash player, ICurrency currency) {
        return Result.failure("Physical item Disabled", ErrorCode.NOT_IMPLEMENTED);
    }

    @Override
    public Result<Money> execute(String playerName, String currencyName) {
        return Result.failure("Physical item Disabled", ErrorCode.NOT_IMPLEMENTED);
    }

    @Override
    public Result<Money> execute(UUID playerUuid, String currencyName) {
        return Result.failure("Physical item Disabled", ErrorCode.NOT_IMPLEMENTED);
    }
}
