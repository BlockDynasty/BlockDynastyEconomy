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

package lib.gui.templates.users;

import BlockDynasty.Economy.domain.entities.account.Player;
import lib.gui.GUIFactory;
import lib.gui.components.IGUI;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.Materials;
import lib.gui.components.abstractions.AccountsList;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

import java.util.List;
import java.util.stream.Collectors;

public class ListPlayersOnline extends AccountsList {
    private final IEntityGUI sender;

    public ListPlayersOnline(IEntityGUI sender, IGUI parent , ITextInput textInput ) {
        super("Select Player", 5,sender,parent, textInput );
        this.sender = sender;

        List<Player> players = platformAdapter.getOnlinePlayers().stream()
                .map(p -> new Player(p.getUniqueId(), p.getName()))
                .sorted((a, b) -> a.getNickname().compareToIgnoreCase(b.getNickname()))
                .collect(Collectors.toList());

        showPlayers(players);
    }

    @Override
    public Player findPlayerByName(String playerName) {
        IEntityGUI player;
        IEntityGUI target = platformAdapter.getOnlinePlayers().stream()
                .filter(p -> p.getName().equalsIgnoreCase(playerName))
                .findFirst().orElse(null); //online players only
        if (target != null) {
            return new Player(target.getUniqueId(), target.getName());
        } else {
            return null;
        }
    }

    @Override
    public void openNextSection(Player target) {
        GUIFactory.currencyListToPayPanel(sender,target,this.getParent()).open();
    }
    @Override
    public void addCustomButtons(){
        super.addCustomButtons(); // Call the parent method to add the default buttons accountList
        setItem(4, createItem(Materials.PAPER, ChatColor.stringValueOf(Colors.GREEN)+"Select Player to Pay", ChatColor.stringValueOf(Colors.WHITE)+"Click to select the player you want to pay","#Ordered by name"), null);
    }
}