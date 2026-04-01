package net.blockdynasty.economy.minestom.commons.services;

import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PermsServiceDefault implements PermissionsService {
    private final static Map<UUID, List<String>> playerPermissions = new ConcurrentHashMap<>();
    private final static Map<UUID, Boolean> playerOps = new ConcurrentHashMap<>();

    public static void addPermission(Player player, String permission) {
        playerPermissions.computeIfAbsent(player.getUuid(), k -> new ArrayList<>()).add(permission);
    }

    public static void setOp(Player player, boolean isOp) {
        playerOps.put(player.getUuid(), isOp);
    }

    public static boolean isOp(Player player) {
        return playerOps.getOrDefault(player.getUuid(), false);
    }

    public static void removePermission(Player player, String permission) {
        List<String> permissions = playerPermissions.get(player.getUuid());
        if (permissions != null) {
            permissions.remove(permission);
        }
    }

    @Override
    public boolean hasPermission(Player player, String permission) {
        if (playerOps.getOrDefault(player.getUuid(), false)) {
            return true;
        }
        return playerPermissions.getOrDefault(player.getUuid(), new ArrayList<>()).contains(permission);
    }

    @Override
    public boolean hasPermission(CommandSender commandSender, String permission) {
        return commandSender instanceof Player player && hasPermission(player, permission);
    }
}
