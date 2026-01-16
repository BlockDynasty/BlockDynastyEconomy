package aplication.useCase;

import domain.entity.player.IEntityHardCash;

import java.math.BigDecimal;

public class GiveItemUseCaseDisable implements IGiveItemUseCase {


    @Override
    public boolean execute(String playerName, BigDecimal amount, String currency) {
        return false;
    }
}
