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

package lib.gui.templates.administrators.subMenus.currencies;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import lib.gui.GUIFactory;
import lib.gui.components.*;
import lib.gui.components.abstractions.AbstractPanel;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.util.materials.Materials;

public class ColorSelectionPanel extends AbstractPanel {
    private final IEntityGUI player;
    private final ICurrency currency;
    private final EditCurrencyUseCase editCurrencyUseCase;
    private final ITextInput textInput;

    public ColorSelectionPanel(IEntityGUI player, ICurrency currency, EditCurrencyUseCase editCurrencyUseCase, IGUI parent, ITextInput textInput) {
        super("Select color", 5,player,parent);
        this.player = player;
        this.currency = currency;
        this.editCurrencyUseCase = editCurrencyUseCase;
        this.textInput = textInput;
        setupColorGUI();
    }

    private void setupColorGUI() {
        setItem(4, createItem(Materials.PAPER,"List Vanilla Colors","These colors ensure maximum compatibility between versions","Additionally you can enter Hexadecimal color for modern versions"), null);
        setItem(10, createColorItem(Materials.WHITE_WOOL, ChatColor.stringValueOf("WHITE"), "WHITE"),
                unused -> handleColorSelection("WHITE"));
        setItem(11, createColorItem(Materials.YELLOW_WOOL, ChatColor.stringValueOf("YELLOW"), "YELLOW"),
                unused -> handleColorSelection("YELLOW"));
        setItem(12, createColorItem(Materials.RED_WOOL, ChatColor.stringValueOf("RED"), "RED"),
                unused -> handleColorSelection("RED"));
        setItem(13, createColorItem(Materials.PINK_WOOL, ChatColor.stringValueOf("LIGHT_PURPLE"), "LIGHT_PURPLE"),
                unused -> handleColorSelection("LIGHT_PURPLE"));
        setItem(14, createColorItem(Materials.PURPLE_WOOL, ChatColor.stringValueOf("DARK_PURPLE"), "DARK_PURPLE"),
                unused -> handleColorSelection("DARK_PURPLE"));
        setItem(15, createColorItem(Materials.ORANGE_WOOL, ChatColor.stringValueOf("GOLD"), "GOLD"),
                unused -> handleColorSelection("GOLD"));
        setItem(16, createColorItem(Materials.LIME_WOOL, ChatColor.stringValueOf("GREEN"), "GREEN"),
                unused -> handleColorSelection("GREEN"));
        setItem(19, createColorItem(Materials.GRAY_WOOL, ChatColor.stringValueOf("GRAY"), "GRAY"),
                unused -> handleColorSelection("GRAY"));
        setItem(20, createColorItem(Materials.LIGHT_GRAY_WOOL, ChatColor.stringValueOf("DARK_GRAY"), "DARK_GRAY"),
                unused -> handleColorSelection("DARK_GRAY"));
        setItem(21, createColorItem(Materials.CYAN_WOOL, ChatColor.stringValueOf("AQUA"), "AQUA"),
                unused -> handleColorSelection("AQUA"));
        setItem(22, createColorItem(Materials.LIGHT_BLUE_WOOL, ChatColor.stringValueOf("BLUE"), "BLUE"),
                unused -> handleColorSelection("BLUE"));
        setItem(23, createColorItem(Materials.BLUE_WOOL, ChatColor.stringValueOf("DARK_BLUE"), "DARK_BLUE"),
                unused -> handleColorSelection("DARK_BLUE"));
        setItem(24, createColorItem(Materials.BROWN_WOOL, ChatColor.stringValueOf("DARK_RED"), "DARK_RED"),
                unused -> handleColorSelection("DARK_RED"));
        setItem(25, createColorItem(Materials.GREEN_WOOL, ChatColor.stringValueOf("DARK_GREEN"), "DARK_GREEN"),
                unused -> handleColorSelection("DARK_GREEN"));

        setItem(32, createColorItem(Materials.GRAY_WOOL, ChatColor.stringValueOf("BLACK"), "BLACK"),
                unused -> handleColorSelection("BLACK"));

        setItem(30, createColorItem(Materials.CYAN_WOOL, ChatColor.stringValueOf("DARK_AQUA"), "DARK_AQUA"),
                unused -> handleColorSelection("DARK_AQUA"));

        setItem(39, createItem(Materials.NAME_TAG, "Input color Hex (#..)"), unused -> {
            textInput.open(this, player, "Hexadecimal Color", "#", s -> {
                try {
                    editCurrencyUseCase.editColor(currency.getSingular(), s);
                    player.sendMessage(ChatColor.stringValueOf(Colors.GREEN) + "[Bank] " + ChatColor.stringValueOf(Colors.GRAY) + "Color updated successfully to " + s + ".");
                    GUIFactory.editCurrencyPanel(player, currency, getParent().getParent()).open();
                } catch (Exception e) {
                    player.sendMessage(ChatColor.stringValueOf(Colors.GREEN) + "[Bank]" + ChatColor.stringValueOf(Colors.RED) + " Error: " + ChatColor.stringValueOf(Colors.YELLOW) + e.getMessage());
                    this.openParent();
                }
                return null;
            });
        });

        setItem(40, createItem(Materials.BARRIER, ChatColor.stringValueOf(Colors.RED) + "Back", ChatColor.stringValueOf(Colors.GRAY) + "Click to go back"), unused -> {
            this.openParent();
        });
    }

    private void handleColorSelection(String colorName) {
        try {
            editCurrencyUseCase.editColor(currency.getSingular(), colorName);
            player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY) +"Color updated successfully to " + colorName + ".");
            GUIFactory.editCurrencyPanel(player,currency,getParent().getParent()).open();
        } catch (Exception e) {
            player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: "+ ChatColor.stringValueOf(Colors.YELLOW)+ e.getMessage());
            this.openParent();
        }
    }

    private IItemStack createColorItem(Materials material, String chatColor, String colorName) {
        return createItem(material, chatColor + colorName,
                ChatColor.stringValueOf(Colors.GRAY)+"Click to select this color.",
                chatColor + "Example: " + currency.getSingular());
    }
}