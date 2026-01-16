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

package adapters;

import DynastyEconomy.Console;
import abstractions.platform.IConsole;
import abstractions.platform.IProxySubscriber;
import abstractions.platform.recipes.RecipeItem;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.RecipeItemCurrency;
import domain.entity.platform.HardCashCreator;
import lib.gui.components.IInventory;
import lib.gui.components.IItemStack;
import lib.gui.components.ITextInput;
import abstractions.platform.recipes.RecipeInventory;
import abstractions.platform.scheduler.IScheduler;
import adapters.inventory.MinestomInventory;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import platform.IPlatform;
import platform.IPlayer;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlatformAdapter implements IPlatform {
    public static boolean onlineMode = false;
    public PlatformAdapter(boolean onlineMode) {
        PlatformAdapter.onlineMode=onlineMode;
    }
    @Override
    public IPlayer getPlayer(String name) {
        Player p = MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(name);
        if (p == null) {
            return null;
        }
        return new PlayerAdapter(p);
    }

    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        Player p = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
        if (p == null) {
            return null;
        }
        return new PlayerAdapter(p);
    }

    @Override
    public List<abstractions.platform.entity.IPlayer> getOnlinePlayers() {
        Collection<Player> players = MinecraftServer.getConnectionManager().getOnlinePlayers();
        return players.stream().map(PlayerAdapter::new).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public IItemStack createItemStack(RecipeItem recipeItem) {
        ItemStack item= MaterialAdapter.createItem(recipeItem);
        return new ItemStackAdapter(item);
    }

    @Override
    public IInventory createInventory(RecipeInventory recipeInventory) {
        return new InventoryAdapter(new MinestomInventory(recipeInventory));
    }

    //enviar un comando a la consola del servidor
    @Override
    public void dispatchCommand(String command) throws Exception {
//not implemented
    }

    @Override
    public void sendPluginMessage(String channel, byte[] message) {
//not implemented
    }

    @Override
    public void registerMessageChannel(IProxySubscriber subscriber) {
        //not implemented
    }

    @Override
    public IScheduler getScheduler() {
        return new SchedulerAdapter();
    }

    @Override
    public IConsole getConsole() {
        return new Console();
    }

    @Override
    public File getDataFolder() {
        return new File("BlockDynastyEconomy"); //verificar ruta utilizada por complementos de Minestom
    }

    @Override
    public boolean isLegacy() {
        return false;
    }

    @Override
    public boolean isOnlineMode() {
        return onlineMode;
    }

    @Override
    public boolean hasSupportAdventureText() {
        return false;
    }

    @Override
    public ITextInput getTextInput() {
        return new TextInput();
    }

    @Override
    public HardCashCreator asPlatformHardCash() {
        return this;
    }

    @Override
    public ItemStackCurrency createItemStackCurrency(RecipeItemCurrency recipe) {
        return null;
    }

    @Override
    public boolean hasSupportHardCash() {
        return false;
    }

    @Override
    public boolean hasSupportGui() {
        return true;
    }
}
