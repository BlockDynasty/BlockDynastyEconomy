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

import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IExchangeUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import services.messages.MessageService;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeCommand extends AbstractCommand {
    private IExchangeUseCase exchange;

    public ExchangeCommand(IExchangeUseCase exchange) {
        super("exchange","BlockDynastyEconomy.players.exchange", List.of("fromCurrency","toCurrency","toAmount" ));
        this.exchange = exchange;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

       /* if (args.length < 4) {
            F.getExchangeHelp(sender);
            return true;
        }

        String player;
        if (args.length > 4) {
            player = args[4];
        }else {
            player =  sender.getName();
        }

        double toExchangeAmount = 0;
        double toReceiveAmount = 0;
        try {
            toExchangeAmount = Double.parseDouble(args[1]);
            toReceiveAmount = Double.parseDouble(args[3]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(F.getUnvalidAmount());
            return true;
        }

        String toExchange = args[0];
        String toReceive = args[2];

        double finalToExchangeAmount = toExchangeAmount;
        double finalToReceiveAmount = toReceiveAmount;
        */

        //si hay un argumento demas es para otro jugador


        double toReceiveAmount = 0;

        try {
            toReceiveAmount = Double.parseDouble(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(MessageService.getMessage("invalidamount"));
            return true;
        }

        String toExchange = args[0];
        String toReceive = args[1];

        double finalToReceiveAmount = toReceiveAmount;

        Result<BigDecimal> result = exchange.execute(sender.getName(), toExchange, toReceive, null, BigDecimal.valueOf(finalToReceiveAmount));
        if (!result.isSuccess()) {
            sender.sendMessage(result.getErrorMessage()+" " +result.getErrorCode());
        }

        return true;
    }
}
