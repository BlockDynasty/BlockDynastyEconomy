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

package spongeV13.adapters.GUI.listener;

import spongeV13.adapters.platformAdapter.EntityPlayerAdapter;
import lib.gui.GUISystem;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.menu.ClickType;
import org.spongepowered.api.item.inventory.menu.ClickTypes;
import org.spongepowered.api.item.inventory.menu.handler.SlotClickHandler;

public class ClickListener implements SlotClickHandler {

    @Override
    public boolean handle(Cause cause, Container container, Slot slot, int slotIndex, ClickType<?> clickType) {
        cause.first(ServerPlayer.class).ifPresent(player -> {
            if (ClickTypes.CLICK_LEFT.get().equals(clickType)){
                GUISystem.handleClick(EntityPlayerAdapter.of(player), lib.gui.components.ClickType.LEFT, slotIndex);
            } else if (ClickTypes.CLICK_RIGHT.get().equals(clickType)) {
                GUISystem.handleClick(EntityPlayerAdapter.of(player), lib.gui.components.ClickType.RIGHT, slotIndex);
            }
        });
        return true; // true = cancelar acción
    }
}