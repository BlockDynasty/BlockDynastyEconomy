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

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import lib.abstractions.IConfigurationGUI;
import services.messages.IMessages;
import lib.gui.components.PlatformGUI;
import lib.gui.components.*;
import lib.gui.components.factory.Inventory;
import lib.gui.components.factory.Item;
import services.messages.Message;

import java.util.UUID;

public class GUISystem {
    private static final IGUIService guiService = new GUIService();

    public static void init(UseCaseFactory useCaseFactory, PlatformGUI adapter, IMessages messages, IConfigurationGUI config) {
        Item.init(adapter);
        Inventory.init(adapter);
        Message.addLang(messages);
        GUIFactory.init(useCaseFactory, adapter, config);
    }


    public static void registerGUI(IEntityGUI entity,IGUI gui){
        GUISystem.guiService.registerGUI(entity,gui);
    }

    public static void unregisterGUI(IEntityGUI entity){
        GUISystem.guiService.unregisterGUI(entity);
    }

    public static void refresh(UUID playerUUID){
        GUISystem.guiService.refresh(playerUUID);
    }

    public static void handleClick(IEntityGUI player, ClickType clickType, int slot){
        GUISystem.guiService.handleClick(player, clickType, slot);
    }

    public static boolean hasOpenedGUI(IEntityGUI entity){
        return GUISystem.guiService.hasOpenedGUI(entity);
    }
}
