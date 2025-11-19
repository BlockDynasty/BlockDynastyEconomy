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

package lib.gui.components.abstractions;

import lib.gui.components.IEntityGUI;
import lib.abstractions.PlatformAdapter;
import lib.gui.components.*;
import lib.util.materials.Materials;

import java.util.*;
import java.util.function.Consumer;

public class AbstractPanel implements IGUI {
    protected static PlatformAdapter platformAdapter; //todas las guis van a compartir el mismo adapter
    protected static IGUIService guiService;
    protected IInventory inventory;
    protected final IEntityGUI owner;
    protected IGUI parent;
    protected final Map<Integer, IItemStack> items = new HashMap<>();
    protected final Map<Integer, Consumer<IEntityGUI>> leftClickActions = new HashMap<>();
    protected final Map<Integer, Consumer<IEntityGUI>> rightClickActions = new HashMap<>();

    // Set the platform adapter at startup
    public static void setPlatformAdapter(PlatformAdapter adapter,IGUIService guiServices) {
        platformAdapter = adapter;
        guiService = guiServices;
    }

    public AbstractPanel(String title, int rows, IEntityGUI owner) {
        createInventory(title, rows);
        fill();
        this.owner = owner;
        this.parent = null;
    }
    public AbstractPanel(String title, int rows, IEntityGUI owner, IGUI parent) {
        this(title, rows, owner);
        this.parent = parent;
    }
    private void createInventory(String title, int rows) {
        this.inventory =platformAdapter.createInventory(title, rows);
        this.inventory.setRows(rows);
        this.inventory.setTitle(title);
        items.forEach((key, value) -> inventory.set(key, value));
    }

    @Deprecated
    protected IItemStack createItem(Materials material,String name, String... lore) {
        IItemStack item =platformAdapter.createItemStack(material);
        item.setDisplayName(name);
        item.setLore(Arrays.asList(lore));
        return item;
    }

    protected IItemStack createItem(RecipeItem recipe) {
        return platformAdapter.createItemStack(recipe);
    }

    protected void setItem(int slot, IItemStack item, Consumer<IEntityGUI> leftClickAction) {
        items.put(slot, item);
        inventory.set(slot, item);
        if (item == null || leftClickAction == null) {
            leftClickActions.remove(slot);
        } else {
            leftClickActions.put(slot, leftClickAction);
        }
    }
    protected void setItem(int slot, IItemStack item, Consumer<IEntityGUI> leftClickAction, Consumer<IEntityGUI> rightClickAction) {
        setItem(slot, item, leftClickAction);

        if (item == null || rightClickAction == null) {
            rightClickActions.remove(slot);
        } else {
            rightClickActions.put(slot, rightClickAction);
        }
    }


    @Override
    public void close() {
        owner.closeInventory();
        guiService.unregisterGUI(owner);
    }

    @Override
    public void open() {
        createInventory(inventory.getTitle(), inventory.getRows());
        owner.openInventory(this.inventory);
        guiService.registerGUI(owner, this);
    }

    @Override
    public void openParent() {
        if (hasParent()) {
            parent.open();
        } else {
            owner.closeInventory();
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

    @Override
    public void handleRightClick(int slot, IEntityGUI player) {
        Consumer<IEntityGUI> action = rightClickActions.get(slot);
        if (action != null) {
            action.accept(player);
            player.playSuccessSound();
        }else {player.playFailureSound();}
    }

    @Override
    public void handleLeftClick(int slot, IEntityGUI player) {
        Consumer<IEntityGUI> action = leftClickActions.get(slot);
        if (action != null) {
            action.accept(player);
            player.playSuccessSound();
        }else{player.playFailureSound();}
    }

    @Override
    public String getTitle(){
        return this.inventory.getTitle();
    }

    @Override
    public int getRows(){
        return this.inventory.getRows();
    }

    @Override
    public void fill() {
        IItemStack blueGlass = createItem(Materials.BLUE_STAINED_GLASS_PANE, " " , " " );
        IItemStack filler = createItem(Materials.GLASS_PANE, " "," " );

        int rows = (getRows()*9) / 9;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 9; col++) {
                int slot = row * 9 + col;
                if (row == 0 || row == rows - 1 || col == 0 || col == 8) {
                    items.put(slot, blueGlass);
                } else {
                    items.put(slot, filler);
                }
            }
        }

    }

    public IInventory getInventory() {
        return this.inventory;
    }

    @Override
    public void refresh() {
    }
}
