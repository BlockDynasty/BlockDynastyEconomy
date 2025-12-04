package platform.listeners;

import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import lib.commands.abstractions.IEntityCommands;

public class PlayerJoinListenerOffline implements LoadAccount{
    private final GetAccountByNameUseCase getAccountByNameUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final IAccountService accountService;

    public PlayerJoinListenerOffline( GetAccountByNameUseCase getAccountByNameUseCase,
                                     CreateAccountUseCase createAccountUseCase,
                                     IAccountService accountService) {
        this.getAccountByNameUseCase = getAccountByNameUseCase;
        this.createAccountUseCase = createAccountUseCase;
        this.accountService = accountService;
    }

    @Override
    public void loadAccount(IEntityCommands player) {
        Result<Account> result = getAccountByNameUseCase.execute(player.getName());
        if (result.isSuccess()) {
            Result<Void> resultChangeUuid = accountService.checkUuidChange(result.getValue(), player.getUniqueId());
            if(!resultChangeUuid.isSuccess()){
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
