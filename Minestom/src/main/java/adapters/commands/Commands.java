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

import lib.commands.CommandService;
import lib.commands.abstractions.Command;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;

import java.util.List;

public class Commands {

    public static void register(){
        CommandManager manager = MinecraftServer.getCommandManager();
        manager.setUnknownCommandCallback((sender, c) -> sender.sendMessage("Command not found"));
        //por cada comando principal contenido en la api de economia vamos a hacer un new CommandMinestrom(command)
        List<Command> commands = CommandService.getMainCommands();
        for (Command command : commands) {
            manager.register(new commandMinestom(command));
        }
    }
}
