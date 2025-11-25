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
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import lib.abstractions.IMessages;
import lib.abstractions.PlatformAdapter;
import lib.gui.components.IGUI;
import lib.gui.components.IGUIService;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.abstractions.AbstractPanel;
import lib.gui.templates.administrators.mainMenus.AccountSelectorToEdit;
import lib.gui.templates.administrators.mainMenus.EconomyAdminPanel;
import lib.gui.templates.administrators.mainMenus.CurrencyAdminPanel;
import lib.gui.templates.administrators.subMenus.accounts.*;
import lib.gui.templates.administrators.subMenus.currencies.*;
import lib.gui.templates.users.*;
import lib.gui.templates.users.Exchange.ExchangeFirstPanel;
import lib.gui.templates.users.Offers.CreateOfferFirstPanel;
import lib.gui.templates.users.Offers.MyActiveOffers;
import lib.gui.templates.users.Offers.ReceivedOffers;
import lib.util.colors.Message;

import java.util.UUID;

public class GUIFactory {
    private static ITextInput textInput;
    private static final IGUIService guiService = new GUIService();
    private static UseCaseFactory useCaseFactory;
    private static PlatformAdapter platformAdapter;

    public static void init(UseCaseFactory useCaseFactory,PlatformAdapter adapter, IMessages messages) {
        GUIFactory.platformAdapter = adapter;
        Message.addLang(messages);
        AbstractPanel.setPlatformAdapter(adapter,guiService);
        GUIFactory.useCaseFactory= useCaseFactory;
        GUIFactory.textInput = adapter.getTextInput();

    }
    public static IGUIService getGuiService() {
        return guiService;
    }
    //_-------------------------------------------------------------------------------

    //main admin panel
    public static IGUI economyAdminPanel(IEntityGUI sender){
        return new EconomyAdminPanel(sender);
    }
    //account admin panel
        public static IGUI accountSelectorToEdit(IEntityGUI sender, IGUI parent){
            return new AccountSelectorToEdit(sender,useCaseFactory.searchAccountByName(),useCaseFactory.searchOfflineAccounts(),parent,textInput);
        }
        //submenus for accountPanel
            public static IGUI editAccountPanel(IEntityGUI sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent) {
                return new EditAccountPanel(useCaseFactory.deleteAccount(),useCaseFactory.editAccount(),useCaseFactory.searchAccountByUUID(),sender, target, parent,textInput);
            }
            //submenus for editAccountPanel
                public static IGUI balancePanel(IEntityGUI sender, UUID target, IGUI parent) {
                    return new AccountBalance(sender, target, useCaseFactory.getBalance(), parent);
                }
                public static IGUI depositPanel(IEntityGUI sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent){
                    return new DepositPanel( sender,target,useCaseFactory.searchCurrency(),useCaseFactory.deposit(),parent,textInput);
                }
                public static IGUI sellCommandPanel(IEntityGUI sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent){
                    return new SellCommandPanel( sender,target,useCaseFactory.searchCurrency(),useCaseFactory.withdraw(),parent,textInput,platformAdapter);
                }
                public static IGUI setPanel(IEntityGUI sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent){
                    return  new SetBalancePanel( sender,target,useCaseFactory.searchCurrency(),useCaseFactory.setBalance(),parent,textInput);
                }
                public static IGUI withdrawPanel(IEntityGUI sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent){
                    return new WithdrawPanel( sender,target,useCaseFactory.searchCurrency(),useCaseFactory.withdraw(),parent,textInput);
                }

        //currency admin panel
        public static IGUI currencyPanel(IEntityGUI player, IGUI parent) {
            return new CurrencyAdminPanel(player, parent);
        }
        //submenus for currencyPanel
            public static void createCurrencyPanel(IEntityGUI sender, IGUI parent) {
                new CreateCurrencyGUI(sender, useCaseFactory.createCurrency(),useCaseFactory.searchCurrency(),parent,textInput);
            }
            public static IGUI currencyListToDeletePanel(IEntityGUI player, IGUI parent) {
                return new CurrencyListDelete(player, useCaseFactory.searchCurrency(), useCaseFactory.deleteCurrency(), parent,textInput);
            }
            public static IGUI currencyListToEditPanel(IEntityGUI player, IGUI parent) {
                return new CurrencyListEdit(player, useCaseFactory.searchCurrency(), parent,textInput);
            }
            //submenus for currencyListToEditPanel
                public static IGUI editCurrencyPanel(IEntityGUI sender, ICurrency currency, IGUI parent) {
                    return new EditCurrencyPanel(sender, currency,useCaseFactory.editCurrency(), parent,textInput);
                }
                //submenus for editCurrencyPanel
                    public static IGUI colorSelectorPanel(IEntityGUI sender, ICurrency currency, EditCurrencyPanel parent) {
                        return new ColorSelectionPanel( sender,currency,useCaseFactory.editCurrency(), parent,textInput);
                    }
    //_-------------------------------------------------------------------------------
    //main bank user panel
    public static IGUI bankPanel(IEntityGUI sender) {
        return new BankPanel(sender, useCaseFactory,textInput);
    }
    //submenus for bankPanel
        public static IGUI createOfferFirstPanel(IEntityGUI sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent ){
            return new CreateOfferFirstPanel(sender,target,useCaseFactory.searchCurrency(),useCaseFactory.createOffer(), parent, textInput);
        }
        public static IGUI balancePanel(IEntityGUI sender, IGUI parent) {
            return new AccountBalance(sender, useCaseFactory.getBalance(), parent);
        }
        public static IGUI listPlayersFromDb(IEntityGUI sender, IGUI parent) {
            return new ListPlayersFromDb(sender, parent, useCaseFactory.searchAccountByName(),useCaseFactory.searchOfflineAccounts(),textInput);
        }
        public static IGUI exchangeFirstPanel(IEntityGUI sender, IGUI parent) {
            return new ExchangeFirstPanel(sender, useCaseFactory.searchCurrency(), useCaseFactory.exchange(), parent , textInput);
        }
        public static IGUI receivedOffers(IEntityGUI sender, IGUI parent) {
            return new ReceivedOffers(useCaseFactory.searchOffer(), useCaseFactory.acceptOffer(),useCaseFactory.cancelOffer(), sender, parent );
        }
        public static IGUI myActiveOffers(IEntityGUI sender, IGUI parent) {
            return new MyActiveOffers(useCaseFactory.searchOffer(),useCaseFactory.cancelOffer(), sender, parent);
        }
        public static IGUI myActiveOffers(IEntityGUI sender) {
            return new MyActiveOffers(useCaseFactory.searchOffer(), useCaseFactory.cancelOffer(), sender, null);
        }
        public static IGUI listPlayersOnline(IEntityGUI sender, IGUI parent) {
        return new ListPlayersOnline(sender, parent, textInput);
    }
            //submenus for payPanel
            public static IGUI currencyListToPayPanel(IEntityGUI sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent) {
                return new CurrencyListToPay(
                        sender,
                        target,
                        useCaseFactory.searchCurrency(),
                        useCaseFactory.pay(),
                        parent,
                        textInput
                );
            }

            public static IGUI currencyListExchange(IEntityGUI player, ICurrency currency, IGUI parent) {
                return new CurrencyListExchange(player, useCaseFactory.editCurrency(), parent, currency);
            }

            public static IGUI currencyListToAddExchange(IEntityGUI player, ICurrency currency, IGUI parent) {
                return new CurrencyListToAddExchange(player, useCaseFactory.searchCurrency(), useCaseFactory.editCurrency(), parent, currency);
            }
}
