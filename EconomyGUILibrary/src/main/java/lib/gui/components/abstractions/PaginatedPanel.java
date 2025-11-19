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

import lib.gui.components.IGUI;
import lib.gui.components.IItemStack;
import lib.gui.components.IEntityGUI;
import lib.util.materials.Materials;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.util.colors.Message;

import java.util.List;
import java.util.Map;

public abstract class PaginatedPanel<T> extends AbstractPanel {
    protected int currentPage = 0;
    protected final int itemsPerPage;
    protected final int rows;

    public PaginatedPanel(String title, int rows, IEntityGUI player, IGUI parent, int itemsPerPage) {
        super(title, Math.max(3, rows), player, parent);
        this.rows = Math.max(3, rows);

        // Calculate maximum safe items per page based on available slots
        // Subtract 1 row (9 slots) for navigation buttons
        int availableRows = this.rows - 1;
        int maxSafeItems = availableRows * 7; // 7 slots per row (avoiding edge columns)

        // Ensure itemsPerPage doesn't exceed available space
        this.itemsPerPage = Math.min(itemsPerPage, maxSafeItems);
    }

    /**
     * Display items with pagination
     */
    protected void showItemsPage(List<T> items) {
        fill();
        if (items.isEmpty()) {
            setItem(getEmptyMessageSlot(), createEmptyMessage(), null);
            setItem(getBackButtonSlot(), createBackButton(), unused -> this.openParent());
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
            setItem(slot, createItemFor(item), unused -> functionLeftItemClick(finalItem), unused -> functionRightItemClick(finalItem));
            slot = getNextSlot(slot);
        }

        // Navigation buttons
        if (currentPage > 0) {
            setItem(getPreviousButtonSlot(), createPreviousButton(), unused -> {
                currentPage--;
                showItemsPage(items);
            });
        }

        if (endIndex < items.size()) {
            setItem(getNextButtonSlot(), createNextButton(), unused -> {
                currentPage++;
                showItemsPage(items);
            });
        }

        // Back button
        setItem(getBackButtonSlot(), createBackButton(), unused -> this.openParent());

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
        return createItem(Materials.BARRIER, Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"Paginated.button1.nameItem"),
                Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"Paginated.button1.lore"));
    }

    protected IItemStack createPreviousButton() {
        return createItem(Materials.ARROW,  Message.process(Map.of("color", ChatColor.stringValueOf(Colors.AQUA)),"Paginated.button2.nameItem"),
                Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"Paginated.button2.lore"));
    }

    protected IItemStack createNextButton() {
        return createItem(Materials.ARROW,  Message.process(Map.of("color", ChatColor.stringValueOf(Colors.AQUA)),"Paginated.button3.nameItem"),
                Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"Paginated.button3.lore"));
    }

    protected IItemStack createBackButton() {
        return createItem(Materials.BARRIER,
                Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"Paginated.button4.nameItem"),
                Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"Paginated.button4.lore"));
    }

    protected void addCustomButtons() {
        // Default implementation does nothing
    }
}