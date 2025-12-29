/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package BlockDynasty.Economy.aplication.useCase.account;

import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;

import java.util.UUID;

public class DeleteAccountUseCase {
    private IRepository repository;
    private IAccountService accountService;
    private GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    private GetAccountByNameUseCase getAccountByNameUseCase;

    public DeleteAccountUseCase(IRepository repository, IAccountService accountService) {
        this.getAccountByNameUseCase = new GetAccountByNameUseCase(accountService);
        this.getAccountByUUIDUseCase = new GetAccountByUUIDUseCase(accountService);
        this.repository= repository;
        this.accountService = accountService;
    }

    public Result<Void> execute(Player account){
        Result<Void> result =repository.deleteAccount(account);
        accountService.removeAccountOnline(account.getUuid());
        if (!result.isSuccess()) {
            return Result.failure("Failed to delete account for player: " + account.getNickname(), result.getErrorCode());
        }
        return Result.success();
    }

    public Result<Void> execute(String name){
        // Primero, obtenemos la cuenta del jugador por su nombre
        Result<Account> accountResult = getAccountByNameUseCase.execute(name);
        if (!accountResult.isSuccess()) {
            return Result.failure("Account not found for player: " + name, accountResult.getErrorCode());
        }
        Result<Void> result =repository.deleteAccount(accountResult.getValue().getPlayer());
        accountService.removeAccountOnline(accountResult.getValue().getUuid());
        if (!result.isSuccess()) {
            return Result.failure("Failed to delete account for player: " + name, result.getErrorCode());
        }
        return Result.success();
    }

    public Result<Void> execute(UUID uuid){
        // Primero, obtenemos la cuenta del jugador por su UUID
        Result<Account> accountResult = getAccountByUUIDUseCase.execute(uuid);
        if (!accountResult.isSuccess()) {
            return Result.failure("Account not found for UUID: " + uuid, accountResult.getErrorCode());
        }
        Result<Void> result = repository.deleteAccount(accountResult.getValue().getPlayer());
        accountService.removeAccountOnline(accountResult.getValue().getUuid());
        if (!result.isSuccess()) {
            return Result.failure("Failed to delete account for UUID: " + uuid, result.getErrorCode());
        }
        return Result.success();
    }
}
