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

public class ChatColor {
    private static ChatColorProvider chatColorProvider;
    //default to modern
    static {
        chatColorProvider = new ChatColorModern();
    }

    public static void setupSystem(boolean supportModern, boolean forceVanillaColorSystem) {
        if(!supportModern || forceVanillaColorSystem){
            ChatColor.setupVanilla();
        }else
        {
            ChatColor.setupModern();
        }
    }

    private static void setupVanilla(){
        chatColorProvider = new ChatColorVanilla();
    }
    private static void setupModern(){
        chatColorProvider = new ChatColorModern();
    }

    public static String stringValueOf(Colors color) {
        return chatColorProvider.stringValueOf(color);
    }
    public static String stringValueOf(String color) {
        return chatColorProvider.stringValueOf(color);
    }

    public static String formatColorToPlaceholder(String string) {
        return chatColorProvider.formatColorToPlaceholder(string);
    }
}
