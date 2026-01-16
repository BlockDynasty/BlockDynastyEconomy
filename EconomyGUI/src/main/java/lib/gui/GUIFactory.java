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

package lib.gui;
import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;

import lib.abstractions.IConfigurationGUI;
import lib.commands.PlatformCommand;
import lib.gui.components.PlatformGUI;
import lib.gui.components.*;
import lib.gui.templates.administrators.mainMenus.AccountSelectorToEdit;
import lib.gui.templates.administrators.mainMenus.EconomyAdminPanel;
import lib.gui.templates.administrators.mainMenus.CurrencyAdminPanel;
import lib.gui.templates.administrators.mainMenus.EconomyAdminPanelDisabled;
import lib.gui.templates.administrators.subMenus.accounts.*;
import lib.gui.templates.administrators.subMenus.currencies.*;
import lib.gui.templates.administrators.subMenus.gui.BankPanelEditor;
import lib.gui.templates.users.*;
import lib.gui.templates.users.Cashmachine.ExtractorPanel;
import lib.gui.templates.users.Exchange.ExchangeFirstPanel;
import lib.gui.templates.users.Offers.*;

public class GUIFactory {
    private static ITextInput textInput;
    private static IConfigurationGUI config;
    private static UseCaseFactory useCaseFactory;
    private static PlatformGUI platformAdapter;

    public static void init(UseCaseFactory useCaseFactory, PlatformGUI adapter, IConfigurationGUI configuration) {
        GUIFactory.platformAdapter = adapter;
        GUIFactory.config = configuration;
        GUIFactory.textInput = adapter.getTextInput();
        GUIFactory.useCaseFactory= useCaseFactory;
        BankPanel.setButtonsState(config.getButtonsConfig());
    }

    public static IGUI economyAdminPanel(IEntityGUI sender){
        if(platformAdapter.hasSupportGui()){
            return new EconomyAdminPanel(sender);
        }
        return new EconomyAdminPanelDisabled(sender);
    }
    public static IGUI accountSelectorToEdit(IEntityGUI sender,IGUI parent){
        return new AccountSelectorToEdit(sender,useCaseFactory.searchAccountByName(),useCaseFactory.searchOfflineAccounts(),parent,textInput);
    }
    public static IGUI editAccountPanel(IEntityGUI sender,Player target,IGUI parent) {
        return new EditAccountPanel(useCaseFactory.deleteAccount(),useCaseFactory.editAccount(),useCaseFactory.searchAccountByPlayer(),platformAdapter,sender, target, parent,textInput);
    }
    public static IGUI balancePanel(IEntityGUI sender,Player target,IGUI parent) {
        return new AccountBalance(sender, target, useCaseFactory.getBalance(), parent);
    }
    public static IGUI depositPanel(IEntityGUI sender,Player target,IGUI parent){
        return new DepositPanel( sender,target,useCaseFactory.searchCurrency(),useCaseFactory.deposit(),parent,textInput);
    }
    public static IGUI sellCommandPanel(IEntityGUI sender,Player target, IGUI parent){
        return new SellCommandPanel( sender,target,useCaseFactory.searchCurrency(),useCaseFactory.withdraw(),parent,textInput,platformAdapter);
    }
    public static IGUI setPanel(IEntityGUI sender,Player target,IGUI parent){
        return  new SetBalancePanel( sender,target,useCaseFactory.searchCurrency(),useCaseFactory.setBalance(),parent,textInput);
    }
    public static IGUI withdrawPanel(IEntityGUI sender,Player target,IGUI parent){
        return new WithdrawPanel( sender,target,useCaseFactory.searchCurrency(),useCaseFactory.withdraw(),parent,textInput);
    }
    public static IGUI currencyPanel(IEntityGUI player,IGUI parent) {
        return new CurrencyAdminPanel(player, parent);
    }
    public static void createCurrencyPanel(IEntityGUI sender,IGUI parent) {
        new CreateCurrencyGUI(sender, useCaseFactory.createCurrency(),useCaseFactory.searchCurrency(),parent,textInput);
    }
    public static IGUI currencyListToDeletePanel(IEntityGUI player,IGUI parent) {
        return new CurrencyListDelete(player, useCaseFactory.searchCurrency(), useCaseFactory.deleteCurrency(), parent,textInput);
    }
    public static IGUI currencyListToEditPanel(IEntityGUI player,IGUI parent) {
        return new CurrencyListEdit(player, useCaseFactory.searchCurrency(), parent,textInput);
    }
    public static IGUI editCurrencyPanel(IEntityGUI sender,ICurrency currency,IGUI parent) {
        return new EditCurrencyPanel(sender, currency,useCaseFactory.editCurrency(), parent,textInput);
    }
    public static IGUI colorSelectorPanel(IEntityGUI sender,ICurrency currency,EditCurrencyPanel parent) {
        return new ColorSelectionPanel( sender,currency,useCaseFactory.editCurrency(), parent,textInput);
    }
    public static IGUI bankPanel(IEntityGUI sender) {
        if(platformAdapter.hasSupportGui()){
            return new BankPanel(sender, useCaseFactory.searchAccountByPlayer(),textInput);
        }else {
            return new BankPanelDisabled(sender);
        }

    }
    public static IGUI createOfferFirstPanel(IEntityGUI sender,Player target,IGUI parent ){
        return new CreateOfferFirstPanel(sender,target,useCaseFactory.searchCurrency(),useCaseFactory.createOffer(), parent, textInput);
    }
    public static IGUI balancePanel(IEntityGUI sender,IGUI parent) {
        return new AccountBalance(sender, useCaseFactory.getBalance(), parent);
    }
    public static IGUI listPlayersFromDb(IEntityGUI sender,IGUI parent) {
        return new ListPlayersFromDb(sender, parent, useCaseFactory.searchAccountByName(),useCaseFactory.searchOfflineAccounts(),textInput);
    }
    public static IGUI exchangeFirstPanel(IEntityGUI sender,IGUI parent) {
        return new ExchangeFirstPanel(sender, useCaseFactory.searchCurrency(), useCaseFactory.exchange(), parent , textInput);
    }
    public static IGUI receivedOffers(IEntityGUI sender,IGUI parent) {
        return new ReceivedOffers(useCaseFactory.searchOffer(), useCaseFactory.acceptOffer(),useCaseFactory.cancelOffer(), sender, parent );
    }
    public static IGUI myActiveOffers(IEntityGUI sender,IGUI parent) {
        return new MyActiveOffers(useCaseFactory.searchOffer(),useCaseFactory.cancelOffer(), sender, parent);
    }
    public static IGUI myActiveOffers(IEntityGUI sender) {
        return new MyActiveOffers(useCaseFactory.searchOffer(), useCaseFactory.cancelOffer(), sender, null);
    }
    public static IGUI listPlayersOnline(IEntityGUI sender,IGUI parent) {
        return new ListPlayersOnline(sender, parent, textInput,platformAdapter);
    }
    public static IGUI listPlayerOnlineToOffer(IEntityGUI sender,IGUI parent) {
        return new ListPlayerOnlineToOffer(sender, parent, textInput,platformAdapter);
    }
    public static IGUI listPlayersOfflineToOffer(IEntityGUI sender,IGUI parent) {
        return new ListPlayersOfflineToOffer(sender, parent, useCaseFactory.searchAccountByName(),useCaseFactory.searchOfflineAccounts(),textInput);
    }
    public static IGUI currencyListToPayPanel(IEntityGUI sender,Player target,IGUI parent) {
        return new CurrencyListToPay(sender, target, useCaseFactory.searchCurrency(), useCaseFactory.pay(), parent, textInput);
    }
    public static IGUI currencyListExchange(IEntityGUI player,ICurrency currency,IGUI parent) {
        return new CurrencyListExchange(player, useCaseFactory.editCurrency(), parent, currency);
    }
    public static IGUI currencyListToAddExchange(IEntityGUI player,ICurrency currency,IGUI parent) {
        return new CurrencyListToAddExchange(player, useCaseFactory.searchCurrency(), useCaseFactory.editCurrency(), parent, currency);
    }
    public static IGUI bankPanelEditor(IEntityGUI player,IGUI parent) {
        return new BankPanelEditor(player,parent,config);
    }

    public static IGUI extractorPanel(IEntityGUI player,IGUI parent) {
        return new ExtractorPanel(player, useCaseFactory.searchCurrency(), parent, textInput);
    }
}
