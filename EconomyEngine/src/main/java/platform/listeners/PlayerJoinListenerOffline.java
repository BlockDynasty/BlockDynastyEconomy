package platform.listeners;

import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import lib.commands.abstractions.IEntityCommands;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import utils.Console;

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
        Result<Account> result = getAccountByNameUseCase.execute(player.getName()); //esto falla y procede automaticamente a crear cuenta
        if (result.isSuccess()) {
            Result<Void> resultChangeUuid = accountService.checkUuidChange(result.getValue(), player.getUniqueId());
            if(!resultChangeUuid.isSuccess()){
                Console.logError(resultChangeUuid.getErrorMessage());
                player.sendMessage(ChatColor.stringValueOf(Colors.RED)+"Error loading your finance account. Please log in again or contact an administrator.");
                return;
            }
            accountService.addAccountToOnline(result.getValue());
        }else if(result.getErrorCode() == ErrorCode.ACCOUNT_NOT_FOUND){
                Result<Account> creationResult = createAccountUseCase.execute(player.getUniqueId(), player.getName());
                if (!creationResult.isSuccess()) {
                    Console.logError(creationResult.getErrorMessage());
                    player.sendMessage(ChatColor.stringValueOf(Colors.RED)+"Error creating your finance account. Please log in again or contact an administrator.");
                }
        }else{
            Console.logError(result.getErrorMessage());
            player.sendMessage(ChatColor.stringValueOf(Colors.RED)+"Error loading your finance account. Please log in again or contact an administrator.");
        }
    }
}
