package net.blockdynasty.economy.hardcash.aplication.useCase.items.balance;

import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.result.ErrorCode;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.service.CacheCurrencyItems;
import net.blockdynasty.economy.hardcash.domain.entity.platform.HardCashCreator;
import net.blockdynasty.economy.hardcash.domain.entity.player.IEntityHardCash;

import java.util.UUID;

public class GetItemBalanceCurrencyDefaultUseCase extends GetItemsBalanceUseCase implements IGetItemBalanceCurrencyDefaultUseCase{
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private HardCashCreator platform;

    public GetItemBalanceCurrencyDefaultUseCase(HardCashCreator platform, SearchCurrencyUseCase searchCurrencyUseCase,CacheCurrencyItems cacheCurrencyItems) {
        super( platform, searchCurrencyUseCase, cacheCurrencyItems);
        this.platform = platform;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    @Override
    public Result<Money> execute(String playerName) {
        Result<ICurrency> currency  = searchCurrencyUseCase.getDefaultCurrency();
        if (!currency.isSuccess()) {
            return Result.failure("currency not found", ErrorCode.CURRENCY_NOT_FOUND);
        }

        IEntityHardCash player = platform.getPlayer(playerName);
        if (player == null) {
            return Result.failure("Player not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        return super.execute(player, currency.getValue());
    }

    @Override
    public Result<Money> execute(UUID playerUUID) {
        Result<ICurrency> currency  = searchCurrencyUseCase.getDefaultCurrency();
        if (!currency.isSuccess()) {
            return Result.failure("currency not found", ErrorCode.CURRENCY_NOT_FOUND);
        }

        IEntityHardCash player = platform.getPlayerByUUID(playerUUID);
        if (player == null) {
            return Result.failure("Player not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        return super.execute(player, currency.getValue());
    }
}
