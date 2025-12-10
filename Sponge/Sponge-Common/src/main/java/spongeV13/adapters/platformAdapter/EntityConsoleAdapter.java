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

import lib.commands.abstractions.IEntityCommands;
import lib.gui.components.IEntityGUI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class EntityConsoleAdapter implements IEntityCommands {
    private final Audience console;

    private EntityConsoleAdapter(Audience console) {
        this.console = console;
    }

    public static EntityConsoleAdapter of(Audience console) {
        return new EntityConsoleAdapter(console);
    }

    @Override
    public void sendMessage(String message) {
        console.sendMessage(Component.text(translateColorCodes(message)));
    }

    private String translateColorCodes(String message) {
        return message.replaceAll("&([0-9a-fk-or])", "");
    }

    @Override
    public void playNotificationSound() {

    }

    @Override
    public String getName() {
        return "BlockDynasty";
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public boolean hasPermission(String permission) {
        // Console typically has all permissions
        return true;
    }

    @Override
    public void kickPlayer(String message) {
    }

    @Override
    public IEntityGUI asEntityGUI() {
        return null;
    }

    @Override
    public Object getRoot() {
        return console;
    }

}
