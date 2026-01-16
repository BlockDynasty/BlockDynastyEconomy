package lib.commands.templates.administrators.EconomySubcommand;

import abstractions.platform.entity.IPlayer;
import aplication.useCase.HardCashUseCaseFactory;
import aplication.useCase.IExtractItemUseCase;
import aplication.useCase.IGiveItemUseCase;
import lib.commands.PlatformCommand;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.abstractions.IEntityCommands;
import services.messages.MessageService;

import java.math.BigDecimal;
import java.util.List;

public class DepositItemCommand extends AbstractCommand {
    private IGiveItemUseCase giveItemUseCase;
    private PlatformCommand platformCommand;

    public DepositItemCommand() {
        super("giveitem", "", List.of("player", "amount", "currency"));
        this.giveItemUseCase = HardCashUseCaseFactory.getGiveItemUseCase();
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

        boolean result = giveItemUseCase.execute(target, BigDecimal.valueOf(finalMount), currencyName);
        if (!result) {
            sender.sendMessage("Give item failed. Check if the player exists and has enough inventory space.");
            return false;
        }
        sender.sendMessage("Successfully gave " + finalMount + " " + currencyName + " to " + target + " in items.");
        return true;
    }
}
