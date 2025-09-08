package BlockDynasty.BukkitImplementation.adapters.commands;

import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.PlayerAdapter;
import lib.commands.abstractions.Source;
import lib.gui.abstractions.IPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

//Adapter para CommandSender que son jugadores
public class SourceAdapter implements Source {
    Player player;

    public SourceAdapter(Player player) {
        this.player = player;
    }
    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public void sendMessage(String message) {
        //player.sendMessage(message);
        player.sendMessage(translateColorCodes(message));
    }

    private String translateColorCodes(String message) {
        return message.replaceAll("&([0-9a-fk-or])", "§$1");
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void kickPlayer(String message) {
        player.kickPlayer(message);
    }

    @Override
    public Object getHandle() {
        return player;
    }

    @Override
    public IPlayer asIPlayer() {
        return new PlayerAdapter(player);
    }
}
