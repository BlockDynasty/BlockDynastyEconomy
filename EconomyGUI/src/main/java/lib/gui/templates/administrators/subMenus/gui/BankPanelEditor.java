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

package lib.gui.templates.administrators.subMenus.gui;

import lib.abstractions.IConfigurationGUI;
import lib.gui.GUIFactory;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.factory.Item;
import lib.gui.components.generics.AbstractPanel;
import lib.gui.components.generics.Button;
import abstractions.platform.recipes.RecipeItem;
import lib.gui.templates.users.BankPanel;
import services.messages.Message;
import util.colors.ChatColor;
import util.colors.Colors;
import abstractions.platform.materials.Materials;

import java.util.Map;

public class BankPanelEditor extends AbstractPanel {
    private final IEntityGUI player;
    private final IConfigurationGUI config;

    public BankPanelEditor(IEntityGUI player, IGUI parent, IConfigurationGUI config) {
        super("Switch buttons", 4, player,parent);
        this.config = config;
        this.player = player;
        setupGUI();
    }

    private void setupGUI() {

        setButton(4, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.LIME_CONCRETE)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)),"BankPanel.button1.nameItem2"))
                            .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button1.lore"))
                            .build()))
                    .build());

        setButton(10,Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.EMERALD)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button11.nameItem"))
                        .setLore(BankPanel.isButtonEnabled(10) ? ChatColor.stringValueOf(Colors.GREEN)+"Enabled" : ChatColor.stringValueOf(Colors.RED)+"Disabled")
                        .build()))
                .setLeftClickAction( f -> {
                    BankPanel.switchButtonState(10);
                    this.refresh();
                    config.saveButtonConfig(10,BankPanel.isButtonEnabled(10));
                })
                .build());


        setButton(11,Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.DIAMOND)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button2.nameItem"))
                            .setLore(BankPanel.isButtonEnabled(11) ? ChatColor.stringValueOf(Colors.GREEN)+"Enabled" : ChatColor.stringValueOf(Colors.RED)+"Disabled")
                            .build()))
                    .setLeftClickAction( f -> {
                        BankPanel.switchButtonState(11);
                        this.refresh();
                        config.saveButtonConfig(11,BankPanel.isButtonEnabled(11));
                    })
                    .build());

            setButton(24, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.WRITABLE_BOOK)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "BankPanel.button3.nameItem"))
                            .setLore(BankPanel.isButtonEnabled(24) ? ChatColor.stringValueOf(Colors.GREEN)+"Enabled" : ChatColor.stringValueOf(Colors.RED)+"Disabled")
                            .build()))
                    .setLeftClickAction(f -> {
                        BankPanel.switchButtonState(24);
                        this.refresh();
                        config.saveButtonConfig(24,BankPanel.isButtonEnabled(24));
                    })
                    .build());

            setButton(25, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.WRITABLE_BOOK)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "BankPanel.button4.nameItem"))
                            .setLore(BankPanel.isButtonEnabled(25) ? ChatColor.stringValueOf(Colors.GREEN)+"Enabled" : ChatColor.stringValueOf(Colors.RED)+"Disabled")
                            .build()))
                    .setLeftClickAction(f -> {
                        BankPanel.switchButtonState(25);
                        this.refresh();
                        config.saveButtonConfig(25,BankPanel.isButtonEnabled(25));
                    })
                    .build());

            setButton(22, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.ENDER_CHEST)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "BankPanel.button5.nameItem"))
                            .setLore(BankPanel.isButtonEnabled(22) ? ChatColor.stringValueOf(Colors.GREEN)+"Enabled" : ChatColor.stringValueOf(Colors.RED)+"Disabled")
                            .build()))
                    .setLeftClickAction(f -> {
                        BankPanel.switchButtonState(22);
                        this.refresh();
                        config.saveButtonConfig(22,BankPanel.isButtonEnabled(22));
                    })
                    .build());

            setButton(13, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.BOOK)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "BankPanel.button6.nameItem"))
                            .setLore(BankPanel.isButtonEnabled(13) ? ChatColor.stringValueOf(Colors.GREEN)+"Enabled" : ChatColor.stringValueOf(Colors.RED)+"Disabled")
                            .build()))
                    .setLeftClickAction(f -> {
                        BankPanel.switchButtonState(13);
                        this.refresh();
                        config.saveButtonConfig(13,BankPanel.isButtonEnabled(13));
                    })
                    .build());

            setButton(20, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.CHEST)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "BankPanel.button7.nameItem"))
                            .setLore(BankPanel.isButtonEnabled(20) ? ChatColor.stringValueOf(Colors.GREEN)+"Enabled" : ChatColor.stringValueOf(Colors.RED)+"Disabled")
                            .build()))
                    .setLeftClickAction(f -> {
                        BankPanel.switchButtonState(20);
                        this.refresh();
                        config.saveButtonConfig(20,BankPanel.isButtonEnabled(20));
                    })
                    .build());

            setButton(15, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.PLAYER_HEAD)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "BankPanel.button8.nameItem"))
                            .setLore(BankPanel.isButtonEnabled(15) ? ChatColor.stringValueOf(Colors.GREEN)+"Enabled" : ChatColor.stringValueOf(Colors.RED)+"Disabled")
                            .build()))
                    .setLeftClickAction(f -> {
                        BankPanel.switchButtonState(15);
                        this.refresh();
                        config.saveButtonConfig(15,BankPanel.isButtonEnabled(15));
                    })
                    .build());

            setButton(16, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.PLAYER_HEAD)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "BankPanel.button9.nameItem"))
                            .setLore(BankPanel.isButtonEnabled(16) ? ChatColor.stringValueOf(Colors.GREEN)+"Enabled" : ChatColor.stringValueOf(Colors.RED)+"Disabled")
                            .build()))
                    .setLeftClickAction(f -> {
                        BankPanel.switchButtonState(16);
                        this.refresh();
                        config.saveButtonConfig(16,BankPanel.isButtonEnabled(16));
                    })
                    .build());

            setButton(31, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                            .setMaterial(Materials.BARRIER)
                            .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)), "BankPanel.button10.nameItem"))
                            .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "BankPanel.button10.lore"))
                            .build()))
                    .setLeftClickAction(unused -> this.openParent())
                    .build());
    }

    @Override
    public void refresh() {
        GUIFactory.bankPanelEditor(player,getParent()).open();
    }
}
