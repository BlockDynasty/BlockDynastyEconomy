package BlockDynasty.Economy.aplication.useCase.transaction.interfaces;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;

import java.math.BigDecimal;
import java.util.UUID;

public interface IDepositUseCase {
   Result<Void> execute(UUID targetUUID, BigDecimal amount);
   Result<Void> execute(String targetName, BigDecimal amount);
   Result<Void> execute(Player targetPlayer, BigDecimal amount);
   Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount);
   Result<Void> execute(String targetName, String currencyName, BigDecimal amount);
   Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount,Context context);
   Result<Void> execute(String targetName, String currencyName, BigDecimal amount,Context context);
   Result<Void> execute(Player targetPlayer, String currencyName, BigDecimal amount,Context context);
}
