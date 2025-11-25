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

public abstract class SingleAccountMultiCurrencyOp extends Operation{
    public SingleAccountMultiCurrencyOp(IAccountService accountService, ICurrencyService currencyService, IRepository dataStore) {
        super(accountService, currencyService, dataStore);
    }

    public Result<BigDecimal> execute(UUID accountUuid, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo){
            return this.execute(this.getAccountByUUIDUseCase.execute(accountUuid),currencyFromName, currencyToname, amountFrom, amountTo);
    };
    public Result<BigDecimal> execute(String accountString, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo){
            return this.execute(this.getAccountByNameUseCase.execute(accountString),currencyFromName, currencyToname, amountFrom, amountTo);
    };

    private Result<BigDecimal> execute(Result<Account> accountResult, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo) {
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<ICurrency> currencyFromResult = this.searchCurrencyUseCase.getCurrency(currencyFromName);
        if (!currencyFromResult.isSuccess()) {
            return Result.failure(currencyFromResult.getErrorMessage(), currencyFromResult.getErrorCode());
        }

        Result<ICurrency> currencyToResult = this.searchCurrencyUseCase.getCurrency(currencyToname);
        if (!currencyToResult.isSuccess()) {
            return Result.failure(currencyToResult.getErrorMessage(), currencyToResult.getErrorCode());
        }

        return execute(accountResult.getValue(), currencyFromResult.getValue(), currencyToResult.getValue(), amountFrom, amountTo);
    }

    public abstract Result<BigDecimal> execute(Account account, ICurrency currencyFrom, ICurrency currencyTo, BigDecimal amountFrom, BigDecimal amountTo);
}
