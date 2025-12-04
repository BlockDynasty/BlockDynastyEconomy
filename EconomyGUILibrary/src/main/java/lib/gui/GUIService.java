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

package lib.gui;

import lib.gui.components.ClickType;
import lib.gui.components.IGUI;
import lib.gui.components.IGUIService;
import lib.gui.components.IEntityGUI;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GUIService implements IGUIService {
    private final Map<UUID, IGUI> openGUIs = new ConcurrentHashMap<>();

    public void registerGUI(IEntityGUI player, IGUI gui) {
        openGUIs.put(player.getUniqueId(), gui);
    }

    public void unregisterGUI(IEntityGUI player) {
        openGUIs.remove(player.getUniqueId());
    }

    public boolean hasOpenedGUI(IEntityGUI player) {
        return openGUIs.containsKey(player.getUniqueId());
    }

    public void handleClick(IEntityGUI player, ClickType clickType, int indexSlot){
        IGUI gui = openGUIs.get(player.getUniqueId());
        if (gui != null) {
            if (ClickType.LEFT == clickType) {
                gui.handleLeftClick(indexSlot);
            } else if (ClickType.RIGHT == clickType) {
                gui.handleRightClick(indexSlot);

            }
        }
    }

    public void refresh(UUID playerId) {
        IGUI gui = openGUIs.get(playerId);
        if (gui != null) {
            gui.refresh();
        }
    }
}