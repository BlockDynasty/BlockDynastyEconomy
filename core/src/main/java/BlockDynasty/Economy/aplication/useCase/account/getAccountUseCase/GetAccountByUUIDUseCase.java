package BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;

import java.util.UUID;

public class GetAccountByUUIDUseCase {
    private final IAccountService accountService;

    public GetAccountByUUIDUseCase(IAccountService accountService) {
        this.accountService = accountService;
    }

    public Result<Account> execute(UUID uuid) {
        return this.accountService.getAccount(uuid);
        //podemos hacer un new Account(account); como programación defensiva, o elaborar un value Object para mostrar valores hacia el exterior
    }
}
