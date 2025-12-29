package platform.listeners;

import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import lib.commands.abstractions.IEntityCommands;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import utils.Console;

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
        Result<Account> result = getAccountByUUIDUseCase.execute(player.getUniqueId());//esto falla y procede automaticamente a crear cuenta
        if (result.isSuccess()) {
            Result<Void> resultChangeName = accountService.checkNameChange(result.getValue(), player.getName());
            if(!resultChangeName.isSuccess()){
                Console.logError(resultChangeName.getErrorMessage());
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
