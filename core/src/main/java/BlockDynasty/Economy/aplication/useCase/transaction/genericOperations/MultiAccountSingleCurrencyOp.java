package BlockDynasty.Economy.aplication.useCase.transaction.genericOperations;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class MultiAccountSingleCurrencyOp extends Operation {
    public MultiAccountSingleCurrencyOp(IAccountService accountService, ICurrencyService currencyService, IRepository dataStore) {
        super(accountService, currencyService, dataStore);
    }

    public Result<Void> execute(UUID userFrom, UUID targetUUID, String currencyName, BigDecimal amount){
        return this.execute(this.getAccountByUUIDUseCase.execute(userFrom),this.getAccountByUUIDUseCase.execute(targetUUID),currencyName,amount);
    };

    public Result<Void> execute(String userFrom,String targetName, String currencyName, BigDecimal amount){
        return this.execute(this.getAccountByNameUseCase.execute(userFrom),this.getAccountByNameUseCase.execute(targetName),currencyName,amount);
    };

    private Result<Void> execute(Result<Account> accountFromResult, Result<Account> accountToResult, String currency, BigDecimal amount) {
        if (!accountFromResult.isSuccess()) {
            return Result.failure(accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
        }
        if (!accountToResult.isSuccess()) {
            return Result.failure(accountToResult.getErrorMessage(), accountToResult.getErrorCode());
        }
        Result<ICurrency> currencyResult = this.searchCurrencyUseCase.getCurrency(currency);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }

        return execute(accountFromResult.getValue(), accountToResult.getValue(), currencyResult.getValue(), amount);
    }

    public abstract Result<Void> execute(Account accountFrom, Account accountTo, ICurrency currency, BigDecimal amount);
}
