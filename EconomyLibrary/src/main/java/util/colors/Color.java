/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package util.colors;

public class Color {
    private Colors color;
    private String stringColor;

    private Color(Colors color) {
        this.color = color;
    }
    private Color(String color) {
        this.stringColor = color;
    }

    public static Color of(Colors color){
        return new Color(color);
    }
    public static Color of(String color){
        return new Color(color);
    }

    public String formatToMinimessage(){
        return "<"+getHexadecimal()+">";
    }

    public String getHexadecimal(){
        if(color != null){
            stringColor = color.name();
        }
        switch (stringColor.toUpperCase()){
            case "BLACK": return "#000000";
            case "DARK_BLUE": return "#0000AA";
            case "DARK_GREEN": return "#00AA00";
            case "DARK_AQUA": return "#00AAAA";
            case "DARK_RED": return "#AA0000";
            case "DARK_PURPLE": return "#AA00AA";
            case "GOLD": return "#FFAA00";
            case "GRAY": return "#C4C4C4";
            case "DARK_GRAY": return "#555555";
            case "BLUE": return "#5555FF";
            case "GREEN": return "#55FF55";
            case "AQUA": return "#55FFFF";
            case "RED": return "#FF5555";
            case "LIGHT_PURPLE": return "#FF55FF";
            case "YELLOW": return "#FFFF55";
            case "WHITE": return "#FFFFFF";
            default: return stringColor;
        }
    }

    public String getVanillaFormat(){
        if(color != null){
            stringColor = color.name();
        }
        if(stringColor.startsWith("#")){
            return "§f"; // No vanilla support for hex colors
        }
        switch (stringColor.toUpperCase()){
            case "BLACK": return "§0";
            case "DARK_BLUE": return "§1";
            case "DARK_GREEN": return "§2";
            case "DARK_AQUA": return "§3";
            case "DARK_RED": return "§4";
            case "DARK_PURPLE": return "§5";
            case "GOLD": return "§6";
            case "GRAY": return "§7";
            case "DARK_GRAY": return "§8";
            case "BLUE": return "§9";
            case "GREEN": return "§a";
            case "AQUA": return "§b";
            case "RED": return "§c";
            case "LIGHT_PURPLE": return "§d";
            case "YELLOW": return "§e";
            case "WHITE": return "§f";
            default: return stringColor;
        }
    }

}
