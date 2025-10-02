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

package lib.gui.components;

import lib.commands.abstractions.IEntityCommands;

import java.util.UUID;

public interface IEntityGUI {
    UUID getUniqueId();
    String getName();
    void sendMessage(String message);
    Object getRoot();

    void playSuccessSound();
    void playFailureSound();

    void closeInventory();
    void openInventory(IInventory inventory);

    IEntityCommands asEntityCommands();
}