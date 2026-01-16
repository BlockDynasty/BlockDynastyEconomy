package lib.commands.templates.administrators.EconomySubcommand;

import aplication.useCase.HardCashUseCaseFactory;
import aplication.useCase.IDepositItemUseCase;
import lib.commands.abstractions.AbstractCommand;

public class DepositHardCashCommand extends AbstractCommand {
    private final IDepositItemUseCase depositItemUseCase;

    public DepositHardCashCommand() {
        super("depositItem", "");
        this.depositItemUseCase = HardCashUseCaseFactory.getDepositItemUseCase();
    }

    @Override
    public boolean execute(lib.commands.abstractions.IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        depositItemUseCase.execute(sender.asEntityGUI().asEntityHardCash());
        return true;
    }
}
