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

package lib.commands.templates.administrators.EconomySubcommand;

import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.domain.result.Result;
import abstractions.platform.entity.IPlayer;
import lib.commands.PlatformCommand;
import lib.gui.components.PlatformGUI;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class DeleteAccountCommand extends AbstractCommand {
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final PlatformCommand platformAdapter;

    public DeleteAccountCommand(DeleteAccountUseCase deleteAccountUseCase, PlatformCommand platformAdapter) {
        super("deleteAccount","", List.of("player"));
        this.platformAdapter = platformAdapter;
        this.deleteAccountUseCase = deleteAccountUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String playerName = args[0];
        IPlayer player = platformAdapter.getPlayer(playerName);
        Result<Void> result =deleteAccountUseCase.execute(playerName);
        if (result.isSuccess()) {
            sender.kickPlayer("Your account has been deleted by an administrator.");
        }
        return true;
    }
}
