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

package net.blockdynasty.economy.gui.gui.templates.users;

import net.blockdynasty.economy.core.aplication.useCase.transaction.balance.GetBalanceUseCase;
import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.ContextualTask;
import net.blockdynasty.economy.hardcash.aplication.useCase.HardCashUseCaseFactory;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.balance.IGetItemsBalanceUseCase;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.PlatformGUI;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.Button;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;
import net.blockdynasty.economy.gui.gui.components.generics.PaginatedPanel;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;
import net.blockdynasty.economy.libs.services.messages.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountBalance extends PaginatedPanel<Money> {
    private final GetBalanceUseCase getBalanceUseCase;
    private final Player targetPlayer;
    private final IEntityGUI sender;
    private final IGetItemsBalanceUseCase getItemsBalanceUseCase;
    private final PlatformGUI platform;

    public AccountBalance(IEntityGUI player, GetBalanceUseCase getBalanceUseCase, IGUI parent, PlatformGUI platform) {
        super(Message.process("AccountBalance.title"), 3, player, parent, 7); // 7 currencies per page
        this.getBalanceUseCase = getBalanceUseCase;
        this.sender = player;
        this.targetPlayer = new Player(player.getUniqueId(), player.getName());
        this.platform = platform;
        this.getItemsBalanceUseCase = HardCashUseCaseFactory.getItemsBalanceUseCase();

        loadBalances();
    }

    public AccountBalance(IEntityGUI sender, Player target, GetBalanceUseCase getBalanceUseCase, IGUI parent, PlatformGUI platform) {
        super(Message.process("AccountBalance.title"), 3, sender, parent, 7);
        this.getBalanceUseCase = getBalanceUseCase;
        this.targetPlayer = target;
        this.platform = platform;
        this.sender = sender;
        this.getItemsBalanceUseCase = HardCashUseCaseFactory.getItemsBalanceUseCase();

        loadBalances();
    }

    private void loadBalances() {
        showItemsPage(new ArrayList<>());
        this.createLoadingMessage();
        //async
        Runnable r= () -> {
            Result<List<Money>> result = getBalanceUseCase.getBalances(targetPlayer);
            if (result.isSuccess() && result.getValue() != null) {
                this.clearStatusMessage();
                showItemsPage(result.getValue());
            }
        };
        platform.getScheduler().runAsync(ContextualTask.build(r,sender));
    }

    @Override
    protected IItemStack createItemFor(Money money) {
        ICurrency currency = money.getCurrency();
        return Item.of(RecipeItem.builder()
                .setBase64Item(currency.getBase64Item())
                .setMaterial(Materials.match(currency.getMaterial()))
                .setName(Message.process(Map.of("currency",ChatColor.stringValueOf(currency.getColor()) + currency.getSingular()),"AccountBalance.button1.nameItem"))
                .setLore( Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE),
                        "currency",ChatColor.stringValueOf(currency.getColor()) + money.format()),"AccountBalance.button1.lore"),
                        getItemBalance(sender,currency))
                .setTexture(currency.getTexture())
                .build());
    }
    private String getItemBalance(IEntityGUI sender, ICurrency currency) {
        if (!currency.isPhysicalItemSupported()) return "";
        Result<Money> result= getItemsBalanceUseCase.execute(sender.asEntityHardCash(), currency);
        if (!result.isSuccess() ) return "";
        int cant = result.getValue().getAmount().intValue();
        return ChatColor.stringValueOf(Colors.WHITE)+"Inventory: "+ ChatColor.stringValueOf(currency.getColor())+cant;
    }

    @Override
    protected IItemStack createEmptyMessage() {
        return Item.of(RecipeItem.builder()
                .setMaterial(Materials.BARRIER)
                .setName(Message.process(Map.of("color",ChatColor.stringValueOf(Colors.RED)),"AccountBalance.button2.nameItem"))
                .setLore( Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)),"AccountBalance.button2.lore"))
                .build());
    }

    @Override
    protected void addCustomButtons() {
        setButton(4, Button.builder()
                .setItemStack( Item.of(RecipeItem.builder()
                        .setMaterial(Materials.BOOK)
                        .setName(Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GOLD)),"AccountBalance.button3.nameItem"))
                        .setLore( Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)),"AccountBalance.button3.lore"))
                        .build()))
                .build());
    }

    @Override
    public void refresh(){
        //GUIFactory.balancePanel(sender,targetPlayer,getParent()).open();
        loadBalances();
    }
}