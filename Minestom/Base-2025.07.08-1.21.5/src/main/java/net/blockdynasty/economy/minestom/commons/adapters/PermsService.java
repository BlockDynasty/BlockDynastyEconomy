package net.blockdynasty.economy.minestom.commons.adapters;

import net.blockdynasty.economy.minestom.commons.services.PermissionsService;

public class PermsService {
    private static PermissionsService permsService;

    public static void setPermissionsService(PermissionsService service) {
        permsService = service;
    }

    public static PermissionsService getPermissionsService() {
        return permsService;
    }

}
