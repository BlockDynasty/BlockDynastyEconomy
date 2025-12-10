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

package spongeV13.adapters.GUI.adapters;

import lib.gui.components.IItemStack;
import org.spongepowered.api.item.inventory.ItemStack;

public class ItemStackAdapter implements IItemStack {
    private ItemStack itemStack;

    public ItemStackAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public Object getHandle() {
        return itemStack;
    }
}
