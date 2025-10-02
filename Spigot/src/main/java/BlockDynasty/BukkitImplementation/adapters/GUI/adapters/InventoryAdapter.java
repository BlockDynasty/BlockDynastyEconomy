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

package BlockDynasty.BukkitImplementation.adapters.GUI.adapters;

import lib.gui.components.IInventory;
import lib.gui.components.IItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryAdapter implements IInventory {
    Inventory inventory;
    int rows;
    String title;

    public InventoryAdapter(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void set(int slot, IItemStack item) {
        this.inventory.setItem(slot,(ItemStack) item.getHandle());
    }

    @Override
    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public int getSize() {
        return this.inventory.getMaxStackSize();
    }

    @Override
    public Object getHandle() {
        return inventory;
    }
}
