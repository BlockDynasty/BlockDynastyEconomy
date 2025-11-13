package BlockDynasty.BukkitImplementation.Integrations.Placeholder;

import BlockDynasty.BukkitImplementation.utils.Console;
import me.clip.placeholderapi.events.ExpansionsLoadedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlaceHolderListener implements Listener {
    private PlaceHolderExpansion expansion;

    public PlaceHolderListener(PlaceHolderExpansion expansion) {
        this.expansion = expansion;
    }

    @EventHandler
    public void onPAPIReload(ExpansionsLoadedEvent event) {
        if(!expansion.isRegistered()){
            expansion.register();
            Console.log("PlaceholderAPI Expansion re-registered successfully after reload!");
        }
    }
}
