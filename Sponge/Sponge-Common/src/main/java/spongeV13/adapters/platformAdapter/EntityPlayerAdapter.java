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

package spongeV13.adapters.platformAdapter;

import abstractions.platform.recipes.RecipeInventory;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.player.IEntityHardCash;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import com.blockdynasty.economy.platform.IPlayer;
import spongeV13.adapters.GUI.adapters.InventoryAdapter;
import spongeV13.adapters.GUI.listener.ClickListener;
import spongeV13.adapters.GUI.listener.CloseListener;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IInventory;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.type.ViewableInventory;

import java.util.UUID;

public class EntityPlayerAdapter implements IPlayer {
    private ServerPlayer player;

    private EntityPlayerAdapter(ServerPlayer player) {
        this.player = player;
    }

    public static EntityPlayerAdapter of(ServerPlayer player) {
        return new EntityPlayerAdapter(player);
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void kickPlayer(String message) {
        player.kick(Component.text(message));
    }

    @Override
    public UUID getUniqueId() {
        return player.uniqueId();
    }

    @Override
    public String getName() {
        return player.name();
    }

    @Override
    public void sendMessage(String message) {
        //Component component = Component.text()
        //        .append(LegacyComponentSerializer.legacyAmpersand().deserialize(message))
        //        .build();

        Component textonuevo = MiniMessage.miniMessage().deserialize(message);
        player.sendMessage(textonuevo);
    }

    @Override
    public void playNotificationSound() {
        Sound sound = Sound.sound(
                org.spongepowered.api.ResourceKey.resolve("minecraft:block.note_block.pling"),
                Sound.Source.PLAYER,
                1.0f,
                1.5f
        );
        player.playSound(sound);
    }

    @Override
    public void playSuccessSound() {
        Sound sound = Sound.sound(
                org.spongepowered.api.ResourceKey.resolve("minecraft:ui.button.click"),
                Sound.Source.PLAYER,
                0.3f,
                1.0f
        );
        player.playSound(sound);
    }

    @Override
    public void playFailureSound() {
        Sound sound = Sound.sound(
                org.spongepowered.api.ResourceKey.resolve("minecraft:block.note_block.pling"),
                Sound.Source.PLAYER,
                0.3f,
                0.5f
        );
        player.playSound(sound);
    }

    @Override
    public void closeInventory() {
        player.closeInventory();
    }

    @Override
    public void openInventory(IInventory inventory) {
        InventoryAdapter inventoryAdapter = (InventoryAdapter) inventory;
        RecipeInventory recipeInventory = inventoryAdapter.getRecipe();
        if (inventory.getHandle() instanceof ViewableInventory) {
            ViewableInventory spongeInventory = (ViewableInventory) inventory.getHandle();
            InventoryMenu menu = spongeInventory.asMenu();

            menu.setReadOnly(true).setTitle(Component.text(recipeInventory.getTitle()));
            menu.registerSlotClick(new ClickListener());
            menu.registerClose(new CloseListener());

            menu.open(player);
        } else {
            throw new IllegalArgumentException("Invalid inventory type provided");
        }
    }

    @Override
    public IEntityCommands asEntityCommands() {
        return this;
    }

    @Override
    public IEntityHardCash asEntityHardCash() {
        return this;
    }

    @Override
    public IEntityGUI asEntityGUI() {
        return this;
    }

    @Override
    public Object getRoot() {
        return player;
    }

    @Override
    public void giveItem(ItemStackCurrency item) {
        player.inventory().primary().offer((ItemStack)item.getRoot());
    }

    @Override
    public ItemStackCurrency takeHandItem() {
        return new ItemStackCurrencyAdapter(player.itemInHand(HandTypes.MAIN_HAND));
    }

    @Override
    public boolean hasItem(ItemStackCurrency itemCurrency) {
        ItemStack item = (ItemStack) itemCurrency.getRoot();
        return player.inventory().primary().contains(item);
    }

    @Override
    public boolean hasEmptySlot() {
        return player.inventory().primary().freeCapacity() > 0;
    }

    @Override
    public void removeItem(ItemStackCurrency itemCurrency) {
        ItemStack item = (ItemStack) itemCurrency.getRoot();
        Inventory inv = player.inventory().primary();
        for (Slot slot : inv.slots()) {
           if (slot.peek().equalTo(item)) {
                slot.clear();
                break;
           }
        }
    }
}
