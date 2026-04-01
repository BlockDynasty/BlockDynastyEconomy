package net.blockdynasty.economy.minestom.commons.adapters.commands;

import net.blockdynasty.economy.minestom.commons.adapters.PermsService;
import net.blockdynasty.economy.minestom.commons.services.PermissionsService;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.condition.CommandCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PermissionCondition implements CommandCondition {
    private final String permission;
    private final PermissionsService permsService = PermsService.getPermissionsService();

    public PermissionCondition(String permission) {
        this.permission = permission;
    }


    @Override
    public boolean canUse(@NotNull CommandSender commandSender, @Nullable String s) {
        return permsService.hasPermission(commandSender, permission);
    }
}
