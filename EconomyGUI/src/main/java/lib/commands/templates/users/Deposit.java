package lib.commands.templates.users;

import BlockDynasty.Economy.aplication.useCase.transaction.TransferFundsUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.ITransferUseCase;
import BlockDynasty.Economy.domain.result.Result;

import lib.commands.PlatformCommand;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.abstractions.IEntityCommands;

import java.math.BigDecimal;
import java.util.List;

public class Deposit extends AbstractCommand {
    private PlatformCommand platformCommand;
    private ITransferUseCase transferFundsUseCase;

    public Deposit(PlatformCommand platformAdapter, ITransferUseCase transferFundsUseCase) {
        super("deposit", "BlockDynastyEconomy.players.deposit",List.of("player","amount","currency"));
        this.platformCommand = platformAdapter;
        this.transferFundsUseCase = transferFundsUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if (!super.execute(sender, args)) {
            return false;
        }

        String targetName = args[0]; //nombre del jugador
        String currencyName = args[2];  //nombre de la moneda

        BigDecimal amount = BigDecimal.ZERO; //monto temporal
        try {
            amount = new BigDecimal(args[1]);  //intentar extraer monto
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid amount");
            return false;
        }

        BigDecimal finalAmount = amount;

        Result<Void> result = transferFundsUseCase.execute(sender.getName(), targetName, currencyName, finalAmount);

        if (!result.isSuccess()) {
            sender.sendMessage(result.getErrorMessage() + " " + result.getErrorCode());
        }
        return true;
    }
}
