package com.BlockDynasty.hytale.adapters.commands;

import com.BlockDynasty.hytale.adapters.PlayerAdapter;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lib.commands.abstractions.Command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandAdapter extends AbstractTargetPlayerCommand {
    private Command command;
    private List<RequiredArg<String>> requiredArgs;

    public CommandAdapter(Command command) {
        super(command.getName(), command.getDescription());
        this.command = command;

        List<Command> subCommands=command.getSubCommands();
        if (subCommands != null && !subCommands.isEmpty()) {
            for (Command subCommand : subCommands) {
                this.addSubCommand(new CommandAdapter(subCommand));
            }
        }

        this.requiredArgs = new ArrayList<>();
        List<String> args=command.getArgs();
        if (args != null){
            for (String argName : args) {
                this.requiredArgs.add(this.withRequiredArg(argName, argName, ArgTypes.STRING));
            }
        }

        requirePermission(command.getPermission());
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nullable Ref<EntityStore> ref,
                           @Nonnull Ref<EntityStore> ref1,
                           @Nonnull PlayerRef playerRef, @Nonnull World world,
                           @Nonnull Store<EntityStore> store) {
        if (commandContext.isPlayer()){
            List<String> argValues = new ArrayList<>();
            for (RequiredArg<String> requiredArg : requiredArgs){
                String argValue = commandContext.get(requiredArg);
                argValues.add(argValue);
            }
            this.command.execute(new PlayerAdapter(playerRef),argValues.toArray(new String[0]));
        }

    }
}
