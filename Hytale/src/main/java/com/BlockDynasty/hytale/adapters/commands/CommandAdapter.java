package com.BlockDynasty.hytale.adapters.commands;

import com.BlockDynasty.hytale.adapters.PlayerAdapter;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lib.commands.abstractions.Command;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CommandAdapter extends CommandBase {
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
    protected void executeSync(@Nonnull CommandContext commandContext) {
        CommandSender sender = commandContext.sender();
        if (sender instanceof Player player) {
            Ref<EntityStore> ref = player.getReference();
            if (ref != null && ref.isValid()){
                Store<EntityStore> store = ref.getStore();
                World world = store.getExternalData().getWorld();

                CompletableFuture.runAsync(()->{
                    PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
                    if (playerRef != null){
                        List<String> argValues = new ArrayList<>();
                        for (RequiredArg<String> requiredArg : requiredArgs){
                            String argValue = commandContext.get(requiredArg);
                            argValues.add(argValue);
                        }
                        this.command.execute(new PlayerAdapter(playerRef,ref,store),argValues.toArray(new String[0]));
                    }
                },world);
            }
        }else if(sender instanceof ConsoleSender console){
                List<String> argValues = new ArrayList<>();
                for (RequiredArg<String> requiredArg : requiredArgs) {
                    String argValue = commandContext.get(requiredArg);
                    argValues.add(argValue);
                }
                this.command.execute(new CommandConsoleAdapter(console), argValues.toArray(new String[0]));
        }
    }

    /*@Override
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
            this.command.execute(new PlayerAdapter(playerRef,ref,store),argValues.toArray(new String[0]));
        }else if(commandContext.sender() instanceof ConsoleSender console){
            List<String> argValues = new ArrayList<>();
            for (RequiredArg<String> requiredArg : requiredArgs){
                String argValue = commandContext.get(requiredArg);
                argValues.add(argValue);
            }
            this.command.execute(new CommandConsoleAdapter(console),argValues.toArray(new String[0]));
        }

    }*/
}
