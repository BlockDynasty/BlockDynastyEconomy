package adapters;

public class PermsService {
    private static PermissionsService permsService;

    public static void setPermissionsService(PermissionsService service) {
        permsService = service;
    }

    public static PermissionsService getPermissionsService() {
        return permsService;
    }

}
