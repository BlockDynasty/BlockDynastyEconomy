package BlockDynasty.Economy.aplication.useCase.transaction.genericOperations;

import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByPlayerUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;

public abstract class Operation {
    protected final SearchCurrencyUseCase searchCurrencyUseCase;
    protected final GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    protected final GetAccountByNameUseCase getAccountByNameUseCase;
    protected final GetAccountByPlayerUseCase getAccountByPlayerUseCase;

    public Operation (IAccountService accountService, ICurrencyService currencyService, IRepository dataStore) {
        this.searchCurrencyUseCase = new SearchCurrencyUseCase(currencyService, dataStore);
        this.getAccountByNameUseCase = new GetAccountByNameUseCase(accountService);
        this.getAccountByUUIDUseCase = new GetAccountByUUIDUseCase(accountService);
        this.getAccountByPlayerUseCase = new GetAccountByPlayerUseCase(accountService);
    }
}
