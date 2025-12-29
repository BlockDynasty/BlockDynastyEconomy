package BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;


public class GetAccountByPlayerUseCase {
    private IAccountService accountService;

    public GetAccountByPlayerUseCase(IAccountService accountService) {
        this.accountService = accountService;
    }

    public Result<Account> execute(Player player) {
        return this.accountService.getAccount(player);
    }
}
