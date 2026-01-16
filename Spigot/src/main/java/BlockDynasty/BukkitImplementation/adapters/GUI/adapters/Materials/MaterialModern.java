package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.Materials;

import abstractions.platform.materials.Materials;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MaterialModern implements MaterialService {
    private static final Material FALLBACK = Material.STONE;
    private final Map<Materials,Material> MATERIAL_MAP;

    public MaterialModern() {
        this.MATERIAL_MAP = getMaterialMap();
    }

    @Override
    public Map<Materials, Material> getMaterialMap() {
        Map<Materials, Material> MATERIAL_MAP = new HashMap<>();
        for (Materials material : Materials.values()) {
            try {
                Material bukkitMaterial = Material.valueOf(material.name());
                MATERIAL_MAP.put(material, bukkitMaterial);
            } catch (IllegalArgumentException e) {
                MATERIAL_MAP.put(material, FALLBACK);
            }
        }
        return MATERIAL_MAP;
    }

    @Override
    public Material toBukkitMaterial(Materials material) {
        return MATERIAL_MAP.getOrDefault(material, FALLBACK);
    }

    @Override
    public boolean isPlayerHead(Material material) {
        return material == Material.PLAYER_HEAD;
    }

    @Override
    public ItemStack createItemStack(Materials materials) {
        return new ItemStack(toBukkitMaterial(materials));
    }
}
