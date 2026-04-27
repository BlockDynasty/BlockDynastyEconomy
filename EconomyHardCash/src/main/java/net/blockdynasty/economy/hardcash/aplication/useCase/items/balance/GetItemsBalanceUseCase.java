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

package net.blockdynasty.economy.hardcash.aplication.useCase.items.balance;

import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.result.ErrorCode;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.service.ItemBase64Creator;
import net.blockdynasty.economy.hardcash.domain.entity.currency.ItemStackCurrency;
import net.blockdynasty.economy.hardcash.domain.entity.platform.HardCashCreator;
import net.blockdynasty.economy.hardcash.domain.entity.player.IEntityHardCash;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.service.CacheCurrencyItems;
import net.blockdynasty.economy.hardcash.domain.service.ItemCreator;

import java.math.BigDecimal;
import java.util.UUID;

public class GetItemsBalanceUseCase implements IGetItemsBalanceUseCase {
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private HardCashCreator platform;
    private ItemCreator itemCreator;
    private CacheCurrencyItems cacheCurrencyItems;

    public GetItemsBalanceUseCase(HardCashCreator platform, SearchCurrencyUseCase searchCurrencyUseCase, CacheCurrencyItems cacheCurrencyItems) {
        this.platform = platform;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.cacheCurrencyItems = cacheCurrencyItems;
        this.itemCreator = new ItemBase64Creator(platform);
    }

    @Override
    public Result<Money> execute(IEntityHardCash player, ICurrency currency) {
        if (!currency.isPhysicalItemSupported()){
             return Result.failure("Not Physical support", ErrorCode.CURRENCY_NOT_FOUND);
        }

        if (currency.getBase64Item() == null || currency.getBase64Item().isEmpty()) {
             return Result.failure("Currency not found", ErrorCode.CURRENCY_NOT_FOUND);
        }
        CacheCurrencyItems.Currencywrapper wrapper = cacheCurrencyItems.getItem(currency.getUuid());
        if (wrapper == null) {
            return Result.failure("Currency not found", ErrorCode.CURRENCY_NOT_FOUND);
        }
        ItemStackCurrency itemCurrency = wrapper.getItem();
        if (itemCurrency.isNull()) return Result.failure("Currency not found", ErrorCode.CURRENCY_NOT_FOUND);
        return Result.success(new Money(currency, BigDecimal.valueOf(player.countItems(itemCurrency))));
    }

    @Override
    public Result<Money> execute(String playerName, String currencyName) {
        IEntityHardCash player = this.platform.getPlayer(playerName);
        if (player == null) {
            return Result.failure("Player not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Result<ICurrency> currencyResult = searchCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure("Currency not found", ErrorCode.CURRENCY_NOT_FOUND);
        }


        return execute(player, currencyResult.getValue());
    }

    @Override
    public Result<Money> execute(UUID playerUuid, String currencyName) {
        IEntityHardCash player = this.platform.getPlayerByUUID(playerUuid);
        if (player == null) {
            return Result.failure("Player not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Result<ICurrency> currencyResult = searchCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure("Currency not found", ErrorCode.CURRENCY_NOT_FOUND);
        }

        return execute(player, currencyResult.getValue());
    }
}
