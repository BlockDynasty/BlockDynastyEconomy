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

package lib.gui.templates.administrators.mainMenus;

import lib.gui.GUIFactory;
import lib.gui.components.IGUI;
import lib.gui.components.IEntityGUI;
import lib.util.materials.Materials;
import lib.gui.components.abstractions.AbstractPanel;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.util.colors.Message;

import java.util.Map;

public class CurrencyAdminPanel extends AbstractPanel {
    private final IEntityGUI player;

    public CurrencyAdminPanel(IEntityGUI player, IGUI parent) {
        super(Message.process("CurrencyAdminPanel.title"), 3,player,parent);
        this.player = player;
        setupGUI();
    }

    private void setupGUI() {
        // Create Currency button
        setItem(10, createItem(Materials.EMERALD, Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)),"CurrencyAdminPanel.button1.nameItem"),
                Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"CurrencyAdminPanel.button1.lore")), unused -> {
            GUIFactory.createCurrencyPanel(player,this);

        });

        // Delete Currency button
        setItem(12, createItem(Materials.REDSTONE, Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"CurrencyAdminPanel.button2.nameItem"),
                Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"CurrencyAdminPanel.button2.lore")), unused -> {
            GUIFactory.currencyListToDeletePanel(player, this).open();
        });

        // Edit Currency button
        setItem(14, createItem(Materials.BOOK, Message.process(Map.of("color", ChatColor.stringValueOf(Colors.YELLOW)),"CurrencyAdminPanel.button3.nameItem"),
                Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"CurrencyAdminPanel.button3.lore")), unused -> {
            GUIFactory.currencyListToEditPanel(player, this).open();
        });

        // Toggle Features button
        setItem(16, createItem(Materials.PAPER, Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)),"CurrencyAdminPanel.button4.nameItem"),
                Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"CurrencyAdminPanel.button4.lore")), unused -> {
            player.sendMessage("[Bank] This feature is not implemented yet.");
        });

        // Exit button
        setItem(22, createItem(Materials.BARRIER, Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"CurrencyAdminPanel.button5.nameItem"),
                Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"CurrencyAdminPanel.button5.lore")), unused -> {
            this.openParent();
        });
    }

}