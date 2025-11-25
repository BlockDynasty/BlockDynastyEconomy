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

package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.account.balance.GetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.components.IGUI;
import lib.gui.components.IItemStack;
import lib.gui.components.IEntityGUI;
import lib.gui.components.RecipeItem;
import lib.util.materials.Materials;
import lib.gui.components.abstractions.PaginatedPanel;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.util.colors.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AccountBalance extends PaginatedPanel<Money> {
    private final GetBalanceUseCase getBalanceUseCase;
    private final UUID targetUUID;

    public AccountBalance(IEntityGUI player, GetBalanceUseCase getBalanceUseCase, IGUI parent) {
        super(Message.process("AccountBalance.title"), 3, player, parent, 7); // 7 currencies per page
        this.getBalanceUseCase = getBalanceUseCase;
        this.targetUUID = player.getUniqueId();

        loadBalances();
    }

    public AccountBalance(IEntityGUI sender, UUID target, GetBalanceUseCase getBalanceUseCase, IGUI parent) {
        super(Message.process("AccountBalance.title"), 3, sender, parent, 7);
        this.getBalanceUseCase = getBalanceUseCase;
        this.targetUUID = target;

        loadBalances();
    }

    private void loadBalances() {
        Result<List<Money>> result = getBalanceUseCase.getBalances(targetUUID);

        //TEST
        //List<Money> monies = new ArrayList<>();
        //for (int i=0; i < 45; i++) {
        //    monies.add(new Money(new Currency(UUID.randomUUID(),"test","test")));
        //}

        if (result.isSuccess() && result.getValue() != null) {
            showItemsPage(result.getValue());

        }else {showItemsPage( new ArrayList<>());}
    }

    @Override
    protected IItemStack createItemFor(Money money) {
        ICurrency currency = money.getCurrency();
        return createItem(RecipeItem.builder()
                .setMaterial(Materials.GOLD_INGOT)
                .setName(Message.process(Map.of("currency",ChatColor.stringValueOf(currency.getColor()) + currency.getSingular()),"AccountBalance.button1.nameItem"))
                .setLore( Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE),
                        "currency",ChatColor.stringValueOf(currency.getColor()) + money.format()),"AccountBalance.button1.lore"))
                .setTexture(currency.getTexture())
                .build());
    }

    @Override
    protected IItemStack createEmptyMessage() {
        return createItem(Materials.BARRIER, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.RED)),"AccountBalance.button2.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)),"AccountBalance.button2.lore"));
    }

    @Override
    protected void addCustomButtons() {
        setItem(4, createItem(Materials.BOOK, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GOLD)),"AccountBalance.button3.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)),"AccountBalance.button3.lore")), null);
    }
}