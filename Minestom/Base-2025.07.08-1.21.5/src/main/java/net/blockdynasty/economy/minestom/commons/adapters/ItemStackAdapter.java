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

package net.blockdynasty.economy.minestom.commons.adapters;

import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.hardcash.domain.entity.currency.ItemStackCurrency;
import net.blockdynasty.economy.hardcash.domain.entity.currency.NbtData;
import net.minestom.server.item.ItemStack;

public class ItemStackAdapter implements IItemStack, ItemStackCurrency {
    private ItemStack itemStack;

    public ItemStackAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public Object getHandle() {
        return itemStack;
    }

    @Override
    public NbtData getNbtData() {
        return new NbtData();
    }

    @Override
    public int getCantity() {
        return itemStack.amount();
    }

    @Override
    public void setCantity(int amount) {
        itemStack = itemStack.withAmount(amount);
    }

    @Override
    public String getMaterial() {
        return itemStack.material().name();
    }

    @Override
    public String asBase64() {
        return "";
    }

    @Override
    public int maxStackSize() {
        return itemStack.maxStackSize();
    }

    @Override
    public boolean isNull() {
        return itemStack == null || itemStack.isAir();
    }

    @Override
    public boolean isSimilar(ItemStackCurrency other) {
        return this.itemStack.isSimilar((ItemStack) other.getRoot());
    }

    @Override
    public Object getRoot() {
        return this.itemStack;
    }
}
