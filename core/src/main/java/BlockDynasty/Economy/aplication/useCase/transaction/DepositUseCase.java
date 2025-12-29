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

package BlockDynasty.Economy.aplication.useCase.transaction;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.useCase.transaction.genericOperations.SingleAccountSingleCurrencyOp;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IDepositUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.events.offersEvents.OfferCreated;
import BlockDynasty.Economy.domain.events.transactionsEvents.DepositEvent;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.courier.Message;
import BlockDynasty.Economy.domain.services.log.Log;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;

public class DepositUseCase extends SingleAccountSingleCurrencyOp implements IDepositUseCase {
    private final IRepository dataStore;
    private final IAccountService accountService;
    private final Log logger;
    private final Courier updateForwarder;
    private final EventManager eventManager;

    public DepositUseCase(IAccountService accountService, ICurrencyService currencyService,
                          IRepository dataStore, Courier updateForwarder, Log logger, EventManager eventManager) {
        super(accountService, currencyService, dataStore);
        this.dataStore = dataStore;
        this.accountService = accountService;
        this.logger = logger;
        this.updateForwarder = updateForwarder;
        this.eventManager = eventManager;
    }

    @Override
    public Result<Void> execute(Account account, ICurrency currency, BigDecimal amount, Context context) {
        if (!account.canReceiveCurrency()) {
            return Result.failure("Account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        if (account.isBlocked()){
            return Result.failure("Account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }

        if(!currency.isValidAmount(amount)){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<Account> result = this.dataStore.deposit(account.getPlayer(), currency, amount);
        if(!result.isSuccess()){
            this.logger.log("[DEPOSIT failed] Account: " + account.getNickname() + " recibió un deposito de " + currency.format(amount) + " de " + currency.getSingular() + " pero falló: " + result.getErrorMessage() + " (" + result.getErrorCode() + ")");
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        this.accountService.syncOnlineAccount(result.getValue());

        this.updateForwarder.sendUpdateMessage( Message.builder()
                .setType(Message.Type.EVENT)
                .setData(new DepositEvent(account.getPlayer(), currency, amount,context).toJson())
                .setTarget(account.getPlayer().getUuid())
                .build());
        this.logger.log("[DEPOSIT] Account: " + account.getNickname() + " has received a deposit of " + currency.format(amount) + " " + currency.getSingular());
        this.eventManager.emit(new DepositEvent(account.getPlayer(), currency, amount,context));

        return Result.success();
    }
}