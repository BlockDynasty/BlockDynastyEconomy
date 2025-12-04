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

package BlockDynasty.adapters.platformAdapter;

import BlockDynasty.adapters.GUI.adapters.InventoryAdapter;
import BlockDynasty.adapters.GUI.adapters.ItemStackAdapter;
import BlockDynasty.adapters.GUI.adapters.MaterialAdapter;
import BlockDynasty.SpongePlugin;
import BlockDynasty.adapters.GUI.adapters.TextInput;
import BlockDynasty.adapters.scheduler.Scheduler;
import lib.abstractions.IConsole;
import lib.abstractions.IPlayer;
import lib.abstractions.PlatformAdapter;
import lib.gui.components.*;
import lib.gui.components.recipes.RecipeInventory;
import lib.gui.components.recipes.RecipeItem;
import lib.util.materials.Materials;
import lib.scheduler.IScheduler;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.ContainerType;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.ViewableInventory;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.api.network.channel.raw.RawDataChannel;
import org.spongepowered.api.network.channel.raw.play.RawPlayDataChannel;
import org.spongepowered.plugin.PluginContainer;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpongeAdapter implements PlatformAdapter {
    private PluginContainer pluginContainer = SpongePlugin.getPlugin();
    private RawDataChannel channel = SpongePlugin.getChannel();

    @Override
    public IPlayer getPlayer(String name) {
        return Sponge.server().player(name).map(EntityPlayerAdapter::of).orElse(null);
    }
    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        return Sponge.server().player(uuid).map(EntityPlayerAdapter::of).orElse(null);
    }

    @Override
    public List<IPlayer> getOnlinePlayers() {
        return Sponge.server().onlinePlayers().stream()
                .map(EntityPlayerAdapter::of)
                .collect(Collectors.toList());
    }

    @Override
    public void dispatchCommand(String command) throws Exception {
        Sponge.server().commandManager().process(command);
    }

    @Override
    public void sendPluginMessage(String channel, byte[] message) {
        Collection<ServerPlayer> players = Sponge.server().onlinePlayers();
        ServerPlayer player = players.stream().findFirst().orElse(null);

        if (player != null) {
            RawPlayDataChannel playChannel = SpongePlugin.getChannel().play();
            playChannel.sendTo(player, (ChannelBuf buf) -> {
                buf.writeBytes(message);
            });
        }
    }

    @Override
    public IScheduler getScheduler() {
        return new Scheduler();
    }

    @Override
    public IConsole getConsole() {
        return new ConsoleAdapter();
    }

    @Override
    public File getDataFolder() {
        return SpongePlugin.getConfigPath().toFile();
    }

    @Override
    public boolean isLegacy() {
        return false;
    }

    @Override
    public boolean isOnlineMode() {
        return Sponge.server().isOnlineModeEnabled();
    }

    @Override
    public boolean hasSupportAdventureText() {
        return true;
    }

    @Override
    public ITextInput getTextInput() {
        return new TextInput();
    }

    @Override
    public IItemStack createItemStack(RecipeItem recipeItem){
        ItemStack itemStack = MaterialAdapter.createItemStack(recipeItem);
        return new ItemStackAdapter(itemStack);
    }

    @Override
    public IInventory createInventory(RecipeInventory recipeInventory) {
        ViewableInventory viewableInventory= ViewableInventory.builder()
                .type(getTypeFromRows(recipeInventory.getRows()))
                .completeStructure()
                .plugin(this.pluginContainer)
                .build();
        return new InventoryAdapter(viewableInventory,recipeInventory);
    }


    private ContainerType getTypeFromRows(int rows) {
        switch (rows) {
            case 1:
                return ContainerTypes.GENERIC_9X1.get();
            case 2:
                return ContainerTypes.GENERIC_9X2.get();
            case 3:
                return ContainerTypes.GENERIC_9X3.get();
            case 4:
                return ContainerTypes.GENERIC_9X4.get();
            case 5:
                return ContainerTypes.GENERIC_9X5.get();
            case 6:
                return ContainerTypes.GENERIC_9X6.get();
            default:
                throw new IllegalArgumentException("Invalid number of rows: " + rows);
        }
    }

}
