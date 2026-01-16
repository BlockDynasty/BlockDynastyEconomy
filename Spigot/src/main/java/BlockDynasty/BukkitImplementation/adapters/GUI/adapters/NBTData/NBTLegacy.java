package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.NBTData;

import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.NBTData.NBTLegacyReflection.NBTItem;
import domain.entity.currency.NbtData;
import org.bukkit.inventory.ItemStack;


import java.util.Map;

//with NMS implementation for legacy versions
public class NBTLegacy implements NBTService{

    @Override
    public void applyNBTData(ItemStack itemStack, NbtData nbtData) {
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtData.getNbtMap().forEach(nbtItem::setString);
    }

    @Override
    public NbtData getNBTData(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        NbtData nbtData = new NbtData();
        Map<String ,String> nbtMap = nbtData.getNbtMap();
        nbtMap.keySet().forEach(key ->{
            String value = nbtItem.getString(key);
            nbtMap.put(key, value);
        });
        return new NbtData(nbtMap);
    }
}
