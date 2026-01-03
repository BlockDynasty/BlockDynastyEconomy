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

import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.courier.Message;
import BlockDynasty.Economy.domain.services.courier.PlayerTargetMessage;

import java.util.UUID;

public class EditAccountUseCase {
    private final IRepository dataStore;
    private final Courier courier;
    private final GetAccountByUUIDUseCase getAccountByUUIDUseCase;

    public EditAccountUseCase(IAccountService accountService, IRepository dataStore, Courier courier) {
        this.dataStore = dataStore;
        this.courier = courier;
        this.getAccountByUUIDUseCase = new GetAccountByUUIDUseCase(accountService);
    }

    public Result<Void> blockReceive(UUID uuid){
        Result<Account> result = getAccountByUUIDUseCase.execute(uuid);

        if (result.isSuccess()) {
            result.getValue().setCanReceiveCurrency(false);
            update(result.getValue());
        } else {
            return Result.failure("Account not found", result.getErrorCode());
        }

        return Result.success();
    }

    public Result<Void> allowReceive(UUID uuid) {
        Result<Account> result = getAccountByUUIDUseCase.execute(uuid);

        if (result.isSuccess()) {
            result.getValue().setCanReceiveCurrency(true);
            update(result.getValue());
        } else {
            return Result.failure("Account not found", result.getErrorCode());
        }

        return Result.success();
    }

    public Result<Void> blockAccount(UUID uuid) {
        Result<Account> result = getAccountByUUIDUseCase.execute(uuid);

        if (result.isSuccess()) {
            result.getValue().block();
            update(result.getValue());
        } else {
            return Result.failure("Account not found", result.getErrorCode());
        }

        return Result.success();
    }

    public Result<Void> unblockAccount(UUID uuid) {
        Result<Account> result = getAccountByUUIDUseCase.execute(uuid);

        if (result.isSuccess()) {
            result.getValue().unblock();
            update(result.getValue());
        } else {
            return Result.failure("Account not found", result.getErrorCode());
        }

        return Result.success();
    }

    private void update(Account account) {
        dataStore.saveAccount(account);
        courier.sendUpdateMessage(PlayerTargetMessage.builder()
                .setType(Message.Type.ACCOUNT)
                .setTarget(account.getUuid())
                .setTargetPlayer(account.getPlayer())
                .build());
    }
}
