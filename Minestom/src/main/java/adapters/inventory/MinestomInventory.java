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

package adapters.inventory;

import lib.gui.components.recipes.RecipeInventory;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;

public class MinestomInventory extends Inventory {

    public MinestomInventory(RecipeInventory recipe) {
        super(MinestomInventory.getInventoryType(recipe.getRows()), Component.text(recipe.getTitle()));
    }

    public static InventoryType getInventoryType(int type) {
        switch (type) {
            case 1:
                return InventoryType.CHEST_1_ROW;
            case 2:
                return InventoryType.CHEST_2_ROW;
            case 3:
                return InventoryType.CHEST_3_ROW;
            case 4:
                return InventoryType.CHEST_4_ROW;
            case 5:
                return InventoryType.CHEST_5_ROW;
            case 6:
                return InventoryType.CHEST_6_ROW;
            default:
                return InventoryType.CHEST_6_ROW;
        }
    }
}
