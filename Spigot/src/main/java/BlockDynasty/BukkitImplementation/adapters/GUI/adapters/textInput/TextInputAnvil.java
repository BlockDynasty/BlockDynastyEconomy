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

package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.textInput;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.MaterialAdapter;
import lib.gui.components.IGUI;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class TextInputAnvil extends TextInputChat {

    @Override
    public void open(IEntityGUI owner, String title, String initialText, Function<String, String> function) {
        Player player = (Player) owner.getRoot();
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    player.playSound(player.getLocation(), MaterialAdapter.getClickSound(), 0.3f, 1.0f);
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String input = stateSnapshot.getText();
                    String response = function.apply(input);

                    if (response == null) {
                        return List.of(AnvilGUI.ResponseAction.close());
                    }

                    // Otherwise, show the response and keep GUI open
                    return List.of(AnvilGUI.ResponseAction.replaceInputText(response));
                })
                .text(initialText)
                .title(title)
                .plugin(BlockDynastyEconomy.getInstance())  //this probably is needed to check plugin version for compatibility
                .open(player);
    }

    @Override
    public void open(IGUI parent, IEntityGUI owner, String title, String initialText, Function<String, String> function) {
        Player player = (Player) owner.getRoot();
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    player.playSound(player.getLocation(), MaterialAdapter.getClickSound(), 0.3f, 1.0f);
                    // When output slot is clicked (process input)
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        String input = stateSnapshot.getText();
                        String response = function.apply(input);

                        if (response == null) {
                            return List.of(AnvilGUI.ResponseAction.close());
                        }

                        // Show response and keep GUI open
                        return List.of(AnvilGUI.ResponseAction.replaceInputText(response));
                    }

                    // When left slot (barrier) is clicked, go back
                    else if (slot == AnvilGUI.Slot.INPUT_LEFT) {
                        return List.of(
                                AnvilGUI.ResponseAction.close(),
                                AnvilGUI.ResponseAction.run(() -> parent.open())
                        );
                    }

                    return Collections.emptyList();
                })
                .text(initialText)
                .title(title)
                .plugin(BlockDynastyEconomy.getInstance())  //this probably is needed to check plugin version for compatibility
                .itemLeft(createBackItem())
                .itemOutput(new ItemStack(Material.PAPER))
                .open(player);
    }

    private static ItemStack createBackItem() {
        ItemStack backItem = new ItemStack(Material.BARRIER);
        ItemMeta meta = backItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§cBack");
            meta.setLore(Arrays.asList("§7Click to go back"));
            backItem.setItemMeta(meta);
        }
        return backItem;
    }

}
