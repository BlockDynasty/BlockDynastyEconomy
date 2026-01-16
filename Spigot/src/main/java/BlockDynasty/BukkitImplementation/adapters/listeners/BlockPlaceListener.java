package BlockDynasty.BukkitImplementation.adapters.listeners;

import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.Materials.MaterialProvider;
import BlockDynasty.BukkitImplementation.adapters.platformAdapter.ItemStackCurrencyAdapter;
import aplication.listener.CustomHeadValidator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack itemStack = event.getItemInHand();
        if(!MaterialProvider.isPlayerHead(itemStack.getType())) {
            return;
        }
        if(CustomHeadValidator.isACurrency(new ItemStackCurrencyAdapter(itemStack))){
            event.setCancelled(true);
        }
    }
}
