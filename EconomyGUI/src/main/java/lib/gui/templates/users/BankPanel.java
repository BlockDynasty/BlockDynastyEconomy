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

import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByPlayerUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.GUIFactory;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.factory.Item;
import lib.gui.components.generics.Button;
import abstractions.platform.recipes.RecipeItem;
import abstractions.platform.materials.Materials;
import lib.gui.components.generics.AbstractPanel;
import lib.gui.templates.users.Cashmachine.MachinePanel;
import util.colors.ChatColor;
import util.colors.Colors;
import services.messages.Message;

import java.util.HashMap;
import java.util.Map;


public class BankPanel extends AbstractPanel {
    private final IEntityGUI player;
    private GetAccountByPlayerUseCase getAccount;
    private ITextInput textInput;
    private static final Map<Integer, Boolean> buttonsEnabled = new HashMap<>();

    public BankPanel(IEntityGUI player, GetAccountByPlayerUseCase getAccount, ITextInput textInput) {
        super(Message.process("BankPanel.title") +" ["+player.getName()+"]", 4, player);
        this.player = player;
        this.getAccount = getAccount;
        this.textInput = textInput;
        setupGUI();
    }

    private void setupGUI() {

        if(isButtonEnabled(4)) {
            Result<Account> account = getAccount.execute(new Player(player.getUniqueId(), player.getName()));
            boolean isBlocked = false;
            if (account.isSuccess()){
                isBlocked = account.getValue().isBlocked();
            }

            setButton(4, Button.builder()
                    .setItemStack(isBlocked
                            ? Item.of(RecipeItem.builder()
                            .setMaterial(Materials.RED_CONCRETE)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"BankPanel.button1.nameItem1"))
                            .setLore( Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button1.lore"))
                            .build())
                            : Item.of(RecipeItem.builder()
                            .setMaterial(Materials.LIME_CONCRETE)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)),"BankPanel.button1.nameItem2"))
                            .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button1.lore"))
                            .build()))
                    .build());
        }

        if (isButtonEnabled(10)){
            setButton(10,
                    Button.builder()
                    .setItemStack(
                            Item.of(RecipeItem.builder()
                                    .setMaterial(Materials.EMERALD)
                                    .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button11.nameItem"))
                                    .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button11.lore"))
                            .build()))
                    .setLeftClickAction( sender -> {
                        new MachinePanel(sender,this).open();
                    })
                    .build());
        }

        if (isButtonEnabled(11)) {
            setButton(11,Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.DIAMOND)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button2.nameItem"))
                            .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button2.lore"))
                            .build()))
                    .setLeftClickAction( sender -> {
                        if(sender.hasPermission("BlockDynastyEconomy.players.exchange")){
                            GUIFactory.exchangeFirstPanel(sender,this).open();
                        }else{
                            sender.sendMessage(ChatColor.stringValueOf(Colors.RED)+"You don't have permission");
                        }
                    })
                    .build());
        }

        if (isButtonEnabled(24)) {
            setButton(24, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.WRITABLE_BOOK)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "BankPanel.button3.nameItem"))
                            .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "BankPanel.button3.lore"))
                            .build()))
                    .setLeftClickAction(sender -> {
                        if(sender.hasPermission("BlockDynastyEconomy.players.offer")){
                            GUIFactory.listPlayerOnlineToOffer(sender, this).open();
                        }else {
                            sender.sendMessage(ChatColor.stringValueOf(Colors.RED)+"You don't have permission");
                        }
                    })
                    .build());
        }

        if (isButtonEnabled(25)) {
            setButton(25, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.WRITABLE_BOOK)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "BankPanel.button4.nameItem"))
                            .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "BankPanel.button4.lore"))
                            .build()))
                    .setLeftClickAction(sender -> {
                        if(sender.hasPermission("BlockDynastyEconomy.players.offer")) {
                            GUIFactory.listPlayersOfflineToOffer(sender, this).open();
                        }else {
                            sender.sendMessage(ChatColor.stringValueOf(Colors.RED)+"You don't have permission");
                        }
                    })
                    .build());
        }

        if (isButtonEnabled(22)) {
            setButton(22, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.ENDER_CHEST)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "BankPanel.button5.nameItem"))
                            .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "BankPanel.button5.lore"))
                            .build()))
                    .setLeftClickAction(sender -> {
                        if(sender.hasPermission("BlockDynastyEconomy.players.offer")) {
                            GUIFactory.myActiveOffers(sender, this).open();
                        }else{
                            sender.sendMessage(ChatColor.stringValueOf(Colors.RED)+"You don't have permission");
                        }
                    })
                    .build());
        }

        if (isButtonEnabled(13)) {
            setButton(13, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.BOOK)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "BankPanel.button6.nameItem"))
                            .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "BankPanel.button6.lore"))
                            .build()))
                    .setLeftClickAction(sender -> {
                        if(sender.hasPermission("BlockDynastyEconomy.players.balance")){
                        GUIFactory.balancePanel(sender, this).open();
                        }else{
                            sender.sendMessage(ChatColor.stringValueOf(Colors.RED)+"You don't have permission");
                        }
                    })
                    .build());
        }

        if (isButtonEnabled(20)) {
            setButton(20, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.CHEST)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "BankPanel.button7.nameItem"))
                            .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "BankPanel.button7.lore"))
                            .build()))
                    .setLeftClickAction(sender -> {
                        if(sender.hasPermission("BlockDynastyEconomy.players.offer")) {
                            GUIFactory.receivedOffers(player, this).open();
                        }else{
                            sender.sendMessage(ChatColor.stringValueOf(Colors.RED)+"You don't have permission");
                        }
                    })
                    .build());
        }
        if (isButtonEnabled(15)) {
            setButton(15, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.PLAYER_HEAD)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "BankPanel.button8.nameItem"))
                            .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "BankPanel.button8.lore"))
                            .build()))
                    .setLeftClickAction(sender -> {
                        if(sender.hasPermission("BlockDynastyEconomy.players.pay")){
                        GUIFactory.listPlayersOnline(sender, this).open();
                        }else{
                            sender.sendMessage(ChatColor.stringValueOf(Colors.RED)+"You don't have permission");
                        }
                    })
                    .build());
        }

        if (isButtonEnabled(16)) {
            setButton(16, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.PLAYER_HEAD)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "BankPanel.button9.nameItem"))
                            .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "BankPanel.button9.lore"))
                            .build()))
                    .setLeftClickAction(sender -> {
                        if(sender.hasPermission("BlockDynastyEconomy.players.pay")) {
                            GUIFactory.listPlayersFromDb(sender, this).open();
                        }else{
                            sender.sendMessage(ChatColor.stringValueOf(Colors.RED)+"You don't have permission");
                        }
                    })
                    .build());
        }

        if (isButtonEnabled(31)) {
            setButton(31, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.BARRIER)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)), "BankPanel.button10.nameItem"))
                            .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "BankPanel.button10.lore"))
                            .build()))
                    .setLeftClickAction(sender -> this.close())
                    .build());
        }
    }

    @Override
    public void refresh() {
        setupGUI();
    }

    public static boolean isButtonEnabled(int slot) {
        return buttonsEnabled.getOrDefault(slot, true);
    }
    public static void setButtonsState(Map<Integer, Boolean> buttonsState) {
        buttonsEnabled.putAll(buttonsState);
    }
    public static void switchButtonState(int slot) {
        boolean currentState = buttonsEnabled.getOrDefault(slot, true);
        buttonsEnabled.put(slot, !currentState);
    }
}