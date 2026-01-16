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
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IDepositUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.NbtData;
import domain.entity.platform.HardCashCreator;
import domain.entity.player.IEntityHardCash;
import aplication.HardCashService;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositItemUseCase implements IDepositItemUseCase {
    private HardCashCreator platformHardCash;
    private IDepositUseCase depositUseCase;
    private SearchCurrencyUseCase searchCurrencyUseCase;

    public DepositItemUseCase(HardCashCreator platformHardCash, IDepositUseCase depositUseCase, SearchCurrencyUseCase searchCurrencyUseCase) {
        this.platformHardCash = platformHardCash;
        this.depositUseCase = depositUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    public void execute(IEntityHardCash player) {
        ItemStackCurrency item = player.takeHandItem();
        if(item == null){
            player.sendMessage("You must hold a currency item to deposit.");
            return;
        }
        NbtData nbtData = item.getNbtData();
        if (nbtData.getItemType() == null || nbtData.getUuidCurrency() == null) {
            player.sendMessage("Not have a valid currency item in hand.");
            return;
        }
        Result<ICurrency> result = searchCurrencyUseCase.getCurrency(nbtData.getItemType());
        if (!result.isSuccess()) {
            player.sendMessage("Currency type not found: " + nbtData.getItemType());
            return;
        }
        ICurrency currency = result.getValue();
        if(!currency.getUuid().equals(UUID.fromString(nbtData.getUuidCurrency()))) {
            player.sendMessage("Not have a valid currency item in hand.");
            return;
        }
        player.removeItem(item);

        BigDecimal value = new BigDecimal(nbtData.getValue());
        BigDecimal cant = new BigDecimal(item.getCantity());
        value = value.multiply(cant);

        Result<Void> depositResult = depositUseCase.execute(player.getUniqueId(),currency.getSingular(), value, Context.COMMAND);
        if (!depositResult.isSuccess()) {
            player.giveItem(item);
        }
    }
}

