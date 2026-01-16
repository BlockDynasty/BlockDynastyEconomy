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

package lib.gui.components.generics;

import lib.gui.GUISystem;
import lib.gui.components.factory.Inventory;
import lib.gui.components.factory.Item;
import abstractions.platform.recipes.RecipeInventory;
import lib.gui.components.IEntityGUI;
import lib.gui.components.*;
import abstractions.platform.recipes.RecipeItem;
import abstractions.platform.materials.Materials;

import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractPanel implements IGUI {
    protected IInventory inventory;
    protected final RecipeInventory recipeInventory;
    protected final IEntityGUI owner;
    protected final Map<Integer, IButton> buttons = new HashMap<>();
    protected IGUI parent;

    public AbstractPanel(String title, int rows, IEntityGUI owner) {
        this.recipeInventory = RecipeInventory.builder().setTitle(title).setRows(rows).build();
        createInventory();
        fill();
        this.owner = owner;
        this.parent = null;
    }
    public AbstractPanel(String title, int rows, IEntityGUI owner, IGUI parent) {
        this(title, rows, owner);
        this.parent = parent;
    }

    private void createInventory() {
        this.inventory = Inventory.of(recipeInventory);
        buttons.forEach((key, value) -> inventory.set(key, value.getItemStack()));
    }

    protected void setButton(int slot, IButton button) {// Consumer<IEntityGUI> leftClickAction
        buttons.put(slot, button);
        inventory.set(slot, button.getItemStack());
    }

    @Override
    public void handleRightClick(int slot) {
        IButton button = buttons.get(slot);
        if (button == null) {
            owner.playFailureSound();
            return;
        }
        Consumer<IEntityGUI> action =button.getRightClickAction();
        if (action != null) {
            action.accept(owner);
            owner.playSuccessSound();
        }else {owner.playFailureSound();}
    }

    @Override
    public void handleLeftClick(int slot) {
        IButton button = buttons.get(slot);
        if (button == null) {
            owner.playFailureSound();
            return;
        }
        Consumer<IEntityGUI> action = button.getLeftClickAction();
        if (action != null) {
            action.accept(owner);
            owner.playSuccessSound();
        }else{owner.playFailureSound();}
    }

    @Override
    public void open() {
        createInventory();
        owner.openInventory(this.inventory);
        GUISystem.registerGUI(owner, this);
    }

    @Override
    public void close() {
        owner.closeInventory();
        GUISystem.unregisterGUI(owner);
    }

    @Override
    public void openParent() {
        if (hasParent()) {
            parent.open();
        } else {
            close();
        }
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public IGUI getParent() {
        return this.parent;
    }

    private int getRows(){
        return this.recipeInventory.getRows();
    }

    @Override
    public void fill() {
        IButton fillBlueGlass = Button.builder()
                .setItemStack(Item.of(RecipeItem.builder().setMaterial(Materials.BLUE_STAINED_GLASS_PANE).build()))
                .build();
        IButton fillGlass = Button.builder()
                .setItemStack(Item.of(RecipeItem.builder().setMaterial(Materials.GLASS_PANE).build()))
                .build();

        int rows = (getRows()*9) / 9;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 9; col++) {
                int slot = row * 9 + col;
                if (row == 0 || row == rows - 1 || col == 0 || col == 8) {
                    buttons.put(slot, fillBlueGlass);
                } else {
                    buttons.put(slot, fillGlass);
                }
            }
        }
    }

    @Override
    public void refresh() {
    }
}
