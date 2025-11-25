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
import BlockDynasty.Economy.aplication.useCase.transaction.genericOperations.MultiAccountMultiCurrencyOp;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.ITradeUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.transactionsEvents.TradeEvent;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.TransferResult;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;

public class TradeCurrenciesUseCase extends MultiAccountMultiCurrencyOp implements ITradeUseCase {
    private IRepository dataStore;
    private Courier updateForwarder;
    private Log logger;
    private IAccountService accountService;
    private EventManager eventManager;

    public TradeCurrenciesUseCase(ICurrencyService currencyService, IAccountService accountService, IRepository dataStore,
                                  Courier updateForwarder, Log economyLogger, EventManager eventManager) {
        super( accountService, currencyService, dataStore);
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.logger = economyLogger;
        this.accountService = accountService;
        this.eventManager = eventManager;
    }

    public Result<Void> execute(Account accountFrom, Account accountTo, ICurrency currencyFrom, ICurrency currencyTo, BigDecimal amountFrom, BigDecimal amountTo){
        if(!currencyFrom.isTransferable() || !currencyTo.isTransferable()){
            return Result.failure("Currency not transferable", ErrorCode.CURRENCY_NOT_PAYABLE);
        }

        if (accountFrom.getUuid().equals(accountTo.getUuid()) || accountFrom.getNickname().equals(accountTo.getNickname())) {
            return Result.failure("You can't trade with yourself", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        if (accountFrom.isBlocked()){
            return Result.failure("Sender account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }

        if (accountTo.isBlocked()){
            return Result.failure("Target account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }

        if(!accountTo.canReceiveCurrency()){
            return Result.failure("Account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        if(amountFrom.compareTo(BigDecimal.ZERO) <= 0){
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }
        if(amountTo.compareTo(BigDecimal.ZERO) <= 0){
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }

        if(!currencyFrom.isValidAmount(amountFrom)){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        if(!currencyTo.isValidAmount(amountTo)){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<TransferResult> result = this.dataStore.trade( accountFrom.getUuid().toString(), accountTo.getUuid().toString(), currencyFrom, currencyTo, amountFrom, amountTo);
        if(!result.isSuccess()){
            this.logger.log("[TRADE failed] Account: " + accountFrom.getNickname() + " traded " + currencyFrom.format(amountFrom) + " to " + accountTo.getNickname() + " for " + currencyTo.format(amountTo) + " - Error: " + result.getErrorMessage() + " - Code: " + result.getErrorCode());
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        this.accountService.syncOnlineAccount(result.getValue().getTo());
        this.accountService.syncOnlineAccount(result.getValue().getFrom());

        this.logger.log("[TRADE] Account: " + accountFrom.getNickname() + " traded " + currencyFrom.format(amountFrom) + " to " + accountTo.getNickname() + " for " + currencyTo.format(amountTo));
        this.eventManager.emit(new TradeEvent(accountFrom.getPlayer(),accountTo.getPlayer(), currencyFrom, currencyTo, amountFrom, amountTo));
        this.updateForwarder.sendUpdateMessage("event", new TradeEvent(accountFrom.getPlayer(),accountTo.getPlayer(), currencyFrom, currencyTo, amountFrom, amountTo).toJson(), accountTo.getUuid().toString());
        this.updateForwarder.sendUpdateMessage("event", new TradeEvent(accountFrom.getPlayer(),accountTo.getPlayer(), currencyFrom, currencyTo, amountFrom, amountTo).toJson(), accountFrom.getUuid().toString());

        return Result.success();
    }
}
