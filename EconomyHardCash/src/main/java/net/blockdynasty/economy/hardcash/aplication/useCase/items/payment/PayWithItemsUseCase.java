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

package net.blockdynasty.economy.hardcash.aplication.useCase.items.payment;

import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.IDepositUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.IPayUseCase;
import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.events.Context;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.ContextualTask;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.IScheduler;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.service.CacheCurrencyItems;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.service.ItemBase64Creator;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.balance.IGetItemsBalanceUseCase;
import net.blockdynasty.economy.hardcash.domain.entity.currency.ItemStackCurrency;
import net.blockdynasty.economy.hardcash.domain.entity.platform.HardCashCreator;
import net.blockdynasty.economy.hardcash.domain.entity.player.IEntityHardCash;
import net.blockdynasty.economy.hardcash.domain.service.ItemCreator;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PayWithItemsUseCase implements IPayWithItemsUseCase{
    private IDepositUseCase depositUseCase;
    private IGetItemsBalanceUseCase getItemsBalanceUseCase;
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private CacheCurrencyItems cacheCurrencyItems;
    private ItemCreator itemCreator;
    private HardCashCreator platform;
    private IPayUseCase payUseCase;
    private IScheduler scheduler;

    private static final Set<UUID> activeTransactions = ConcurrentHashMap.newKeySet();

    public PayWithItemsUseCase(HardCashCreator platform, SearchCurrencyUseCase searchCurrencyUseCase,
                               IPayUseCase payUseCase, IDepositUseCase depositUseCase, IGetItemsBalanceUseCase getItemsBalanceUseCase, CacheCurrencyItems cacheCurrencyItems) {
        this.depositUseCase = depositUseCase;
        this.getItemsBalanceUseCase = getItemsBalanceUseCase;
        this.cacheCurrencyItems = cacheCurrencyItems;
        this.scheduler = platform.getScheduler();
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.payUseCase = payUseCase;
        this.itemCreator = new ItemBase64Creator(platform);
    }


    @Override
    public void execute(IEntityHardCash player, String targetPlayerName, ICurrency currency, int cantItems) {
        if (!activeTransactions.add(player.getUniqueId())) {
            player.sendMessage("You already have a transaction in progress. Please wait.");
            return;
        }
        Runnable r = () ->{
            try {
                if(cantItems <= 0){
                    player.sendMessage("The amount of items must be greater than zero.");
                    return;
                }
                Result<Money> resultBalance = getItemsBalanceUseCase.execute(player, currency);
                if(!resultBalance.isSuccess())return;
                int playerItemsBalance = resultBalance.getValue().getAmount().intValue();
                if (playerItemsBalance < cantItems) {
                    player.sendMessage("You don't have enough items to make the payment.");
                    return;
                }

                if(!currency.isTransferable()){
                    player.sendMessage("This currency is not transferable.");
                    return;
                }
                if (!currency.isPhysicalItemSupported()){
                    player.sendMessage("This currency does not support physical item transactions.");
                    return;
                }

                CacheCurrencyItems.Currencywrapper wrapper = cacheCurrencyItems.getItem(currency.getUuid());
                if (wrapper == null){
                    player.sendMessage("Currency does not have a valid item representation.");
                    return;
                }

                ItemStackCurrency itemCurrency = wrapper.getItem();
                if (itemCurrency.isNull()){
                    player.sendMessage("Currency does not have a valid item representation.");
                    return;
                }

                if(!player.takeItems(itemCurrency,cantItems)) {
                    player.sendMessage("Failed to take items from inventory. Transaction cancelled.");
                    return;
                }
                Result<Void> resultDeposit= depositUseCase.execute(player.getUniqueId(),currency.getSingular(), BigDecimal.valueOf(cantItems), Context.SYSTEM);

                if(resultDeposit.isSuccess()){
                    Result<Void> resultPay=payUseCase.execute(player.getName(),targetPlayerName,currency.getSingular(), BigDecimal.valueOf(cantItems));
                    if (!resultPay.isSuccess()) {
                        player.sendMessage("Payment failed.");
                    }
                }else{
                    scheduler.run(ContextualTask.build(()->{
                        itemCurrency.setCantity(cantItems);
                        player.giveItem(itemCurrency);
                        player.sendMessage("Payment failed.");
                        }, player));
                }
            }finally {
                activeTransactions.remove(player.getUniqueId());
            }
        };

        ContextualTask executor = ContextualTask.build(r,player);
        scheduler.runAsync(executor);
    }
}
