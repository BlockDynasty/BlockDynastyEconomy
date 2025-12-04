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

import lib.commands.abstractions.Command;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.registrar.CommandRegistrar;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;
import java.util.List;
import java.util.stream.Collectors;

public class CommandRegister {
    private static void registrarComando(CommandRegistrar<org.spongepowered.api.command.Command.Parameterized> a, PluginContainer container, Command command){
        org.spongepowered.api.command.Command.Builder mainCommandBuilder = registrarComandoRecursivo(command);
        org.spongepowered.api.command.Command.Parameterized commandBuilt = mainCommandBuilder.build();
        a.register(container, commandBuilt, command.getName());
    }

    private static org.spongepowered.api.command.Command.Builder registrarComandoRecursivo(Command command){
        org.spongepowered.api.command.Command.Builder commandBuilder = org.spongepowered.api.command.Command.builder()
                .executor(new CommandAdapter(command));

        if (command.getArgs() != null && !command.getArgs().isEmpty()) {
            for (String argName : command.getArgs()) {
                if ("player".equalsIgnoreCase(argName)) {
                    commandBuilder.addParameter(
                            Parameter.string()
                                    .key(argName)
                                    .completer((context, currentInput) -> Sponge.server().onlinePlayers().stream()
                                            .map(ServerPlayer::name)
                                            .filter(name -> name.toLowerCase().startsWith(currentInput.toLowerCase()))
                                            .map(CommandCompletion::of)
                                            .collect(Collectors.toList()))
                                    .build()
                    );
                } else {
                    commandBuilder.addParameter(Parameter.string().key(argName).build());
                }
            }
            commandBuilder.addParameter(CommandAdapter.ARGS);
        }


        if (command.getPermission() != null && !command.getPermission().isEmpty()) {
            commandBuilder.permission(command.getPermission());
        }
        List<Command> subCommands = command.getSubCommands();
        if (subCommands != null && !subCommands.isEmpty()){
            for (Command subCommand : subCommands) {
                org.spongepowered.api.command.Command.Builder subCommandBuilder = registrarComandoRecursivo(subCommand);
                commandBuilder.addChild(subCommandBuilder.build(), subCommand.getName());
            }
        }
        return commandBuilder;
    }
    public static void registerCommands( CommandRegistrar<org.spongepowered.api.command.Command.Parameterized> a,
                                        PluginContainer container,
                                        List<Command> commands) {
        for (Command command : commands) {
            registrarComando(a, container, command);
        }
    }
}