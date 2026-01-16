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

package lib.commands.templates.administrators.EconomySubcommand;

import lib.commands.abstractions.AbstractCommand;
import lib.commands.abstractions.Command;
import lib.commands.abstractions.IEntityCommands;

import java.util.List;

public class CurrencyCommand extends AbstractCommand {
    public CurrencyCommand() {
        super("currency","BlockDynastyEconomy.economy.superUser");
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args )){
            return false;
        }

        if (args.length == 0) {
            List<Command> subCommands = getSubCommands();
            StringBuilder helpMessage = new StringBuilder(">>"+this.getName()+" Subcommands:\n");
            for (Command cmd : subCommands) {
                helpMessage.append("- ").append(cmd.getName()).append("\n");
            }
            sender.sendMessage(helpMessage.toString());
            return false;
        }

        Command subCommand = getSubCommands().stream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(args[0]))
                .findFirst()
                .orElse(null);

        if (subCommand == null) {
            List<Command> subCommands = getSubCommands();
            StringBuilder helpMessage = new StringBuilder(">>"+this.getName()+" Subcommands:\n");
            for (Command cmd : subCommands) {
                helpMessage.append("- ").append(cmd.getName()).append("\n");
            }
            sender.sendMessage(helpMessage.toString());
            return false;
        }

        String[] subCommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subCommandArgs, 0, args.length - 1);

        return subCommand.execute(sender, subCommandArgs);
    }
}
