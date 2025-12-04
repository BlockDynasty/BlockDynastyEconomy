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

import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.abstractions.PlatformAdapter;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.CommandService;

import java.util.List;

public class AcceptOfferCommand extends AbstractCommand {
    private final AcceptOfferUseCase acceptOfferUseCase;
    private final PlatformAdapter platformAdapter;

    public AcceptOfferCommand(AcceptOfferUseCase acceptOfferUseCase, PlatformAdapter platformAdapter) {
        super("accept","", List.of("player"));
        this.platformAdapter = platformAdapter;
        this.acceptOfferUseCase = acceptOfferUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String playerNme = args[0];
        IEntityCommands playerFrom = platformAdapter.getPlayer(playerNme);

        if (playerFrom == null || !playerFrom.isOnline()) {
            sender.sendMessage(" El jugador no está en línea.");
            return false;
        }

        //if(Message.getEnableDistanceLimitOffer()){
         //   double distance = Message.getDistanceLimitOffer();
          //  if(player.getLocation().distance(playerFrom.getLocation())>distance){
           //     sender.sendMessage(Message.getTooFar(distance));
            //    return false;
            //}
        //}

        Result<Void> result = acceptOfferUseCase.execute(sender.getUniqueId(), playerFrom.getUniqueId());

        if (!result.isSuccess()) {
            sender.sendMessage(result.getErrorMessage()+" "+result.getErrorCode());
           return false;
        }
        return true;
    }
}
