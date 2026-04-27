package net.blockdynasty.economy.gui.placeholder.components;

import net.blockdynasty.economy.core.aplication.useCase.transaction.balance.GetBalanceUseCase;
import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.hardcash.aplication.useCase.HardCashUseCaseFactory;

import java.math.BigDecimal;
import java.util.UUID;

public class BalanceItemsAndVirtualPlaceHolder {
    private final GetBalanceUseCase getBalanceUseCase;

    public BalanceItemsAndVirtualPlaceHolder(GetBalanceUseCase getBalanceUseCase) {
        this.getBalanceUseCase = getBalanceUseCase;
    }

    //%blockdynastyeconomy_balanceitemsandvirtual_currencyName%
    public String handle(UUID playerId, String placeholder){
        String[] parts = placeholder.split("_");
        if (parts.length < 2) return "Invalid placeholder format";

        String currencyName = parts[1];

        try {
            Result<Money> result = getBalanceUseCase.execute(playerId, currencyName);
            if (!result.isSuccess()) {
                return result.getErrorMessage();
            }
            Result<Money> itemsBalance = HardCashUseCaseFactory.getItemsBalanceUseCase().execute(playerId, currencyName);
            if (!itemsBalance.isSuccess()){
                return itemsBalance.getErrorMessage();
            }
            BigDecimal balance = itemsBalance.getValue().getAmount();
            return String.valueOf(balance.add(result.getValue().getAmount()));
        } catch (Exception e) {
            return "Unknown error";
        }
    }
}
