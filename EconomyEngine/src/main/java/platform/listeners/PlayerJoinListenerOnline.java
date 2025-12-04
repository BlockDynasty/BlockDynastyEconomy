package platform.listeners;

import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import lib.commands.abstractions.IEntityCommands;

public class PlayerJoinListenerOnline implements LoadAccount{
    private final GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final IAccountService accountService;

    public PlayerJoinListenerOnline( GetAccountByUUIDUseCase getAccountByUUIDUseCase,
                                    CreateAccountUseCase createAccountUseCase,
                                    IAccountService accountService) {
        this.getAccountByUUIDUseCase = getAccountByUUIDUseCase;
        this.createAccountUseCase = createAccountUseCase;
        this.accountService = accountService;
    }

    public void loadAccount(IEntityCommands player) {
        Result<Account> result = getAccountByUUIDUseCase.execute(player.getUniqueId());
        if (result.isSuccess()) {
            Result<Void> resultChangeName = accountService.checkNameChange(result.getValue(), player.getName());
            if(!resultChangeName.isSuccess()){
                player.kickPlayer("Error loading your finance account. Please log in again or contact an administrator.");
                return;
            }
            accountService.addAccountToOnline(result.getValue());
            return;
        }

        Result<Account> creationResult = createAccountUseCase.execute(player.getUniqueId(), player.getName());
        if (!creationResult.isSuccess()) {
            player.kickPlayer("Error creating or loading your finance account. Please log in again or contact an administrator.");
        }
    }
}
