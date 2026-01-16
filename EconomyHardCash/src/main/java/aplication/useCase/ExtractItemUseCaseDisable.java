package aplication.useCase;

import domain.entity.player.IEntityHardCash;

import java.math.BigDecimal;

public class ExtractItemUseCaseDisable implements IExtractItemUseCase{

    @Override
    public void execute(IEntityHardCash player, BigDecimal amount, String currency) {
        player.sendMessage("HardCash system is disabled." );
    }
}
