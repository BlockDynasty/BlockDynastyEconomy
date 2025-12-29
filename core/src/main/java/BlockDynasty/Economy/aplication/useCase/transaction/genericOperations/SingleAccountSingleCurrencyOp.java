package BlockDynasty.Economy.aplication.useCase.transaction.genericOperations;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class SingleAccountSingleCurrencyOp extends Operation{
    public SingleAccountSingleCurrencyOp(IAccountService accountService, ICurrencyService currencyService, IRepository dataStore) {
        super(accountService, currencyService, dataStore);
    }

    public Result<Void> execute(String name, BigDecimal amount) {
        return this.execute(this.getAccountByNameUseCase.execute(name), null, amount,Context.OTHER);
    }

    public Result<Void> execute(UUID name, BigDecimal amount) {
        return this.execute(this.getAccountByUUIDUseCase.execute(name), null, amount,Context.OTHER);
    }

    public Result<Void> execute(Player player, BigDecimal amount) {
        return this.execute(this.getAccountByPlayerUseCase.execute(player), null, amount,Context.OTHER);
    }

    public Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount){
        return this.execute(targetUUID, currencyName, amount, Context.OTHER);
    };

    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount){
        return  this.execute(targetName, currencyName, amount, Context.OTHER);
    };

    public Result<Void> execute(Player targetPlayer, String currencyName, BigDecimal amount){
        return this.execute(this.getAccountByPlayerUseCase.execute(targetPlayer), currencyName, amount,Context.OTHER);
    }

    public Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount, Context context) {
        return this.execute(this.getAccountByUUIDUseCase.execute(targetUUID), currencyName, amount,context);
    }

    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount,Context context) {
        return this.execute(this.getAccountByNameUseCase.execute(targetName), currencyName, amount,context);
    }

    public  Result<Void> execute(Player targetPlayer, String currencyName, BigDecimal amount, Context context) {
        return this.execute(this.getAccountByPlayerUseCase.execute(targetPlayer), currencyName, amount,context);
    }

    private Result<Void> execute(Result<Account> accountResult, String currencyName, BigDecimal amount, Context context) {
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<ICurrency> currencyResult = currencyName == null ? this.searchCurrencyUseCase.getDefaultCurrency() : this.searchCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }
        return execute(accountResult.getValue(), currencyResult.getValue(), amount,context);
    }

    public abstract Result<Void> execute(Account account, ICurrency currency, BigDecimal amount, Context context);
}
