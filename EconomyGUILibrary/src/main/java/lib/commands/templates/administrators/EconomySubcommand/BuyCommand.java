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

import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IWithdrawUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.abstractions.PlatformAdapter;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.CommandService;
import lib.messages.MessageService;

import java.math.BigDecimal;
import java.util.List;

public class BuyCommand extends AbstractCommand {
    private final IWithdrawUseCase withdraw;
    private final PlatformAdapter platformAdapter;

    public BuyCommand(IWithdrawUseCase withdraw, PlatformAdapter platformAdapter) {
        super("buy","",List.of("player", "amount", "currency", "command..."));
        this.platformAdapter = platformAdapter;
        this.withdraw = withdraw;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        IEntityCommands player = platformAdapter.getPlayer(args[0]);

        if(player==null) {
            sender.sendMessage("player is offline");
            return false;
        }
        double cantidadDemoneda;
        try {
            cantidadDemoneda = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(MessageService.getMessage("invalidamount"));
            return false;
        }

        String tipoDemoneda = args[2];

        //constructor de comando a entregar a partir del argumento 3
        StringBuilder cmdBuilder = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            cmdBuilder.append(args[i]).append(" ");
        }
        String cmd = cmdBuilder.toString().trim();

        Result<Void> result =withdraw.execute(player.getName(),tipoDemoneda, BigDecimal.valueOf(cantidadDemoneda));

        if(result.isSuccess()){
            try{
                platformAdapter.dispatchCommand(cmd);
                player.sendMessage(MessageService.getMessage("buy_success"));
            }catch (Exception e){
                sender.sendMessage("Error dispatch command: " + e.getMessage());
            }

        }else{
            sender.sendMessage("Error: " + result.getErrorMessage());
        }

        return true;
    }

}
