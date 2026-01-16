package com.BlockDynasty.hytale.adapters.commands;

import com.BlockDynasty.hytale.BlockDynastyEconomy;
import lib.commands.CommandService;
import lib.commands.abstractions.Command;

import java.util.List;

public class CommandRegister {


    public static void registerCommands() {
       BlockDynastyEconomy plugin= BlockDynastyEconomy.getInstance();

       List<Command> commands = CommandService.getMainCommands();
         for (Command command : commands) {
                  plugin.getCommandRegistry().registerCommand(new CommandAdapter(command));
         }
    }
}
