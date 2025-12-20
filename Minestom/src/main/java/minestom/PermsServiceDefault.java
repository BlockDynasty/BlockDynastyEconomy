package minestom;

import adapters.PermissionsService;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermsServiceDefault implements PermissionsService {
    //can be a general tree structure for manage permissions parents/children
    private final static Map<Player, List<String>> playerPermissions = new HashMap<>();

    public static void addPermission(Player player, String permission) {
        playerPermissions.computeIfAbsent(player, k -> new ArrayList<>()).add(permission);
    }

    public static void removePermission(Player player, String permission) {
        List<String> permissions = playerPermissions.get(player);
        if (permissions != null) {
            permissions.remove(permission);
        }
    }

    @Override
    public boolean hasPermission(Player player, String permission) {
        return playerPermissions.getOrDefault(player, new ArrayList<>()).contains(permission);
    }
}
