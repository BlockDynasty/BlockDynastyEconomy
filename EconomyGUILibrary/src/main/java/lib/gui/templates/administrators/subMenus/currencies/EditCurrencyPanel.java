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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lib.abstractions.IPlayer;
import lib.gui.GUIFactory;
import lib.gui.components.*;
import lib.util.materials.Materials;
import lib.gui.components.abstractions.AbstractPanel;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.util.colors.Message;

import java.io.Console;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Map;

public class EditCurrencyPanel extends AbstractPanel {
    private final IEntityGUI player;
    private final Currency currency;
    private final EditCurrencyUseCase editCurrencyUseCase;
    private final ITextInput textInput;

    public EditCurrencyPanel(IEntityGUI player, Currency currency, EditCurrencyUseCase editCurrencyUseCase, IGUI parentGUI, ITextInput textInput) {
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
        setItem(4,createItem(RecipeItem.builder()
                .setMaterial(Materials.GOLD_INGOT)
                .setName(Message.process(Map.of("currency",color + currency.getSingular() + " / " + currency.getPlural()), "EditCurrencyPanel.button1.nameItem"))
                .setTexture(currency.getTexture())
                .setLore(Message.processLines(Map.of(
                        "symbol",color + currency.getSymbol(),
                        "color",color + currency.getColor(),
                        "balance",color + currency.getDefaultBalance(),
                        "rate",color + currency.getExchangeRate(),
                        "transferable",(currency.isTransferable() ? ChatColor.stringValueOf(Colors.GREEN)+"Yes" : ChatColor.stringValueOf(Colors.RED)+"No"),
                        "default",(currency.isDefaultCurrency() ? ChatColor.stringValueOf(Colors.GREEN)+"Yes" : ChatColor.stringValueOf(Colors.RED)+"No"),
                        "decimals",(currency.isDecimalSupported() ? ChatColor.stringValueOf(Colors.GREEN)+"Yes" : ChatColor.stringValueOf(Colors.RED)+"No")),"EditCurrencyPanel.button1.lore"))
                .build()), null);

        // Edit Start Balance button
        setItem(11, createItem(Materials.EMERALD_BLOCK, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button2.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button2.lore")), f -> {openStartBalanceInput();});

        // Set Currency Rate button
        setItem(13, createItem(Materials.GOLD_NUGGET, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button3.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button3.lore")), f -> {openExchangeRateInput();});

        setItem(14,createItem(Materials.BOOK, ChatColor.stringValueOf(Colors.GREEN)+ "Whitelist currencies for Exchange", "In this section you can decide which currencies ","you will allow this currency to be exchanged with."), f -> {
                GUIFactory.currencyListExchange(player,currency,this).open();
        });

        // Edit Color button
        setItem(15, createItem(Materials.LIME_DYE, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button4.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button4.lore")), f -> {openColorSelectionGUI();});

        // Edit Texture URL button
        setItem(16,createItem(RecipeItem.builder().setMaterial(Materials.EMERALD).setName(ChatColor.stringValueOf(Colors.GREEN)+"Add texture URL or Base64").setLore("This option allows you to add custom","heads for this currency","example url: http://textures.minecraft.net/texture/...","You can find them at:"+ ChatColor.stringValueOf(Colors.GREEN)+"https://minecraft-heads.com","For developers").build()), f -> {
            textInput.asInputChat().open(this,player,"Texture URL or Base64: "+currency.getSingular(),currency.getTexture(), s ->{
                String stringUrl = "";
                URL url =null;
                try {
                    url = new URL(s);
                    stringUrl = s;
                }catch (MalformedURLException e){
                    stringUrl= getTextureURLFromBase64(s,player);
                }

                try {
                    editCurrencyUseCase.editTexture(currency.getSingular(), stringUrl);
                    player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Texture updated successfully.");
                    openEditCurrencyGUI();
                } catch (Exception e) {
                    player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: " + e.getMessage());
                    openEditCurrencyGUI();
                }
                return null;
            });
        });



        // Edit Symbol button
        setItem(20, createItem(Materials.NAME_TAG, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button5.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button5.lore")), f -> {openSymbolInput();});

        // Set Default Currency button
        setItem(22, createItem(Materials.NETHER_STAR, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button6.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button6.lore")), f -> {
            try {
                editCurrencyUseCase.setDefaultCurrency(currency.getSingular());
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY) + currency.getSingular() + " is now the default currency.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: " + e.getMessage());
                openEditCurrencyGUI();
            }
        });

        // Toggle Payable button
        setItem(24, createItem(
                        currency.isTransferable() ? Materials.LIME_CONCRETE: Materials.RED_CONCRETE,
                        currency.isTransferable() ? "Transferable: "+ ChatColor.stringValueOf(Colors.GREEN)+"Activated" : "Transferable: "+ChatColor.stringValueOf(Colors.RED)+"Disabled",
                        ChatColor.stringValueOf(Colors.GRAY)+"Click to " + (currency.isTransferable() ? "Disable" : "Enable"),"This option affects:","Transfer","Pay","Trade/Offers"),
                f -> {
                    try {
                        editCurrencyUseCase.togglePayable(currency.getSingular());
                        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Transfer option changed");
                        openEditCurrencyGUI();
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] Error: " + e.getMessage());
                        openEditCurrencyGUI();
                    }
                });

        // Edit Singular Name button
        setItem(29, createItem(Materials.PAPER, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button7.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button7.lore")), f -> {openSingularNameInput();});

        // Edit Plural Name button
        setItem(31, createItem(Materials.BOOK, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button8.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button8.lore")), f -> {openPluralNameInput();});

        // Toggle Decimals button
        setItem(33, createItem(
                        currency.isDecimalSupported() ?Materials.LIME_CONCRETE : Materials.RED_CONCRETE,
                        currency.isDecimalSupported() ? "Decimals support: "+ ChatColor.stringValueOf(Colors.GREEN)+"Activated" : "Decimals support: "+ChatColor.stringValueOf(Colors.RED)+"Disabled",
                        "Click to " + (currency.isDecimalSupported() ? "Disable" : "Enable") + " decimal support"),
                f -> {
                    try {
                        editCurrencyUseCase.toggleDecimals(currency.getSingular());
                        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] Decimal support changed.");
                        openEditCurrencyGUI();
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] Error: " + e.getMessage());
                        openEditCurrencyGUI();
                    }
                });

        // Back button
        setItem(40, createItem(Materials.BARRIER, ChatColor.stringValueOf(Colors.RED)+"Back",
                ChatColor.stringValueOf(Colors.GRAY)+"Click to go back"), f -> {
            GUIFactory.currencyListToEditPanel(player, this.getParent().getParent()).open();
        });
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

    private static String getTextureURLFromBase64(String base64Texture, IEntityGUI player) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Texture);
            String decodedString = new String(decodedBytes);

            JsonElement jsonElement = JsonParser.parseString(decodedString);
            JsonObject json = jsonElement.getAsJsonObject();
            return json.getAsJsonObject("textures")
                    .getAsJsonObject("SKIN")
                    .get("url")
                    .getAsString();

        } catch (IllegalArgumentException e) {
            player.sendMessage("invalid base64 TEXTURE input: " + e.getMessage());
        }catch (JsonSyntaxException e){
            player.sendMessage("invalid JSON TEXTURE input: " + e.getMessage());
        }catch (NullPointerException e){
           player.sendMessage("missing expected fields in TEXTURE JSON: " + e.getMessage());
        }catch (IllegalStateException e){
            player.sendMessage("invalid TEXTURE JSON structure."+ e.getMessage());
        }
        return "";
    }
}
