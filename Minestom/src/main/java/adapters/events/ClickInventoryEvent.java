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

package adapters.events;

import adapters.PlayerAdapter;
import lib.gui.GUISystem;
import adapters.inventory.MinestomInventory;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.trait.InventoryEvent;
import org.jetbrains.annotations.NotNull;

public class ClickInventoryEvent {

    public static void register(){
        EventNode<@NotNull InventoryEvent> eventNode = EventNode.type("clickEconomyGUI", EventFilter.INVENTORY, (event, inv) -> inv instanceof MinestomInventory)
                .addListener(InventoryPreClickEvent.class, event -> {
                    event.setCancelled(true);
                    int slot = event.getSlot();

                    Class<?> clase = event.getClick().getClass();
                    String tipo = clase.getSimpleName();
                    if(tipo.equals("Left")){
                        GUISystem.handleClick(new PlayerAdapter(event.getPlayer()),lib.gui.components.ClickType.LEFT, slot);
                    }else if(tipo.equals("Right")){
                        GUISystem.handleClick(new PlayerAdapter(event.getPlayer()),lib.gui.components.ClickType.RIGHT, slot);
                    }
                })
                .addListener(InventoryCloseEvent.class, event -> {
                    GUISystem.unregisterGUI(new PlayerAdapter(event.getPlayer()));
                });

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addChild(eventNode);
    }
}
