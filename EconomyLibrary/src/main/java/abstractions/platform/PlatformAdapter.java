package abstractions.platform;

import abstractions.platform.entity.IPlayer;
import abstractions.platform.scheduler.IScheduler;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface PlatformAdapter {
    void sendPluginMessage(String channel, byte[] message);
    void registerMessageChannel(IProxySubscriber subscriber);
    IScheduler getScheduler();
    IConsole getConsole();
    File getDataFolder();
    boolean isLegacy();
    boolean isOnlineMode();
    boolean hasSupportAdventureText();
    IPlayer getPlayer(String name);
    IPlayer getPlayerByUUID(UUID uuid);
    List<IPlayer> getOnlinePlayers();

}
