package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.Materials;

import abstractions.platform.materials.Materials;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MaterialLegacy implements MaterialService {
    private static final Material FALLBACK = Material.STONE;
    private final Map<Materials,Material> MATERIAL_MAP;

    public MaterialLegacy() {
        this.MATERIAL_MAP = getMaterialMap();
    }

    @Override
    public Map<Materials, Material> getMaterialMap() {

        Map<Materials, Material> MATERIAL_MAP = new HashMap<Materials, Material>();
        for (Materials material : Materials.values()) {
            if (material == Materials.PLAYER_HEAD) {
                MATERIAL_MAP.put(material, Material.valueOf("SKULL_ITEM"));
                continue;
            }
            if (material.name().contains("WOOL")) {
                MATERIAL_MAP.put(material, Material.valueOf("WOOL"));
                continue;
            }
            if (material == Materials.LIME_DYE) {
                MATERIAL_MAP.put(material, Material.SLIME_BALL);
                continue;
            }
            if (material == Materials.LIME_CONCRETE) {
                MATERIAL_MAP.put(material, Material.EMERALD_BLOCK);
                continue;
            }
            if (material == Materials.RED_CONCRETE) {
                MATERIAL_MAP.put(material, Material.REDSTONE_BLOCK);
                continue;
            }
            if (material == Materials.WRITABLE_BOOK) {
                MATERIAL_MAP.put(material, Material.valueOf("BOOK_AND_QUILL"));
                continue;
            }
            if (material == Materials.GLASS_PANE) {
                MATERIAL_MAP.put(material, Material.valueOf("THIN_GLASS"));
                continue;
            }
            if (material == Materials.BLUE_STAINED_GLASS_PANE) {
                MATERIAL_MAP.put(material, Material.valueOf("STAINED_GLASS_PANE"));
                continue;
            }

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
    public boolean isPlayerHead(Material material) {
        return material == Material.valueOf("SKULL_ITEM");
    }

    @Override
    public ItemStack createItemStack(Materials materials) {
        switch (materials) {
            case PLAYER_HEAD:
                return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
            case BLUE_STAINED_GLASS_PANE:
                return new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 11);
            default:
                if (materials.name().contains("WOOL")) {
                    return new ItemStack(Material.valueOf("WOOL"), 1, getLegacyDataValue(materials.name()));
                }
        }
        return new ItemStack(toBukkitMaterial(materials));
    }

    public Material toBukkitMaterial(Materials material) {
        return MATERIAL_MAP.getOrDefault(material, FALLBACK);
    }

    /**
     * Obtiene el valor de data (durabilidad) para wool en versiones legacy.
     */
    private static short getLegacyDataValue(String material) {
        switch (material) {
            case "WHITE_WOOL":
                return 0;
            case "ORANGE_WOOL":
                return 1;
            case "MAGENTA_WOOL":
                return 2;
            case "LIGHT_BLUE_WOOL":
                return 3;
            case "YELLOW_WOOL":
                return 4;
            case "LIME_WOOL":
                return 5;
            case "PINK_WOOL":
                return 6;
            case "GRAY_WOOL":
                return 7;
            case "LIGHT_GRAY_WOOL":
                return 8;
            case "CYAN_WOOL":
                return 9;
            case "PURPLE_WOOL":
                return 10;
            case "BLUE_WOOL":
                return 11;
            case "BROWN_WOOL":
                return 12;
            case "GREEN_WOOL":
                return 13;
            case "RED_WOOL":
                return 14;
            case "BLACK_WOOL":
                return 15;
            default:
                return 0; // Default a white wool
        }
    }
}
