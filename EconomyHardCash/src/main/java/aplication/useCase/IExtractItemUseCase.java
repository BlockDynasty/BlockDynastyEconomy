package aplication.useCase;

import domain.entity.player.IEntityHardCash;

import java.math.BigDecimal;

public interface IExtractItemUseCase {

    void execute(IEntityHardCash player, BigDecimal amount, String currency);
}
