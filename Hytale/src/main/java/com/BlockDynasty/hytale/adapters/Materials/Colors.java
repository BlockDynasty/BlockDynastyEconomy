package com.BlockDynasty.hytale.adapters.Materials;

import java.awt.*;
import java.util.Map;

public class Colors {
    public static final Map<String, String> HEXADECIMAL = Map.ofEntries(
            Map.entry("§0", "#000000"),
            Map.entry("§1", "#0000AA"),
            Map.entry("§2", "#00AA00"),
            Map.entry("§3", "#00AAAA"),
            Map.entry("§4", "#AA0000"),
            Map.entry("§5", "#AA00AA"),
            Map.entry("§6", "#FFAA00"),
            Map.entry("§7", "#EBEBEB"),
            Map.entry("§8", "#555555"),
            Map.entry("§9", "#5555FF"),
            Map.entry("§a", "#55FF55"),
            Map.entry("§b", "#55FFFF"),
            Map.entry("§c", "#FF5555"),
            Map.entry("§d", "#FF55FF"),
            Map.entry("§e", "#FFFF55"),
            Map.entry("§f", "#FFFFFF"),
            Map.entry("§r", "#FFFFFF")
    );

    public static Color decode(String minecraftColor){
        String hex = HEXADECIMAL.getOrDefault(minecraftColor, "#FFFFFF");
        return Color.decode(hex);
    }
}
