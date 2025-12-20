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

import lib.abstractions.IPlayer;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IInventory;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.advancements.Notification;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.UUID;

public class PlayerAdapter implements IPlayer {
    private final Player player;

    public PlayerAdapter(Player player) {
        this.player = player;
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    @Override
    public boolean hasPermission(String permission) {
        return PermsService.getPermissionsService().hasPermission(player, permission);
    }

    @Override
    public void kickPlayer(String message) {
        player.kick(message);
    }

    @Override
    public IEntityGUI asEntityGUI() {
        return this;
    }

    @Override
    public UUID getUniqueId() {
        return player.getUuid();
    }

    @Override
    public String getName() {
        return player.getUsername();
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public Object getRoot() {
        return this.player;
    }

    @Override
    public void playNotificationSound() {
        player.playSound(Sound.sound().type(Key.key("minecraft:entity.experience_orb.pickup")).pitch(1.0f).volume(1.0f).source(Sound.Source.PLAYER).build());
        player.sendNotification(new Notification(
                Component.text("Transaction received", NamedTextColor.GREEN),
                FrameType.GOAL,
                ItemStack.of(Material.GOLD_INGOT)));
    }
    @Override
    public void playSuccessSound() {
        player.playSound(Sound.sound().type(Key.key("minecraft:ui.button.click")).pitch(1.0f).volume(0.3f).source(Sound.Source.PLAYER).build());
    }

    @Override
    public void playFailureSound() {
        player.playSound(Sound.sound().type(Key.key("minecraft:block.note_block.pling")).pitch(0.5f).volume(0.3f).source(Sound.Source.PLAYER).build());
    }

    @Override
    public void closeInventory() {
        player.closeInventory();
    }

    @Override
    public void openInventory(IInventory inventory) {
        player.openInventory( (net.minestom.server.inventory.Inventory) inventory.getHandle());
    }

    @Override
    public IEntityCommands asEntityCommands() {
        return this;
    }
}
