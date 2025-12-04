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

import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.ITextInput;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class TextInput implements ITextInput {

    @Override
    public void open(IEntityGUI owner, String title, String initialText, Function<String, String> function) {
        owner.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[" + title + "] "+ChatColor.stringValueOf(Colors.GRAY) + initialText);
        owner.sendMessage(ChatColor.stringValueOf(Colors.GRAY)+"Type your response in chat. Type"+ChatColor.stringValueOf(Colors.RED) +" cancel "+ ChatColor.stringValueOf(Colors.GRAY)+ "to cancel.");
        owner.closeInventory();

        EventNode<@NotNull PlayerEvent> eventNode = EventNode.type("chatInputListener", EventFilter.PLAYER, ((event, player) -> {return player.getUuid().equals(owner.getUniqueId())&&event instanceof PlayerChatEvent;}));
        MinecraftServer.getGlobalEventHandler().addChild(registerListener(null,owner,function,title,eventNode));
    }

    @Override
    public void open(IGUI parent, IEntityGUI owner, String title, String initialText, Function<String, String> function) {
        owner.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[" + title + "] "+ChatColor.stringValueOf(Colors.GRAY) + initialText);
        owner.sendMessage(ChatColor.stringValueOf(Colors.GRAY)+"Type your response in chat. Type"+ChatColor.stringValueOf(Colors.RED) +" cancel "+ ChatColor.stringValueOf(Colors.GRAY)+"to cancel. Type" +ChatColor.stringValueOf(Colors.RED) +" back "+ChatColor.stringValueOf(Colors.GRAY)+"to go back.");
        owner.closeInventory();

        EventNode<@NotNull PlayerEvent> eventNode = EventNode.type("chatInputListener", EventFilter.PLAYER, ((event, player) -> {return player.getUuid().equals(owner.getUniqueId())&&event instanceof PlayerChatEvent;}));
        MinecraftServer.getGlobalEventHandler().addChild(registerListener(parent,owner,function,title,eventNode));
    }

    @Override
    public ITextInput asInputChat() {
        return this;
    }

    private static void handleText(String message, String title,Player player,IEntityGUI owner, IGUI parent,Function<String, String> function, EventNode<@NotNull PlayerEvent> eventNode) {
        if (!player.getUuid().equals(owner.getUniqueId())) {
            return;
        }

        if (message.equalsIgnoreCase("cancel")) {
            player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[" + title + "] "+ChatColor.stringValueOf(Colors.RED)+"Operation cancelled.");
            MinecraftServer.getGlobalEventHandler().removeChild(eventNode);
            return;
        }
        if (parent != null) {
            if (message.equalsIgnoreCase("back")) {
                parent.open();
                MinecraftServer.getGlobalEventHandler().removeChild(eventNode);
                return;
            }
        }
        String response = function.apply(message);
        if (response == null) {
            MinecraftServer.getGlobalEventHandler().removeChild(eventNode);
            return;
        }
        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[" + title + "] "+ ChatColor.stringValueOf(Colors.GRAY)+ response);
        MinecraftServer.getGlobalEventHandler().removeChild(eventNode);
    }

    private static EventNode<@NotNull PlayerEvent> registerListener(IGUI parent,IEntityGUI owner, Function<String, String> function, String title, EventNode<@NotNull PlayerEvent> eventNode) {
        eventNode.addListener(PlayerChatEvent.class, event -> {
            event.setCancelled(true);
            handleText(event.getRawMessage(),title,event.getPlayer(),owner,parent,function,eventNode);
        });
        return eventNode;
    }

}
