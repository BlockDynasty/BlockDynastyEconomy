package BlockDynasty.Economy.aplication.useCase.transaction.interfaces;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;

import java.math.BigDecimal;
import java.util.UUID;

public interface ITradeUseCase {
    Result<Void> execute(UUID userFrom, UUID userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo);
    Result<Void> execute(String userFrom, String userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo);
    Result<Void> execute(Player userFrom, Player userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo);
}
