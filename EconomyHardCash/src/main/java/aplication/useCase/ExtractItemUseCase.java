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

package aplication.useCase;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IWithdrawUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import aplication.HardCashService;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.NbtData;
import domain.entity.currency.RecipeItemCurrency;
import domain.entity.platform.HardCashCreator;
import domain.entity.player.IEntityHardCash;
import abstractions.platform.materials.Materials;
import domain.service.ItemCreator;
import domain.service.ItemCreatorFactory;
import util.colors.ChatColor;
import util.colors.Colors;

import java.math.BigDecimal;

public class ExtractItemUseCase implements IExtractItemUseCase {
    private HardCashCreator platform;
    private IWithdrawUseCase withdrawUseCase;
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private ItemCreator itemCreator;

    public ExtractItemUseCase(HardCashCreator platform, IWithdrawUseCase withdrawUseCase, SearchCurrencyUseCase searchCurrencyUseCase) {
        this.platform = platform;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.withdrawUseCase = withdrawUseCase;
        this.itemCreator = ItemCreatorFactory.getItemCreator(platform);
    }

    public void execute(IEntityHardCash player, BigDecimal amount, String currency){
        Result<ICurrency> currencyResult = searchCurrencyUseCase.getCurrency(currency);
        if (!currencyResult.isSuccess()) {
            player.sendMessage("Error."+ currencyResult.getErrorMessage());
            return;
        }
        ICurrency currencyData = currencyResult.getValue();
        ItemStackCurrency item = itemCreator.create(currencyData, amount);
        if (player.hasItem(item) || player.hasEmptySlot() ){
            var withdrawResult = withdrawUseCase.execute(player.getUniqueId(),currency, amount, Context.COMMAND);
            if (withdrawResult.isSuccess()){
                player.giveItem(item);
            }else {
                player.sendMessage("Error."+ withdrawResult.getErrorMessage());
            }
        }else  {
            player.sendMessage("Error."+ "Not enough space in inventory.");
        }
    }
}
