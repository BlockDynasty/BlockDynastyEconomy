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

package BlockDynasty.BukkitImplementation.adapters.commands;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import lib.commands.abstractions.Command;
import lib.commands.CommandService;
import org.bukkit.command.PluginCommand;

import java.util.List;

public class CommandRegister {
    private static List<Command> commands = CommandService.getMainCommands();
    private static final BlockDynastyEconomy plugin= BlockDynastyEconomy.getInstance();

    public static void registerAllEconomySystem(){
        CommandService.registerSubCommand("eco",new ReloadCommand(plugin));

        for(Command command: commands){
            CommandAdapter commandAdapter = new CommandAdapter(command);
            TabCompletionAdapter tabCompleterAdapter = new TabCompletionAdapter(command);
            PluginCommand pluginCommand = plugin.getCommand(command.getName());

            if (pluginCommand != null) {
                pluginCommand.setExecutor(commandAdapter);
                pluginCommand.setTabCompleter(tabCompleterAdapter);
                pluginCommand.setPermission(command.getPermission());
            }
        }
    }
}
