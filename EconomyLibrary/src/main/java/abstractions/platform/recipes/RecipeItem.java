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
package abstractions.platform.recipes;

import abstractions.platform.materials.Materials;

/**
 * Recipe represent attributes for create an item
 * you can use the builder to create an instance of RecipeItem
 **/
public class RecipeItem {
    private final Materials material;
    private final String name;
    private final String texture;
    private final String[] lore;

    protected RecipeItem(Materials material, String name, String texture, String... lore) {
        this.material = material;
        this.name = name;
        this.texture = texture;
        this.lore = lore;
    }

    public Materials getMaterial() {
        return material;
    }
    public String getName() {
        return name;
    }
    public String getTexture() {
        return texture;
    }
    public String[] getLore() {
        return lore;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private Materials material = Materials.STONE;
        private String name = "";
        private String texture = "";
        private String[] lore = new String[0];

        public Builder setMaterial(Materials material) {
            this.material = material;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setTexture(String texture) {
            this.texture = texture;
            return this;
        }

        public Builder setLore(String... lore) {
            this.lore = lore;
            return this;
        }

        public RecipeItem build() {
            return new RecipeItem(material, name, texture, lore);
        }
    }

}
