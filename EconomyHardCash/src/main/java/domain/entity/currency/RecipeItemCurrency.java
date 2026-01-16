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

package domain.entity.currency;

import abstractions.platform.recipes.RecipeItem;
import abstractions.platform.materials.Materials;

public class RecipeItemCurrency extends RecipeItem {
    private NbtData nbtData;

    protected RecipeItemCurrency(RecipeItem recipeItem, NbtData nbtData) {
        super(recipeItem.getMaterial(), recipeItem.getName(), recipeItem.getTexture(), recipeItem.getLore());
        this.nbtData = nbtData;
    }

    public NbtData getNbtData() {
        return nbtData;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends RecipeItem.Builder {
        private NbtData nbtData;

        @Override
        public Builder setMaterial(Materials material) {
            super.setMaterial(material);
            return this;
        }

        @Override
        public Builder setName(String name) {
            super.setName(name);
            return this;
        }

        @Override
        public Builder setTexture(String texture) {
            super.setTexture(texture);
            return this;
        }

        @Override
        public Builder setLore(String... lore) {
            super.setLore(lore);
            return this;
        }

        public Builder setNbtData(NbtData nbtData) {
            this.nbtData = nbtData;
            return this;
        }

        public RecipeItemCurrency build() {
            if (nbtData == null) {
                throw new IllegalArgumentException("nbtData is null");
            }
            return new RecipeItemCurrency(super.build(), nbtData);
        }
    }
}
