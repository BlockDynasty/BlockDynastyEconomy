package net.blockdynasty.economy.gui.placeholder.components;
import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.hardcash.aplication.useCase.HardCashUseCaseFactory;

import java.util.UUID;

//todo: muestra el balance basado en items solamente de su inventario
public class BalanceItemsPlaceHolder {
    private final Formatter formatter;

    public BalanceItemsPlaceHolder(){
        this.formatter = new Formatter();
    }
    //%blockdynastyeconomy_balanceItems_currencyName%
    public String handle(UUID playerId, String placeholder) {
        String[] parts = placeholder.split("_");
        if (parts.length < 2) return "Invalid placeholder format";

        String currencyName = parts[1];

        try {
            Result<Money> balance = HardCashUseCaseFactory.getItemsBalanceUseCase().execute(playerId, currencyName);
            if (!balance.isSuccess()) {return balance.getErrorMessage();}
            return formatter.format(placeholder,parts,balance.getValue());
        } catch (Exception e) {
            return "Unknown";
        }
    }
}


