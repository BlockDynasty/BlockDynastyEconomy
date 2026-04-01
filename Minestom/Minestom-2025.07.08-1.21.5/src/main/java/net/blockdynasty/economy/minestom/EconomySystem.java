package net.blockdynasty.economy.minestom;

import net.blockdynasty.economy.engine.platform.IPlatform;
import net.blockdynasty.economy.minestom.commons.EconomyHandler;
import net.blockdynasty.economy.minestom.commons.adapters.IMaterialAdapter;
import net.blockdynasty.economy.minestom.commons.adapters.MaterialAdapter;
import net.blockdynasty.economy.minestom.commons.adapters.PlatformAdapter;
import net.blockdynasty.economy.minestom.commons.services.PermissionsService;

public class EconomySystem {
    private static EconomyHandler economySystem;

    /**
     * Initializes the economy system with the provided online mode and permissions service.
     *
     * @param onlineMode        Whether the server is in online mode.
     * @param permissionsService The permissions service to use for permission checks.
     */

    public static void start(boolean onlineMode, PermissionsService permissionsService)
    {
        IMaterialAdapter materialAdapter = new MaterialAdapter();
        IPlatform platform = new PlatformAdapter(onlineMode, materialAdapter);
        EconomySystem.economySystem = new EconomyHandler(platform);

        economySystem.start(onlineMode, permissionsService);
    }

    /**
     * Initializes the economy system with the provided online mode, using a default permissions service.
     *
     * @param onlineMode Whether the server is in online mode.
     */
    public static void start(boolean onlineMode)
    {
        IMaterialAdapter materialAdapter = new MaterialAdapter();
        IPlatform platform = new PlatformAdapter(onlineMode, materialAdapter);
        EconomySystem.economySystem = new EconomyHandler(platform);

        economySystem.start(onlineMode);
    }

    /**
     * Stops the economy system, performing any necessary cleanup.
     */
    public static void stop()
    {
        if (economySystem != null) {
            economySystem.stop();
        }
    }

    /**
     * Checks if the economy system is currently started.
     * @return true if the economy system is started, false otherwise.
     */
    public static boolean isStarted()
    {
        if (economySystem != null) {
            return economySystem.isStarted();
        }
        return false;
    }
}
