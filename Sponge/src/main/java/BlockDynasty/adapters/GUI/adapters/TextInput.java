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

package BlockDynasty.adapters.GUI.adapters;

import BlockDynasty.SpongePlugin;
import lib.gui.components.IGUI;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.ExecuteCommandEvent;
import org.spongepowered.api.event.message.PlayerChatEvent;

import java.util.function.Function;

public class TextInput implements ITextInput {
    @Override
    public void open(IEntityGUI owner, String title, String initialText, Function<String, String> function) {
        owner.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[" + title + "] "+ChatColor.stringValueOf(Colors.GRAY) + initialText);
        owner.sendMessage(ChatColor.stringValueOf(Colors.GRAY)+"Type your response in chat. Type"+ChatColor.stringValueOf(Colors.RED) +" cancel "+ ChatColor.stringValueOf(Colors.GRAY)+ "to cancel.");
        owner.closeInventory();

        TextInput.ChatInputListener listener = new TextInput.ChatInputListener(null,owner, title, function);

        Sponge.eventManager().registerListeners(SpongePlugin.getPlugin(), listener);
    }

    @Override
    public void open(IGUI parent, IEntityGUI owner, String title, String initialText, Function<String, String> function) {

        owner.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[" + title + "] "+ChatColor.stringValueOf(Colors.GRAY) + initialText);
        owner.sendMessage(ChatColor.stringValueOf(Colors.GRAY)+"Type your response in chat. Type"+ChatColor.stringValueOf(Colors.RED) +" cancel "+ ChatColor.stringValueOf(Colors.GRAY)+"to cancel. Type" +ChatColor.stringValueOf(Colors.RED) +" back "+ChatColor.stringValueOf(Colors.GRAY)+"to go back.");
        owner.closeInventory();

        TextInput.ChatInputListener listener = new TextInput.ChatInputListener(parent,owner, title, function);

        Sponge.eventManager().registerListeners(SpongePlugin.getPlugin(), listener);

    }

    @Override
    public ITextInput asInputChat() {
        return this;
    }

    public static class ChatInputListener {
        private final IGUI parent;
        private final IEntityGUI owner;
        private final String title;
        private final Function<String, String> function;

        public ChatInputListener(IGUI parent, IEntityGUI owner, String title, Function<String, String> function) {
            this.owner = owner;
            this.title = title;
            this.function = function;
            this.parent = parent;
        }

        @Listener(order = Order.EARLY)
        public void onChat(PlayerChatEvent.Submit event) {
            if (!(event.cause().root() instanceof ServerPlayer)) {
                return;
            }

            ServerPlayer player = (ServerPlayer) event.cause().root();
            if (!player.uniqueId().equals(owner.getUniqueId())) {
                return;
            }
            event.setCancelled(true);
            TextComponent component = (TextComponent) event.message();
            String input = component.content();
            if (input.equalsIgnoreCase("cancel")) {
                owner.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[" + title + "] "+ChatColor.stringValueOf(Colors.RED)+"Operation cancelled.");
                Sponge.eventManager().unregisterListeners(this);
                return;
            }
            if (parent != null) {
                if (input.equalsIgnoreCase("back")) {
                    parent.open();
                    Sponge.eventManager().unregisterListeners(this);
                    return;
                }
            }
            String response = function.apply(input);
            if (response == null) {
                Sponge.eventManager().unregisterListeners(this);
                return;
            }
            owner.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[" + title + "] "+ ChatColor.stringValueOf(Colors.GRAY)+ response);
            Sponge.eventManager().unregisterListeners(this);
        }

        @Listener(order = Order.EARLY)
        public void onCommand(ExecuteCommandEvent.Pre event) {
            Sponge.eventManager().unregisterListeners(this);
        }
    }
}
