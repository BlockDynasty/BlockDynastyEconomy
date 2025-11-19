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

package lib.abstractions;

import lib.gui.components.IInventory;
import lib.gui.components.IItemStack;
import lib.gui.components.ITextInput;
import lib.gui.components.RecipeItem;
import lib.util.materials.Materials;
import lib.scheduler.IScheduler;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface PlatformAdapter {
    IPlayer getPlayer(String name);
    IPlayer getPlayerByUUID(UUID uuid);
    List<IPlayer> getOnlinePlayers();

    @Deprecated
    IItemStack createItemStack(Materials material);

    IItemStack createItemStack(RecipeItem recipeItem);
    IInventory createInventory(String title, int rows);
    void dispatchCommand(String command) throws Exception;

    void sendPluginMessage(String channel, byte[] message);
    IScheduler getScheduler();
    IConsole getConsole();
    File getDataFolder();
    boolean isLegacy();
    boolean hasSupportAdventureText();
    ITextInput getTextInput();
}
