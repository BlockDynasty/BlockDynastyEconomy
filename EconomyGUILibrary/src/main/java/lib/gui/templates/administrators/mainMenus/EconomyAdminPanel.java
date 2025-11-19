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
import lib.gui.components.IEntityGUI;
import lib.util.materials.Materials;
import lib.gui.components.abstractions.AbstractPanel;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.util.colors.Message;

import java.util.Map;

public class EconomyAdminPanel extends AbstractPanel {
    private final IEntityGUI sender;

    public EconomyAdminPanel(IEntityGUI sender) {
        super(Message.process("EconomyAdminPanel.title"), 5,sender);
        this.sender = sender;

        initializeButtons();
    }

    private void initializeButtons() {
        setItem(20, createItem(Materials.EMERALD, Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)),"EconomyAdminPanel.button1.nameItem"),
                        Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"EconomyAdminPanel.button1.lore")),
                event -> {
            GUIFactory.currencyPanel( sender, this).open();
        });

        setItem(24, createItem(Materials.PLAYER_HEAD, Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)),"EconomyAdminPanel.button2.nameItem"),
                Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"EconomyAdminPanel.button2.lore")), event -> {
            GUIFactory.accountSelectorToEdit( sender, this).open();
        });

        setItem(40, createItem(Materials.BARRIER, Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"EconomyAdminPanel.button3.nameItem"),
                Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"EconomyAdminPanel.button3.lore")), event -> {
            this.close();
        });


    }
}