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

public abstract class MultiAccountMultiCurrencyOp extends Operation {
    public MultiAccountMultiCurrencyOp(IAccountService accountService, ICurrencyService currencyService, IRepository dataStore) {
        super(accountService, currencyService, dataStore);
    }

    public Result<Void> execute(UUID userFrom, UUID userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        return this.execute(this.getAccountByUUIDUseCase.execute(userFrom), this.getAccountByUUIDUseCase.execute(userTo), currencyFromS, currencyToS, amountFrom, amountTo);
    };
    public Result<Void> execute(String userFrom, String userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        return this.execute(this.getAccountByNameUseCase.execute(userFrom), this.getAccountByNameUseCase.execute(userTo), currencyFromS, currencyToS, amountFrom, amountTo);
    };

    private Result<Void> execute(Result<Account> accountFromResult,Result<Account> accountToResult,  String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        if (!accountFromResult.isSuccess()) {
            return Result.failure(accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
        }

        if (!accountToResult.isSuccess()) {
            return Result.failure(accountToResult.getErrorMessage(), accountToResult.getErrorCode());
        }

        Result<ICurrency> currencyFromResult =  this.searchCurrencyUseCase.getCurrency(currencyFromS);
        if (!currencyFromResult.isSuccess()) {
            return Result.failure(currencyFromResult.getErrorMessage(), currencyFromResult.getErrorCode());
        }

        Result<ICurrency> currencyToResult =  this.searchCurrencyUseCase.getCurrency(currencyToS);
        if (!currencyToResult.isSuccess()) {
            return Result.failure(currencyToResult.getErrorMessage(), currencyToResult.getErrorCode());
        }

        return execute(accountFromResult.getValue(), accountToResult.getValue(), currencyFromResult.getValue(), currencyToResult.getValue(), amountFrom, amountTo);
    }

    public abstract Result<Void> execute(Account accountFrom, Account accountTo, ICurrency currencyFrom, ICurrency currencyTo, BigDecimal amountFrom, BigDecimal amountTo);
}
