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

import BlockDynasty.Economy.domain.entities.account.Player;
import lib.gui.components.IEntityGUI;
import lib.gui.components.*;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.util.colors.Message;
import lib.util.materials.Materials;

import java.util.List;
import java.util.Map;

public abstract class AccountsList extends PaginatedPanel<Player> {
    private final ITextInput textInput;
    public AccountsList(String title, int rows, IEntityGUI sender, IGUI parent, ITextInput textInput) {
        super(title, rows, sender, parent, 21); // 21 players per page
        this.textInput = textInput;
    }

    public void showPlayers(List<Player> players) {
        showItemsPage(players);
    }

    @Override
    protected IItemStack createItemFor(Player player) {
        return createItem(Materials.PLAYER_HEAD, player.getNickname());
    }

    @Override
    protected void functionLeftItemClick(Player player) {
        openNextSection(player);
    }

    @Override
    protected void addCustomButtons() {
        setItem(39, createItem(Materials.NAME_TAG,
                        Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GOLD)),"AccountList.button1.nameItem"),
                        Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"AccountList.button1.lore")),
                unused -> openAnvilSearch(unused));
    }

    protected void openAnvilSearch(IEntityGUI sender) {
        textInput.open(this, sender,Message.process("AccountList.button2.nameItem"), Message.process("AccountList.button2.lore"), s -> {
            Player foundPlayer = findPlayerByName(s);

            if (foundPlayer != null) {
                //openNextSection(foundPlayer);
                showPlayers(List.of(foundPlayer));
                this.open();
            } else {
                sender.sendMessage(Message.process(Map.of("color",ChatColor.stringValueOf(Colors.RED)),"AccountList.button2.response"));
                this.open();
            }
            return null;
        });
    }

    public abstract Player findPlayerByName(String playerName);
    public abstract void openNextSection(Player target);
}