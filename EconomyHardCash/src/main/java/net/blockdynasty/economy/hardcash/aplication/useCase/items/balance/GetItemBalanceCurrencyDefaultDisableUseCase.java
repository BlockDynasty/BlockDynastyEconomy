package net.blockdynasty.economy.hardcash.aplication.useCase.items.balance;

import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.result.ErrorCode;
import net.blockdynasty.economy.core.domain.result.Result;

import java.util.UUID;

public class GetItemBalanceCurrencyDefaultDisableUseCase implements IGetItemBalanceCurrencyDefaultUseCase{
    @Override
    public Result<Money> execute(String playerName) {
        return Result.failure("Physical item Disabled", ErrorCode.NOT_IMPLEMENTED);
    }

    @Override
    public Result<Money> execute(UUID playerUUID) {
        return Result.failure("Physical item Disabled", ErrorCode.NOT_IMPLEMENTED);
    }
}
