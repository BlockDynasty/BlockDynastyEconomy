package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.NBTData;

import de.tr7zw.changeme.nbtapi.NBT;
import domain.entity.currency.NbtData;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class NBTApi implements NBTService{

    @Override
    public void applyNBTData(ItemStack itemStack, NbtData nbtData) {
        NBT.modify(itemStack, nbt ->{
            Map<String ,String> nbtMap = nbtData.getNbtMap();
            for (Map.Entry<String, String> entry : nbtMap.entrySet()) {
                nbt.setString(entry.getKey(), entry.getValue());
            }
        });
    }

    @Override
    public NbtData getNBTData(ItemStack itemStack) {
        NbtData nbtData = new NbtData();
        Map<String ,String> nbtMap = new HashMap<>();
        NBT.get(itemStack, nbt -> {
            for (String key : nbtData.getNbtMap().keySet()) {
                String value = nbt.getString(key);
                nbtMap.put(key, value);
            }
        });
        return new NbtData(nbtMap);
    }
}
