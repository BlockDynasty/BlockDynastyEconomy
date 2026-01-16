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

import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.domain.result.Result;
import abstractions.platform.entity.IPlayer;
import lib.commands.PlatformCommand;
import lib.gui.components.PlatformGUI;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;

import java.math.BigDecimal;
import java.util.List;

public class CreateOfferCommand extends AbstractCommand {
    private final CreateOfferUseCase createOfferUseCase;
    private final PlatformCommand platformAdapter;

    public CreateOfferCommand(CreateOfferUseCase createOfferUseCase, PlatformCommand platformAdapter) {
        super("create","", List.of("amount","currencyAmount","price","currencyPrice","player"));
        this.platformAdapter = platformAdapter;
        this.createOfferUseCase = createOfferUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        double cantidad;
        double monto;
        try {
            cantidad= Double.parseDouble(args[0]);
            monto =Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid amount");
            return false;
        }

        //CAPTURAR LOS TIPOS DE MONEDA y target
        String tipoCantidad = args[1];
        String tipoMonto = args[3];
        IPlayer target = platformAdapter.getPlayer(args[4]);

        //si target no existe no se puede ofertar o no esta en linea
        if (target == null || !target.isOnline()) {
            sender.sendMessage("");
            return false;
        }

        //si entre el target y el vendedor no hay una distancia de 5 bloques informar error
        //if(Message.getEnableDistanceLimitOffer()){
        //    double distance = Message.getDistanceLimitOffer();
         //   if(player.getLocation().distance(target.getLocation())>distance){
         //       sender.sendMessage(Message.getTooFar(distance));
         //       return false;
          //  }
        //}

        if(sender.getName().equals(target.getName())){        // SI SE ESTA INTENTANDO OFRECER A SI MISMO
            sender.sendMessage("");
            return false;
        }

        Result<Void> result =createOfferUseCase.execute(sender.getUniqueId(), target.getUniqueId(),tipoCantidad, BigDecimal.valueOf(cantidad),tipoMonto,BigDecimal.valueOf(monto));
        if(!result.isSuccess()){
            sender.sendMessage( result.getErrorMessage()+" " +result.getErrorCode());
            return false;
        }
        return true;

    }
}
