package aplication.useCase;

import domain.entity.player.IEntityHardCash;

public class DepositItemUseCaseDisable implements IDepositItemUseCase{

    @Override
    public void execute(IEntityHardCash player) {
        player.sendMessage("HardCash system is disabled." );
    }
}
