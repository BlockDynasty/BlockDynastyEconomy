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

package lib.commands;

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import lib.abstractions.PlatformAdapter;
import lib.commands.abstractions.Command;
import lib.commands.templates.administrators.EconomyGUICommand;
import lib.commands.templates.administrators.EconomySubcommand.*;
import lib.commands.templates.administrators.EconomySubcommand.CurrencySubcommand.*;
import lib.commands.templates.administrators.EconomyCommand;
import lib.commands.templates.users.*;
import lib.commands.templates.users.OfferSubCommand.AcceptOfferCommand;
import lib.commands.templates.users.OfferSubCommand.CancelOfferCommand;
import lib.commands.templates.users.OfferSubCommand.CreateOfferCommand;
import lib.commands.templates.users.OfferSubCommand.DenyOfferCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandService {
    private static final List<Command> MainCommands = new ArrayList<>();
    private static UseCaseFactory useCaseFactory;
    private static PlatformAdapter platformAdapter;

    public static void init(PlatformAdapter platformAdapter,UseCaseFactory useCaseFactory) {
        //MessageService.setProvider(messageProvider);
        CommandService.useCaseFactory = useCaseFactory;
        CommandService.platformAdapter = platformAdapter;
        CommandService.registerCommands();
    }
    //tree structure of commands
    private static void registerCommands(){
        OfferCommand offerCommand = new OfferCommand();//main
        EconomyCommand economyCommand = new EconomyCommand();//main
        CurrencyCommand currencyCommand = new CurrencyCommand();//main
        WithdrawCommand withdrawCommand = new WithdrawCommand(useCaseFactory.withdraw());
        DepositCommand depositCommand = new DepositCommand(useCaseFactory.deposit());
        SetCommand setCommand = new SetCommand(useCaseFactory.setBalance());
        ExchangeCommand exchangeCommand = new ExchangeCommand(useCaseFactory.exchange());

        BuyCommand buyCommand = new BuyCommand(useCaseFactory.withdraw(),platformAdapter);

        CreateCurrencyCommand createCurrencyCommand = new CreateCurrencyCommand(useCaseFactory.createCurrency());
        DeleteCurrencyCommand deleteCurrencyCommand = new DeleteCurrencyCommand(useCaseFactory.deleteCurrency());
        EditColorCommand editColorCommand = new EditColorCommand(useCaseFactory.editCurrency());
        EditDecimalsCommand editDecimalsCommand = new EditDecimalsCommand(useCaseFactory.editCurrency());
        EditPayableCommand editPayableCommand = new EditPayableCommand(useCaseFactory.editCurrency());
        EditRateCommand editRateCommand = new EditRateCommand(useCaseFactory.editCurrency());
        EditStartBalCommand editStartBalCommand = new EditStartBalCommand(useCaseFactory.editCurrency());
        ViewCommand ViewCommand = new ViewCommand(useCaseFactory.searchCurrency());
        EditSymbolCommand editSymbolCommand = new EditSymbolCommand(useCaseFactory.editCurrency());
        ListCommand listCommand = new ListCommand(useCaseFactory.searchCurrency());
        SetDefaultCommand setDefaultCommand = new SetDefaultCommand(useCaseFactory.editCurrency());
        EditPluralNameCommand editPluralNameCommand = new EditPluralNameCommand(useCaseFactory.editCurrency());
        EditSingularNameCommand editSingularNameCommand = new EditSingularNameCommand(useCaseFactory.editCurrency());

        CreateOfferCommand createOfferCommand = new CreateOfferCommand(useCaseFactory.createOffer(),platformAdapter);
        CancelOfferCommand cancelOfferCommand = new CancelOfferCommand(useCaseFactory.cancelOffer(),platformAdapter);
        AcceptOfferCommand acceptOfferCommand = new AcceptOfferCommand(useCaseFactory.acceptOffer(),platformAdapter);
        DenyOfferCommand denyOfferCommand = new DenyOfferCommand(useCaseFactory.cancelOffer(),platformAdapter);

        PayCommand payCommand = new PayCommand(useCaseFactory.pay(),platformAdapter);
        BalanceCommand balanceCommand = new BalanceCommand(useCaseFactory.getBalance());
        BalanceTopCommand balanceTopCommand = new BalanceTopCommand(useCaseFactory.topAccounts());

        BankGUICommand bankGUICommand = new BankGUICommand();
        EconomyGUICommand economyGUICommand = new EconomyGUICommand();

        economyCommand.registerSubCommand(withdrawCommand);
        economyCommand.registerSubCommand(depositCommand);
        economyCommand.registerSubCommand(setCommand);
        economyCommand.registerSubCommand(buyCommand);
        economyCommand.registerSubCommand(currencyCommand);
        economyCommand.registerSubCommand(economyGUICommand);

        currencyCommand.registerSubCommand(createCurrencyCommand);
        currencyCommand.registerSubCommand(deleteCurrencyCommand);
        currencyCommand.registerSubCommand(ViewCommand);
        currencyCommand.registerSubCommand(editColorCommand);
        currencyCommand.registerSubCommand(editDecimalsCommand);
        currencyCommand.registerSubCommand(editPayableCommand);
        currencyCommand.registerSubCommand(editRateCommand);
        currencyCommand.registerSubCommand(editStartBalCommand);
        currencyCommand.registerSubCommand(editSymbolCommand);
        currencyCommand.registerSubCommand(listCommand);
        currencyCommand.registerSubCommand(setDefaultCommand);
        currencyCommand.registerSubCommand(editPluralNameCommand);
        currencyCommand.registerSubCommand(editSingularNameCommand);

        offerCommand.registerSubCommand(createOfferCommand);
        offerCommand.registerSubCommand(cancelOfferCommand);
        offerCommand.registerSubCommand(acceptOfferCommand);
        offerCommand.registerSubCommand(denyOfferCommand);

        MainCommands.add(economyCommand);
        MainCommands.add(offerCommand);
        MainCommands.add(payCommand);
        MainCommands.add(balanceCommand);
        MainCommands.add(balanceTopCommand);
        MainCommands.add(exchangeCommand);
        MainCommands.add(bankGUICommand);
    }

    public static List<Command> getMainCommands() {
            return MainCommands;
        }

    public static Command getCommand(String commandName) {
        for(Command command: MainCommands){
            if(command.getName().equalsIgnoreCase(commandName)){
                return command;
            }
        }
        return null;
    }

    public static void registerMainCommand(Command command) {
        if(getCommand(command.getName())==null)
            MainCommands.add(command);
    }

    public static void registerSubCommand(String mainCommand, Command subCommand) {
        Command command = getCommand(mainCommand);
        if(command!=null)
            command.registerSubCommand(subCommand);
    }
}
