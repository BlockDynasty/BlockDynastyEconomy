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

package lib.commands.templates.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyColorUnformat;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import util.colors.ChatColor;
import util.colors.Colors;

import java.util.List;

public class EditColorCommand extends AbstractCommand {
    private final EditCurrencyUseCase editCurrencyUseCase;

    public EditColorCommand(EditCurrencyUseCase editCurrencyUseCase) {
        super("color","", List.of("currency", "color"));
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.stringValueOf(Colors.BLACK)+" BLACK");
            sender.sendMessage(ChatColor.stringValueOf(Colors.DARK_BLUE)+" DARK BLUE");
            sender.sendMessage(ChatColor.stringValueOf(Colors.DARK_GREEN)+" DARK GREEN");
            sender.sendMessage(ChatColor.stringValueOf(Colors.DARK_AQUA)+" DARK AQUA");
            sender.sendMessage(ChatColor.stringValueOf(Colors.DARK_RED)+" DARK RED");
            sender.sendMessage(ChatColor.stringValueOf(Colors.DARK_PURPLE)+" DARK PURPLE");
            sender.sendMessage(ChatColor.stringValueOf(Colors.GOLD)+" GOLD");
            sender.sendMessage(ChatColor.stringValueOf(Colors.GRAY)+" GRAY");
            sender.sendMessage(ChatColor.stringValueOf(Colors.DARK_GRAY)+" DARK GRAY");
            sender.sendMessage(ChatColor.stringValueOf(Colors.BLUE)+" BLUE");
            sender.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+" GREEN");
            sender.sendMessage(ChatColor.stringValueOf(Colors.AQUA)+" AQUA");
            sender.sendMessage(ChatColor.stringValueOf(Colors.RED)+" RED");
            sender.sendMessage(ChatColor.stringValueOf(Colors.LIGHT_PURPLE)+" LIGHT PURPLE");
            sender.sendMessage(ChatColor.stringValueOf(Colors.YELLOW)+" YELLOW");
            sender.sendMessage(ChatColor.stringValueOf(Colors.WHITE)+" WHITE");
            return false;
        }


        String currencyName = args[0];
        String colorString = args[1].toUpperCase();

            try {
                editCurrencyUseCase.editColor(currencyName, colorString);
                sender.sendMessage("Color for " + currencyName + " updated: " + colorString);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage("Currency not found.");
            } catch (CurrencyColorUnformat e) {
                sender.sendMessage("Invalid chat color.");
            }

        return true;
    }
}
