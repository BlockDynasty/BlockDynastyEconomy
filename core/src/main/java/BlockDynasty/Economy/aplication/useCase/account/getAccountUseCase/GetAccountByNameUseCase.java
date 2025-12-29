package BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;

public class GetAccountByNameUseCase {
    private final IAccountService accountService;

    public GetAccountByNameUseCase(IAccountService accountService) {
        this.accountService = accountService;
    }

    public Result<Account> execute(String name) {
        return this.accountService.getAccount(name);
        //podemos hacer un new Account(account); como programación defensiva, o elaborar un value Object para mostrar valores hacia el exterior
    }
}
