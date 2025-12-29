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
import BlockDynasty.Economy.aplication.useCase.transaction.genericOperations.MultiAccountSingleCurrencyOp;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.ITransferUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.transactionsEvents.TransferEvent;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.courier.Message;
import BlockDynasty.Economy.domain.services.log.Log;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.TransferResult;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;

public class TransferFundsUseCase extends MultiAccountSingleCurrencyOp implements ITransferUseCase {
    private IRepository dataStore;
    private Courier updateForwarder;
    private Log logger;
    private EventManager eventManager;
    private IAccountService accountService;

    public TransferFundsUseCase(ICurrencyService currencyService, IAccountService accountService, IRepository dataStore, Courier updateForwarder, Log economyLogger, EventManager eventManager) {
        super(accountService, currencyService, dataStore);
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.logger = economyLogger;
        this.eventManager = eventManager;
        this.accountService = accountService;
    }

    public Result<Void> execute(Account accountFrom, Account accountTo, ICurrency currency, BigDecimal amount) {
        if(!currency.isTransferable()){
            return Result.failure("Currency is not transferable", ErrorCode.CURRENCY_NOT_PAYABLE);
        }
        if (accountFrom.getUuid().equals(accountTo.getUuid()) || accountFrom.getNickname().equals(accountTo.getNickname())) {
            return Result.failure("You can't transfer to yourself", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }
        if (!accountTo.canReceiveCurrency()) {
            return Result.failure("Target account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }
        if (accountFrom.isBlocked()) {
            return Result.failure("Sender account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }
        if (accountTo.isBlocked()) {
            return Result.failure("Target account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }
        if (!currency.isValidAmount(amount)) {
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<TransferResult> result = this.dataStore.transfer(accountFrom.getPlayer(), accountTo.getPlayer(), currency, amount);
        if (!result.isSuccess()) {
            logFailure(accountFrom, accountTo, currency, amount, result);
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        updateCacheAndEmitEvents(accountFrom, accountTo, currency, amount, result);
        return Result.success();
    }

    private void updateCacheAndEmitEvents(Account accountFrom, Account accountTo, ICurrency currency, BigDecimal amount, Result<TransferResult> result) {
        this.accountService.syncOnlineAccount(result.getValue().getTo());
        this.accountService.syncOnlineAccount(result.getValue().getFrom());

        this.updateForwarder.sendUpdateMessage(Message.builder().setType(Message.Type.EVENT)
                .setData(new TransferEvent(accountFrom.getPlayer(),accountTo.getPlayer(), currency, amount).toJson())
                .setTarget( accountTo.getUuid())
                .build());
        logSuccess(accountFrom, accountTo, currency, amount);
        emitEvent(accountFrom, accountTo, currency, amount);
    }

    protected void logSuccess(Account accountFrom, Account accountTo, ICurrency currency, BigDecimal amount){
        this.logger.log("[TRANSFER] Account: " + accountFrom.getNickname() + " send " + currency.format(amount) + " to " + accountTo.getNickname());
    };

    protected void logFailure(Account accountFrom, Account accountTo, ICurrency currency, BigDecimal amount, Result<TransferResult> result){
        this.logger.log("[TRANSFER Failed] Account: " + accountFrom.getNickname() + " pay " + currency.format(amount) + " to " + accountTo.getNickname() + " Error: " + result.getErrorMessage() + " Code: " + result.getErrorCode());
    };

    protected void emitEvent(Account accountFrom, Account accountTo, ICurrency currency, BigDecimal amount){
        this.eventManager.emit(new TransferEvent(accountFrom.getPlayer(),accountTo.getPlayer(), currency, amount));
    };
}