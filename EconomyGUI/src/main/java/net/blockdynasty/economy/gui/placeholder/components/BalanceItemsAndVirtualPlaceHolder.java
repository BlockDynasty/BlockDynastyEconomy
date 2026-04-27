package net.blockdynasty.economy.gui.placeholder.components;

import net.blockdynasty.economy.core.aplication.useCase.transaction.balance.GetBalanceUseCase;
import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.hardcash.aplication.useCase.HardCashUseCaseFactory;

import java.math.BigDecimal;
import java.util.UUID;

public class BalanceItemsAndVirtualPlaceHolder {
    private final GetBalanceUseCase getBalanceUseCase;
    private final Formatter formatter;

    public BalanceItemsAndVirtualPlaceHolder(GetBalanceUseCase getBalanceUseCase) {
        this.getBalanceUseCase = getBalanceUseCase;
        this.formatter = new Formatter();
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
            Money virtualBalance = result.getValue();
            Result<Money> itemsBalance = HardCashUseCaseFactory.getItemsBalanceUseCase().execute(playerId, currencyName);
            if (!itemsBalance.isSuccess()){
                return itemsBalance.getErrorMessage();
            }
            Money itemBalance = itemsBalance.getValue();

            BigDecimal balance = virtualBalance.getAmount().add(itemBalance.getAmount());
            ICurrency currency = result.getValue().getCurrency();
            return formatter.format(placeholder,parts,new Money(currency,balance));
        } catch (Exception e) {
            return "Unknown error";
        }
    }
}
