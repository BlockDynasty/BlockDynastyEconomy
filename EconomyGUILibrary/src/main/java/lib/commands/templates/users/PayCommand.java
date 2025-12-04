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

package lib.commands.templates.users;

import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.abstractions.PlatformAdapter;
import lib.commands.CommandService;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;

import java.math.BigDecimal;
import java.util.List;

public class PayCommand extends AbstractCommand {
    private final PayUseCase pay;
    private final PlatformAdapter platformAdapter;

    public PayCommand(PayUseCase pay, PlatformAdapter platformAdapter) {
        super("pay","BlockDynastyEconomy.players.pay", List.of("player","amount","currency"));
        this.platformAdapter = platformAdapter;
        this.pay = pay;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String targetName = args[0]; //nombre del jugador

        IEntityCommands target = platformAdapter.getPlayer(targetName);
        if(target==null){
            sender.sendMessage("The player is not online");
            return false;
        }

        String currencyName = args[2];  //nombre de la moneda

        BigDecimal amount=BigDecimal.ZERO; //monto temporal
        try{
            amount = new BigDecimal(args[1]);  //intentar extraer monto
        }catch (NumberFormatException e){
            sender.sendMessage("Invalid amount");
            return false;
        }

        BigDecimal finalAmount = amount;

        Result<Void> result = pay.execute(sender.getName(), targetName, currencyName, finalAmount);

        if (!result.isSuccess()){
            sender.sendMessage(result.getErrorMessage()+" " +result.getErrorCode());
        }
        return true;
    }
}
