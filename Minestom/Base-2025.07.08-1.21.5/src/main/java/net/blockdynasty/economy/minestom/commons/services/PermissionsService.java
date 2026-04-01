package net.blockdynasty.economy.minestom.commons.services;

import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

public interface PermissionsService {
    boolean hasPermission(Player player, String permission);
    boolean hasPermission(CommandSender commandSender, String permission);
}
