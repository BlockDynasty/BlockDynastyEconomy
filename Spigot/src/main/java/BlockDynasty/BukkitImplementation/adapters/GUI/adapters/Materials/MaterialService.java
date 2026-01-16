package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.Materials;

import abstractions.platform.materials.Materials;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface MaterialService {
    Map<Materials, Material> getMaterialMap();
    Material toBukkitMaterial(Materials material);
    boolean isPlayerHead(Material material);
    ItemStack createItemStack(Materials materials);
}
