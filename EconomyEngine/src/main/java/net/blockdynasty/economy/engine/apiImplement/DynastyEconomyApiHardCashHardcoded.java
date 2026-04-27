package net.blockdynasty.economy.engine.apiImplement;

import net.blockdynasty.economy.core.aplication.useCase.UseCaseFactory;
import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.core.domain.services.log.Log;
import net.blockdynasty.economy.hardcash.aplication.useCase.HardCashUseCaseFactory;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.balance.IGetItemsBalanceUseCase;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.deposit.IDepositItemsCurrencyUseCase;
import net.blockdynasty.economy.api.EconomyResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class DynastyEconomyApiHardCashHardcoded extends DynastyEconomyApiHardCoded{
    private String currencyName;
    private IDepositItemsCurrencyUseCase depositItemUseCase;
    private IGetItemsBalanceUseCase getItemsBalanceUseCase;


    public DynastyEconomyApiHardCashHardcoded(UseCaseFactory factory, UUID id, String currencyName) {
        super(factory, id, currencyName);
        this.depositItemUseCase = HardCashUseCaseFactory.getDepositItemsCurrencyUseCase();
        this.getItemsBalanceUseCase = HardCashUseCaseFactory.getItemsBalanceUseCase();
        this.currencyName = currencyName;
    }

    public DynastyEconomyApiHardCashHardcoded(UseCaseFactory factory, Log log, UUID id, String currencyName) {
        super(factory, log, id, currencyName);
        this.depositItemUseCase = HardCashUseCaseFactory.getDepositItemsCurrencyUseCase();
        this.getItemsBalanceUseCase = HardCashUseCaseFactory.getItemsBalanceUseCase();
        this.currencyName = currencyName;
    }

    @Override
    public EconomyResponse withdraw(UUID uuid, BigDecimal amount){
        BigDecimal filteredAmount = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal itemsNeeded = filteredAmount.setScale(0, RoundingMode.CEILING);


        BigDecimal itemsAvailable = BigDecimal.ZERO;
        Result<Money> result = getItemsBalanceUseCase.execute(uuid, this.currencyName);
        if (result.isSuccess()) {
            itemsAvailable = result.getValue().getAmount();
        }

        BigDecimal amountToMove = itemsNeeded.min(itemsAvailable);

        if (amountToMove.compareTo(BigDecimal.ZERO) > 0) {
            this.depositItemUseCase.execute(uuid, this.currencyName, amountToMove);
        }
        return super.withdraw(uuid, filteredAmount, this.currencyName);
    }

    @Override
    public EconomyResponse withdraw(String name, BigDecimal amount) {
        BigDecimal filteredAmount = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal itemsNeeded = filteredAmount.setScale(0, RoundingMode.CEILING);

        BigDecimal itemsAvailable = BigDecimal.ZERO;
        Result<Money> result = getItemsBalanceUseCase.execute(name, this.currencyName);
        if (result.isSuccess()) {
            itemsAvailable = result.getValue().getAmount();
        }

        BigDecimal amountToMove = itemsNeeded.min(itemsAvailable);

        if (amountToMove.compareTo(BigDecimal.ZERO) > 0) {
            this.depositItemUseCase.execute(name, this.currencyName, amountToMove);
        }
        return super.withdraw(name, filteredAmount, this.currencyName);
    }

    @Override
    public BigDecimal getBalance(UUID uuid){
        Result<Money> result = getItemsBalanceUseCase.execute(uuid, this.currencyName);
        BigDecimal balance = super.getBalance(uuid,this.currencyName);
        if (result.isSuccess()) return balance.add(result.getValue().getAmount());
        return balance;
    }

    @Override
    public BigDecimal getBalance(String name){
        Result<Money> result = getItemsBalanceUseCase.execute(name, this.currencyName);
        BigDecimal balance = super.getBalance(name,this.currencyName);
        if (result.isSuccess()) return balance.add(result.getValue().getAmount());
        return balance;
    }


    @Override
    public boolean hasAmount(UUID uuid, BigDecimal amount){
        return getBalance(uuid).compareTo(amount) >= 0;
    }

    @Override
    public boolean hasAmount(String name, BigDecimal amount) {
        return getBalance(name).compareTo(amount) >= 0;
    }
}
