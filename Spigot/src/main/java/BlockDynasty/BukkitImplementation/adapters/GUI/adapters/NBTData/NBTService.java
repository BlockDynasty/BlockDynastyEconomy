package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.NBTData;

import domain.entity.currency.NbtData;
import org.bukkit.inventory.ItemStack;

public interface NBTService {
    void applyNBTData(ItemStack itemStack, NbtData nbtData);
    NbtData getNBTData(ItemStack itemStack);
}
