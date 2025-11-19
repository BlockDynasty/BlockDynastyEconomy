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

package lib.gui.templates.administrators.mainMenus;

import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetOfflineAccountsUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.GUIFactory;
import lib.gui.components.IGUI;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.util.materials.Materials;
import lib.gui.components.abstractions.AccountsList;
import lib.util.colors.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountSelectorToEdit extends AccountsList {
    private final IEntityGUI sender;
    private final GetAccountByNameUseCase getAccountByNameUseCase;

    public AccountSelectorToEdit(IEntityGUI sender, GetAccountByNameUseCase getAccountByNameUseCase, GetOfflineAccountsUseCase getOfflineAccountsUseCase, IGUI parent, ITextInput textInput) {
        super(Message.process("AccountSelectorToEdit.title"), 5,sender,parent,textInput);
        this.getAccountByNameUseCase = getAccountByNameUseCase;
        this.sender = sender;

        Result<List<Account>> result = getOfflineAccountsUseCase.execute();
        if(result.isSuccess()) {
            List<Player> players = new ArrayList<>(result.getValue().stream()
                    .map(Account::getPlayer)
                    .sorted((a, b) -> a.getNickname().compareToIgnoreCase(b.getNickname()))
                    .collect(Collectors.toList()));

            //test
            /*for (int i=0; i < 45 ; i++) {
               players.add(new Player("empty", "empty"));
            }
            players.add(new Player("empty", "Cristian"));
            players.add(new Player("empty", "Daniel"));
            players.add(new Player("empty", "Alberto"));
            */
            showPlayers(players);
        }else {showPlayers(new ArrayList<>());}
    }

    @Override
    protected void addCustomButtons(){
        super.addCustomButtons();
        setItem(4, createItem(Materials.PAPER, Message.process("AccountSelectorToEdit.button1.nameItem"),
                        Message.processLines("AccountSelectorToEdit.button1.lore")),
                null);
    }
    @Override
    public Player findPlayerByName(String playerName) {
        Result<Account> result = getAccountByNameUseCase.execute(playerName);
        if(result.isSuccess()){
            return result.getValue().getPlayer();
        }else {return null;}
    }

    @Override
    public void openNextSection(Player target) {
        GUIFactory.editAccountPanel( sender, target, this).open();
    }
}
