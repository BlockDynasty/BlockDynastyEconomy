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

package adapters.commands;

import adapters.PlayerAdapter;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

import java.util.List;

public class commandMinestom extends Command {

    public commandMinestom(lib.commands.abstractions.Command command) {
        super(command.getName());

        List<String> args = command.getArgs();
        if (args != null && !args.isEmpty()) {
            Argument<?>[] argumentList = args.stream()
                    .map(argName -> {
                        if (argName.equalsIgnoreCase("player")) {
                            return ArgumentType.Entity(argName).onlyPlayers(true);
                        } else {
                            return ArgumentType.String(argName);
                        }
                    }).toArray(Argument[]::new);
            addSyntax((sender, context) -> {
                String[] arguments = new String[args.size()];
                for (int i = 0; i < args.size(); i++) {
                    if (argumentList[i] instanceof ArgumentEntity argumentEntity) {
                        EntityFinder finder = context.get(argumentEntity);
                        //finder.find(sender);//<- all entities found
                        Player targetPlayer = finder.findFirstPlayer(sender);
                        assert targetPlayer != null;
                        arguments[i] = targetPlayer.getUsername();
                    } else {
                        arguments[i] = context.get(argumentList[i]).toString();
                    }
                }
                command.execute(new PlayerAdapter((Player) sender), arguments);
            }, argumentList);
        }else{
            setDefaultExecutor((sender, commandContext) -> {
                command.execute(new PlayerAdapter((Player) sender), new String[]{});
            });
        }
        List<lib.commands.abstractions.Command> subCommands = command.getSubCommands();
        for (lib.commands.abstractions.Command subCommand : subCommands) {
            addSubcommand(new commandMinestom(subCommand));
        }
    }
}
