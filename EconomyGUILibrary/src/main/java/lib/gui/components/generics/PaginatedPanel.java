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

import lib.gui.components.IGUI;
import lib.gui.components.IItemStack;
import lib.gui.components.IEntityGUI;
import lib.gui.components.factory.Item;
import lib.gui.components.recipes.RecipeItem;
import lib.util.materials.Materials;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.messages.Message;

import java.util.List;
import java.util.Map;

public abstract class PaginatedPanel<T> extends AbstractPanel {
    protected int currentPage = 0;
    protected final int itemsPerPage;
    protected final int rows;

    public PaginatedPanel(String title, int rows, IEntityGUI player, IGUI parent, int itemsPerPage) {
        super(title, Math.max(3, rows), player, parent);
        this.rows = Math.max(3, rows);
        int availableRows = this.rows - 1;
        int maxSafeItems = availableRows * 7;

        this.itemsPerPage = Math.min(itemsPerPage, maxSafeItems);
    }

    /**
     * Display items with pagination
     */
    protected void showItemsPage(List<T> items) {
        fill();
        if (items.isEmpty()) {
            setButton(getEmptyMessageSlot(), Button.builder().setItemStack(createEmptyMessage()).build());
            createBackButton();
            addCustomButtons();
            return;
        }

        // Calculate pagination
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());
        // Add items to GUI
        int slot = getStartingSlot();
        for (int i = startIndex; i < endIndex; i++) {
            T item = items.get(i);
            final T finalItem = item; // Make final for lambda
            setButton(slot, Button.builder()
                    .setItemStack(createItemFor(item))
                    .setLeftClickAction(unused -> functionLeftItemClick(finalItem))
                    .setRightClickAction(unused -> functionRightItemClick(finalItem)).build());
            slot = getNextSlot(slot);
        }

        // Navigation buttons
        if (currentPage > 0) {
            setButton(getPreviousButtonSlot(),Button.builder().setItemStack(createPreviousButton()).setLeftClickAction(unused -> {
                currentPage--;
                showItemsPage(items);
            }).build());
        }

        if (endIndex < items.size()) {
            setButton(getNextButtonSlot(), Button.builder().setItemStack(createNextButton()).setLeftClickAction(unused -> {
                currentPage++;
                showItemsPage(items);
            }).build());
        }

        // Back button
        createBackButton();

        // Add additional custom buttons
        addCustomButtons();
    }

    // Abstract methods
    protected abstract IItemStack createItemFor(T item);

    protected void functionLeftItemClick(T item) {
    }

    protected void functionRightItemClick(T item) {
    }

    protected int getStartingSlot() {
        // Default starting slot is 10, can be overridden
        return 10;
    }

    protected int getNextSlot(int currentSlot) {
        // Adjust slot position (skip edge columns)
        currentSlot++;
        if (currentSlot % 9 == 8) currentSlot += 2;
        return currentSlot;
    }

    // Methods with dynamic positions based on inventory size
    protected int getEmptyMessageSlot() {
        return (rows * 9) / 2; // Center of the inventory
    }

    protected int getPreviousButtonSlot() {
        return (rows - 1) * 9 + 2; // Bottom row, left side
    }

    protected int getNextButtonSlot() {
        return (rows - 1) * 9 + 6; // Bottom row, right side
    }

    protected int getBackButtonSlot() {
        return (rows - 1) * 9 + 4; // Bottom row, center
    }

    protected IItemStack createEmptyMessage() {
        RecipeItem recipe = RecipeItem.builder()
                .setMaterial(Materials.BARRIER)
                .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)), "Paginated.button1.nameItem"))
                .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "Paginated.button1.lore"))
                .build();
        return Item.of(recipe);
    }

    protected IItemStack createPreviousButton() {
        RecipeItem recipe = RecipeItem.builder()
                .setMaterial(Materials.ARROW)
                .setName( Message.process(Map.of("color", ChatColor.stringValueOf(Colors.AQUA)),"Paginated.button2.nameItem"))
                .setLore( Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"Paginated.button2.lore"))
                .build();
        return Item.of(recipe);
    }

    protected IItemStack createNextButton() {
        RecipeItem recipe = RecipeItem.builder()
                .setMaterial(Materials.ARROW)
                .setName( Message.process(Map.of("color", ChatColor.stringValueOf(Colors.AQUA)),"Paginated.button3.nameItem"))
                .setLore( Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"Paginated.button3.lore"))
                .build();
        return Item.of(recipe);
    }

    protected IItemStack createBackButton() {
        RecipeItem recipe = RecipeItem.builder()
                .setMaterial(Materials.BARRIER)
                .setName( Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"Paginated.button4.nameItem"))
                .setLore( Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"Paginated.button4.lore"))
                .build();

        setButton(getBackButtonSlot(), Button.builder()
                .setItemStack(Item.of(recipe))
                .setLeftClickAction(unused -> this.openParent())
                .build());

        return Item.of(recipe);
    }

    protected void addCustomButtons() {
        // Default implementation does nothing
    }
}