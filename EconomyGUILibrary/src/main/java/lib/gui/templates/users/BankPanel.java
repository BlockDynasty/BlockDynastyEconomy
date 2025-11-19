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

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.GUIFactory;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.util.materials.Materials;
import lib.gui.components.abstractions.AbstractPanel;
import lib.gui.templates.users.Offers.ListPlayerOnlineToOffer;
import lib.gui.templates.users.Offers.ListPlayersOfflineToOffer;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.util.colors.Message;

import java.util.Map;


public class BankPanel extends AbstractPanel {
    private final IEntityGUI player;
    private final ITextInput textInput;
    private final UseCaseFactory useCaseFactory;

    public BankPanel(IEntityGUI player, UseCaseFactory useCaseFactory, ITextInput textInput) {
        super(Message.process("BankPanel.title") +" ["+player.getName()+"]", 4, player);
        this.player = player;
        this.useCaseFactory = useCaseFactory;
        this.textInput = textInput;

        setupGUI();
    }

    private void setupGUI() {
        Result<Account> account = useCaseFactory.searchAccountByUUID().execute(player.getUniqueId());
        boolean isBlocked = false;
        if (account.isSuccess()){
            isBlocked = account.getValue().isBlocked();
        }
        setItem(4, isBlocked
                        ? createItem(Materials.RED_CONCRETE,
                        Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"BankPanel.button1.nameItem1"),
                        Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button1.lore"))
                        : createItem(Materials.LIME_CONCRETE,
                        Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)),"BankPanel.button1.nameItem2"),
                        Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button1.lore")),
                null);
        setItem(11,createItem(Materials.DIAMOND,
                        Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button2.nameItem"),
                        Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button2.lore")),
                unused -> {
                    GUIFactory.exchangeFirstPanel(player,this).open();
                });
        setItem(24, createItem(Materials.WRITABLE_BOOK,
                        Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button3.nameItem"),
                        Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button3.lore")),
                f -> {
                    new ListPlayerOnlineToOffer(player,this,textInput).open();
                });
        setItem(25, createItem(Materials.WRITABLE_BOOK,
                Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button4.nameItem"),
                Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button4.lore")),
                f -> {
                    new ListPlayersOfflineToOffer(player,this,useCaseFactory.searchAccountByName(),useCaseFactory.searchOfflineAccounts(),textInput).open();
                });
        setItem(22,createItem(Materials.ENDER_CHEST,
                        Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button5.nameItem"),
                        Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button5.lore")),
                f -> {
                    GUIFactory.myActiveOffers(player,this).open();
                });;
        setItem(13, createItem(Materials.BOOK,
                        Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button6.nameItem"),
                        Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button6.lore")),
                unused -> {
                    GUIFactory.balancePanel(player,this).open();
                });
        setItem(20,createItem(Materials.CHEST,
                        Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button7.nameItem"),
                        Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button7.lore")),
                f -> {
                    GUIFactory.receivedOffers(player,this).open();
                });
        setItem(15, createItem(Materials.PLAYER_HEAD,
                        Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button8.nameItem"),
                        Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button8.lore")),
                unused -> {
                    GUIFactory.listPlayersOnline(player,this).open();
                });
        setItem(16,createItem(Materials.PLAYER_HEAD,
                        Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button9.nameItem"),
                        Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button9.lore")),
                f->{
                    GUIFactory.listPlayersFromDb(player,this).open();
                });
        setItem(31, createItem(Materials.BARRIER,
                        Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"BankPanel.button10.nameItem"),
                        Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button10.lore")),
                unused -> this.close());
    }
}