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

import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.NbtData;
import org.spongepowered.api.item.inventory.ItemStack;
import spongeV13.adapters.platformAdapter.NBTApi.CustomKeys;

import java.util.Optional;

public class ItemStackCurrencyAdapter implements ItemStackCurrency {
    private ItemStack item;

    public ItemStackCurrencyAdapter(ItemStack item) {
        this.item = item;
    }

    @Override
    public NbtData getNbtData() {
        String nameCurrency = null;
        Optional<String> object = item.get(CustomKeys.NAME_CURRENCY);
        if (object.isPresent()) {
            nameCurrency = object.get();
        }

        String valueCurrency = null;
        Optional<String> valueObject = item.get(CustomKeys.VALUE);
        if (valueObject.isPresent()) {
            valueCurrency = valueObject.get();
        }

        String uuidCurrency = null;
        Optional<String> uuidObject = item.get(CustomKeys.UUID_CURRENCY);
        if (uuidObject.isPresent()) {
            uuidCurrency = uuidObject.get();
        }

        return new NbtData(nameCurrency,uuidCurrency,valueCurrency);
    }

    @Override
    public int getCantity() {
        return item.quantity();
    }

    @Override
    public Object getRoot() {
        return item;
    }
}
