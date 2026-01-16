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

package BlockDynasty.BukkitImplementation.adapters.platformAdapter;

import abstractions.platform.IConsole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleAdapter implements IConsole {
    private final static ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    private static final String Debug_Prefix = "§3[BlockDynastyEconomy-Debug] §f";
    private static final String Console_Prefix = "§2[BlockDynastyEconomy] §f";
    private static final String Error_Prefix = "§c[BlockDynastyEconomy-Error] §f";

    public void debug(String message) {
         console.sendMessage(Debug_Prefix + colorize(message));
    }

    public void log(String message){
        console.sendMessage(Console_Prefix + colorize(message));
    }

    public void logError(String message){
        console.sendMessage(Error_Prefix + message);
    }

    private static String colorize(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}

