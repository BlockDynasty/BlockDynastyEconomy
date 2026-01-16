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
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import lib.gui.GUIFactory;
import lib.gui.components.*;
import lib.gui.components.factory.Item;
import lib.gui.components.generics.Button;
import abstractions.platform.recipes.RecipeItem;
import lib.util.TextureValidator;
import abstractions.platform.materials.Materials;
import lib.gui.components.generics.AbstractPanel;
import util.colors.ChatColor;
import util.colors.Colors;
import services.messages.Message;

import java.util.Map;

public class EditCurrencyPanel extends AbstractPanel {
    private final IEntityGUI player;
    private final ICurrency currency;
    private final EditCurrencyUseCase editCurrencyUseCase;
    private final ITextInput textInput;

    public EditCurrencyPanel(IEntityGUI player, ICurrency currency, EditCurrencyUseCase editCurrencyUseCase, IGUI parentGUI, ITextInput textInput) {
        super(Message.process(Map.of("currency",currency.getSingular()),"EditCurrencyPanel.title"), 5,player, parentGUI);
        this.player = player;
        this.currency = currency;
        this.textInput = textInput;
        this.editCurrencyUseCase =editCurrencyUseCase;

        setupGUI();
    }

    private void setupGUI() {
        // Current currency info
        String color = ChatColor.stringValueOf(currency.getColor());
        setButton(4, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.GOLD_INGOT)
                        .setName(Message.process(Map.of("currency", color + currency.getSingular() + " / " + currency.getPlural()), "EditCurrencyPanel.button1.nameItem"))
                        .setTexture(currency.getTexture())
                        .setLore(Message.processLines(Map.of(
                                "symbol", color + currency.getSymbol(),
                                "color", color + currency.getColor(),
                                "balance", color + currency.getDefaultBalance(),
                                "rate", color + currency.getExchangeRate(),
                                "transferable", (currency.isTransferable() ? ChatColor.stringValueOf(Colors.GREEN) + "Yes" : ChatColor.stringValueOf(Colors.RED) + "No"),
                                "default", (currency.isDefaultCurrency() ? ChatColor.stringValueOf(Colors.GREEN) + "Yes" : ChatColor.stringValueOf(Colors.RED) + "No"),
                                "decimals", (currency.isDecimalSupported() ? ChatColor.stringValueOf(Colors.GREEN) + "Yes" : ChatColor.stringValueOf(Colors.RED) + "No")), "EditCurrencyPanel.button1.lore"))
                        .build()))
                .build());

        // Edit Start Balance button
        setButton(11, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.EMERALD_BLOCK)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button2.nameItem"))
                        .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button2.lore"))
                        .build()))
                .setLeftClickAction(f -> {
                    openStartBalanceInput();
                })
                .build());

        // Set Currency Rate button
        setButton(13, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.GOLD_NUGGET)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button3.nameItem"))
                        .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button3.lore"))
                        .build()))
                .setLeftClickAction(unused -> {
                    openExchangeRateInput();
                })
                .build());

        setButton(14, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.BOOK)
                        .setName(ChatColor.stringValueOf(Colors.GREEN) + "Whitelist currencies for Exchange")
                        .setLore("In this section you can decide which currencies ", "you will allow this currency to be exchanged with.")
                        .build()))
                .setLeftClickAction(f -> {
                    GUIFactory.currencyListExchange(player, currency, this).open();
                })
                .build());

        // Edit Color button
        setButton(15, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.LIME_DYE)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button4.nameItem"))
                        .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button4.lore"))
                        .build()))
                .setLeftClickAction(f -> {
                    openColorSelectionGUI();
                })
                .build());

        // Edit Texture URL button
        setButton(16, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.EMERALD)
                        .setName(ChatColor.stringValueOf(Colors.GREEN) + "Add texture URL or Base64")
                        .setLore("This option allows you to add custom", "heads for this currency", "example url: http://textures.minecraft.net/texture/...", "You can find them at:" + ChatColor.stringValueOf(Colors.GREEN) + "https://minecraft-heads.com", "For developers")
                        .build()))
                .setLeftClickAction(f -> {
                    textInput.asInputChat().open(this, player, "Texture URL or Base64: " + currency.getSingular(), currency.getTexture(), s -> {
                        String stringUrl = TextureValidator.validateInput(s);

                        if(!stringUrl.isEmpty()){
                            try {
                                editCurrencyUseCase.editTexture(currency.getSingular(), stringUrl);
                                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN) + "[Bank] " + ChatColor.stringValueOf(Colors.GRAY) + "Texture updated successfully.");
                                openEditCurrencyGUI();
                            } catch (Exception e) {
                                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN) + "[Bank] " + ChatColor.stringValueOf(Colors.RED) + "Error: " + e.getMessage());
                                openEditCurrencyGUI();
                            }
                        }else{
                            player.sendMessage(ChatColor.stringValueOf(Colors.GREEN) + "[Bank] " + ChatColor.stringValueOf(Colors.RED) + "Error: Invalid texture URL or Base64 input.");
                            openEditCurrencyGUI();
                        }
                        return null;
                    });
                })
                .build());

        // Edit Symbol button
        setButton(20,Button.builder()
                .setItemStack( Item.of(RecipeItem.builder()
                        .setMaterial(Materials.NAME_TAG)
                        .setName(Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button5.nameItem"))
                        .setLore(Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button5.lore"))
                        .build()))
                .setLeftClickAction(f -> {openSymbolInput();})
                .build());

        // Set Default Currency button
        setButton(22, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.NETHER_STAR)
                        .setName(Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button6.nameItem"))
                        .setLore(Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button6.lore"))
                        .build()))
                .setLeftClickAction(f -> {
                    try {
                        editCurrencyUseCase.setDefaultCurrency(currency.getSingular());
                        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY) + currency.getSingular() + " is now the default currency.");
                        openEditCurrencyGUI();
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: " + e.getMessage());
                        openEditCurrencyGUI();
                    }
                }).build());

        // Toggle Payable button
        setButton(24, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(currency.isTransferable() ? Materials.LIME_CONCRETE: Materials.RED_CONCRETE)
                        .setName(currency.isTransferable() ? "Transferable: "+ ChatColor.stringValueOf(Colors.GREEN)+"Activated" : "Transferable: "+ChatColor.stringValueOf(Colors.RED)+"Disabled")
                        .setLore(ChatColor.stringValueOf(Colors.GRAY)+"Click to " + (currency.isTransferable() ? "Disable" : "Enable"),"This option affects:","Transfer","Pay","Trade/Offers")
                        .build()))
                .setLeftClickAction(f -> {
                    try {
                        editCurrencyUseCase.togglePayable(currency.getSingular());
                        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Transfer option changed");
                        openEditCurrencyGUI();
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] Error: " + e.getMessage());
                        openEditCurrencyGUI();
                    }
                }).build());
        // Edit Singular Name button
        setButton(29, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.PAPER)
                        .setName(Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button7.nameItem"))
                        .setLore(Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button7.lore"))
                        .build()))
                .setLeftClickAction(f -> {openSingularNameInput();})
                .build());

        // Edit Plural Name button

        setButton(31, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.BOOK)
                        .setName(Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button8.nameItem"))
                        .setLore(Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button8.lore"))
                        .build()))
                .setLeftClickAction(f -> {openPluralNameInput();})
                .build());

        // Toggle Decimals button
        setButton(33, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(currency.isDecimalSupported() ? Materials.LIME_CONCRETE : Materials.RED_CONCRETE)
                        .setName(currency.isDecimalSupported() ? "Decimals support: "+ ChatColor.stringValueOf(Colors.GREEN)+"Activated" : "Decimals support: "+ChatColor.stringValueOf(Colors.RED)+"Disabled")
                        .setLore("Click to " + (currency.isDecimalSupported() ? "Disable" : "Enable") + " decimal support")
                        .build()))
                .setLeftClickAction(f -> {
                    try {
                        editCurrencyUseCase.toggleDecimals(currency.getSingular());
                        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] Decimal support changed.");
                        openEditCurrencyGUI();
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] Error: " + e.getMessage());
                        openEditCurrencyGUI();
                    }
                }).build());

        // Back button
        setButton(40, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.BARRIER)
                        .setName(ChatColor.stringValueOf(Colors.RED)+"Back")
                        .setLore(ChatColor.stringValueOf(Colors.GRAY)+"Click to go back")
                        .build()))
                .setLeftClickAction(f -> {GUIFactory.currencyListToEditPanel(player, this.getParent().getParent()).open();})
                .build());
    }

    private void openEditCurrencyGUI() {
        GUIFactory.editCurrencyPanel(player, currency, this.getParent()).open();
    }

    private void openColorSelectionGUI() {
        GUIFactory.colorSelectorPanel(player,currency,this).open();
    }

    private void openStartBalanceInput(){
        textInput.open(this,player,"Initial balance:"+currency.getSingular(),currency.getDefaultBalance().toString(), s->{
            try {
                double startBal = Double.parseDouble(s);
                try {
                    editCurrencyUseCase.editStartBal(currency.getSingular(), startBal);
                    player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Initial balance updated correctly.");
                    openEditCurrencyGUI();
                } catch (Exception e) {
                    player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: " + e.getMessage());
                    openEditCurrencyGUI();
                }
            }catch (NumberFormatException e){
                return "Invalid format";
            }
            return null;
        });
    }

    private void openExchangeRateInput(){
        textInput.open(this,player,"Exchange rate:"+currency.getSingular(),String.valueOf(currency.getExchangeRate()),s->{
            try {
                double rate = Double.parseDouble(s);
                try {
                    editCurrencyUseCase.setCurrencyRate(currency.getSingular(), rate);
                    player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Exchange rate updated correctly.");
                    openEditCurrencyGUI();
                } catch (Exception e) {
                    player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: " + e.getMessage());
                    openEditCurrencyGUI();
                }
            } catch (NumberFormatException e) {
                return "Invalid format";
            }
            return null;
        });
    }

    private void openSymbolInput(){
        textInput.open(this,player,"Symbol:"+currency.getSingular(),currency.getSymbol(),s ->{
            try {
                editCurrencyUseCase.editSymbol(currency.getSingular(), s);
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Symbol updated successfully.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: " + e.getMessage());
                openEditCurrencyGUI();
            }
            return null;
        });
    }

    private void openSingularNameInput(){
        textInput.open(this,player,"Singular Name",currency.getSingular(), s ->{
            try {
                editCurrencyUseCase.setSingularName(currency.getSingular(), s);
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Singular name updated correctly.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: " + e.getMessage());
                openEditCurrencyGUI();
            }
            return null;
        });
    }

    private void openPluralNameInput() {
        textInput.open(this,player,"Plural Name", currency.getPlural(),s->{
            try {
                editCurrencyUseCase.setPluralName(currency.getSingular(), s);
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Plural noun updated correctly.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: " + e.getMessage());
                openEditCurrencyGUI();
            }
            return null;
        });
    }
}
