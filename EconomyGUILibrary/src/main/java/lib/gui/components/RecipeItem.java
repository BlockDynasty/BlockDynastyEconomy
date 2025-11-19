package lib.gui.components;

import lib.util.materials.Materials;

public class RecipeItem {
    private final Materials material;
    private final String name;
    private final String texture;
    private final String[] lore;

    private RecipeItem(Materials material, String name, String texture, String... lore) {
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
        private Materials material = Materials.GLASS_PANE;
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
