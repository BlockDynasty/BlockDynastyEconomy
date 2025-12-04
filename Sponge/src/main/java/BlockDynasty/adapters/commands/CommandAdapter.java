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

package BlockDynasty.adapters.commands;


import BlockDynasty.adapters.platformAdapter.EntityConsoleAdapter;
import BlockDynasty.adapters.platformAdapter.EntityPlayerAdapter;
import lib.commands.abstractions.Command;
import lib.commands.abstractions.IEntityCommands;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class CommandAdapter implements CommandExecutor {
    private final Command command;
    public static Parameter.Value<String> ARGS = Parameter.remainingJoinedStrings().key("args").optional().build();

    public CommandAdapter(Command command) {
        this.command = command;
    }

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        IEntityCommands sender;
        if (context.cause().root() instanceof ServerPlayer) {
            sender = EntityPlayerAdapter.of((ServerPlayer) context.cause().root());
        } else {
            sender = EntityConsoleAdapter.of(context.cause().audience());
        }
        List<String> argList = new ArrayList<>();
        // Extract arguments based on defined parameters
        if (command.getArgs() != null && !command.getArgs().isEmpty()) {
            for (String argName : command.getArgs()) {
                context.one(Parameter.string().key(argName).build()).ifPresent(argList::add);
            }

            String rawArgs = context.one(ARGS).orElse("");
            if (!rawArgs.isEmpty()) {
                String[] parts = rawArgs.split(" ");
                for (String part : parts) {
                    argList.add(part);
                }
            }
        }


        try {
            command.execute(sender, argList.toArray(new String[0]));
            //return success ? CommandResult.success() : CommandResult.error( Component.text("Command execution failed") );
            return CommandResult.success();
        } catch (Exception e) {
            throw new CommandException(Component.text(e.getMessage()));
        }
    }
}

