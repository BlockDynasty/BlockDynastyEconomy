package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.NBTData;

import domain.entity.currency.NbtData;
import org.bukkit.inventory.ItemStack;

public class NBTNull implements  NBTService{

    @Override
    public void applyNBTData(ItemStack itemStack, NbtData nbtData) {

    }

    @Override
    public NbtData getNBTData(ItemStack itemStack) {
        return new NbtData();
    }

}
