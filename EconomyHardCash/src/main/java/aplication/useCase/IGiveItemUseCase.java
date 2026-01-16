package aplication.useCase;

import domain.entity.player.IEntityHardCash;

import java.math.BigDecimal;

public interface IGiveItemUseCase {

    boolean execute(String target, BigDecimal amount, String currency);
}
