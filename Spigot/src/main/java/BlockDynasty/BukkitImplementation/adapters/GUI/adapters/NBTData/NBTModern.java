package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.NBTData;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import domain.entity.currency.NbtData;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

//with bukkit implementation for modern versions
public class NBTModern implements NBTService{

    @Override
    public void applyNBTData(ItemStack itemStack, NbtData nbtData) {
        Map<String ,String> nbtMap = nbtData.getNbtMap();
        ItemMeta meta = itemStack.getItemMeta();
        for (Map.Entry<String, String> entry : nbtMap.entrySet()) {
            NamespacedKey key = new NamespacedKey(BlockDynastyEconomy.getInstance(), entry.getKey());
            meta.getPersistentDataContainer().set(
                    key,
                    PersistentDataType.STRING,
                    entry.getValue()
            );
        }
        itemStack.setItemMeta(meta);
    }

    @Override
    public NbtData getNBTData(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return new NbtData();
        }
        NbtData nbtData = new NbtData();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        Map<String ,String> nbtMap = new HashMap<>();
        for (String key : nbtData.getNbtMap().keySet()) {
            String value = pdc.get(new NamespacedKey(BlockDynastyEconomy.getInstance(), key), PersistentDataType.STRING);
            nbtMap.put(key, value);
        }
        return new NbtData(nbtMap);
    }
}
