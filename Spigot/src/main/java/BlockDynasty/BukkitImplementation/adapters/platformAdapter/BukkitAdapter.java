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

package BlockDynasty.BukkitImplementation.adapters.platformAdapter;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.InventoryAdapter;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.ItemStackAdapter;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.MaterialAdapter;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.textInput.TextInputFactory;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.BukkitImplementation.scheduler.SchedulerFactory;
import BlockDynasty.BukkitImplementation.utils.Version;
import lib.abstractions.IConsole;
import lib.abstractions.IPlayer;
import lib.abstractions.PlatformAdapter;
import lib.gui.components.IInventory;
import lib.gui.components.IItemStack;
import lib.gui.components.ITextInput;
import lib.gui.components.RecipeItem;
import lib.util.materials.Materials;
import lib.scheduler.ContextualTask;
import lib.scheduler.IScheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


//permite explicarle a la libreria como obtener un jugador y como ejecutar un comando
public class BukkitAdapter implements PlatformAdapter {

    @Override
    public IPlayer getPlayer(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            return null;
        }
        return EntityPlayerAdapter.of(player);
    }

    @Override
    public void dispatchCommand(String command) {
        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        Scheduler.run(ContextualTask.build(()->Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)));
    }

    @Override
    public void sendPluginMessage(String channel, byte[] message) {
        Bukkit.getServer().sendPluginMessage(BlockDynastyEconomy.getInstance(), channel, message);
    }

    @Override
    public IScheduler getScheduler() {
        return SchedulerFactory.getScheduler();
    }

    @Override
    public IConsole getConsole() {
        return new ConsoleAdapter();
    }

    @Override
    public File getDataFolder() {
        return BlockDynastyEconomy.getInstance().getDataFolder();
    }

    @Override
    public boolean isLegacy() {
        return Version.isLegacy();
    }

    @Override
    public boolean hasSupportAdventureText() {
        return Version.hasSupportAdventureText();
    }


    @Override
    public IItemStack createItemStack(Materials material) {
        ItemStack itemStack = MaterialAdapter.createItemStack(material);
        return new ItemStackAdapter(itemStack);
    }

    @Override
    public IItemStack createItemStack(RecipeItem recipeItem) {
        ItemStack itemStack = MaterialAdapter.createItemStack(recipeItem);
        return new ItemStackAdapter(itemStack);
    }

    @Override
    public IInventory createInventory(String title, int rows) {
        Inventory inventory = Bukkit.createInventory(null, rows * 9, title);
        return new InventoryAdapter(inventory);
    }

    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        }
        return EntityPlayerAdapter.of(player);
    }

    @Override
    public List<IPlayer> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream().map(EntityPlayerAdapter::of).collect(Collectors.toList());
    }

    @Override
    public ITextInput getTextInput() {
        return TextInputFactory.getTextInput();
    }
}
