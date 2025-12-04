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

package lib.commands.templates.users.OfferSubCommand;

import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;
import lib.abstractions.PlatformAdapter;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.CommandService;

import java.util.List;

public class DenyOfferCommand extends AbstractCommand {
    private final CancelOfferUseCase cancelOfferUseCase;
    private final PlatformAdapter platformAdapter;

    public DenyOfferCommand(CancelOfferUseCase cancelOfferUseCase,PlatformAdapter platformAdapter) {
        super("deny","", List.of("player"));
        this.platformAdapter= platformAdapter;
        this.cancelOfferUseCase = cancelOfferUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String playerNme = args[0];
        IEntityCommands playerFrom = platformAdapter.getPlayer(playerNme);
        if (playerFrom == null || !playerFrom.isOnline()) {
            sender.sendMessage("player offline");
            return false;
        }

        Player player = new Player(playerFrom.getUniqueId(), playerFrom.getName());

        Result<Void> result =cancelOfferUseCase.execute(player);
        if (!result.isSuccess()) {
            sender.sendMessage(result.getErrorMessage()+" " +result.getErrorCode());
            return false;
        }
        return true;
    }
}
