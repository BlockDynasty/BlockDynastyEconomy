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

package lib.gui.abstractions;

public interface IGUI {
    void open();
    void close();
    void refresh();
    void handleLeftClick(int slot, IEntityGUI player);
    void handleRightClick(int slot, IEntityGUI player);
    int getRows();
    String getTitle();
    IInventory getInventory();
    boolean hasParent();
    IGUI getParent();
    void openParent();
    void fill();
}