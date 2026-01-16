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

import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.ISetBalanceUseCase;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import services.messages.MessageService;

import java.math.BigDecimal;
import java.util.List;

public class SetCommand extends AbstractCommand {
    private final ISetBalanceUseCase setbalance;

    public SetCommand(ISetBalanceUseCase setbalance) {
        super("set", "",List.of("player", "amount", "currency"));
        this.setbalance = setbalance;

    }
    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String target = args[0];
        String montoString= args[1];
        String currencyName = args[2];

        double amount=0;
        try {
            amount = Double.parseDouble(montoString);
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageService.getMessage("invalidamount"));
            return false;
        }

        double finalMount = amount;

        Result<Void> result = setbalance.execute(target, currencyName, BigDecimal.valueOf(finalMount), Context.COMMAND);

        if(!result.isSuccess()){
            sender.sendMessage("Error on Set Balance: " + result.getErrorMessage()+" "+result.getErrorMessage());
        }else{
            sender.sendMessage("Set Balance successful");
        }

        return true;
    }

}
