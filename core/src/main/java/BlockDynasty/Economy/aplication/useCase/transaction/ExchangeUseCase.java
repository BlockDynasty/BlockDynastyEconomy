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
import BlockDynasty.Economy.aplication.useCase.transaction.genericOperations.SingleAccountMultiCurrencyOp;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IExchangeUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.transactionsEvents.ExchangeEvent;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.courier.Message;
import BlockDynasty.Economy.domain.services.courier.PlayerTargetMessage;
import BlockDynasty.Economy.domain.services.log.Log;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeUseCase extends SingleAccountMultiCurrencyOp implements IExchangeUseCase {
    private Courier updateForwarder;
    private IAccountService accountService;
    private Log logger;
    private EventManager eventManager;
    private IRepository dataStore;

    public ExchangeUseCase(ICurrencyService currencyService, IAccountService accountService, IRepository dataStore, Courier updateForwarder,
                           Log economyLogger, EventManager eventManager) {
        super(accountService,currencyService,dataStore);
        this.updateForwarder = updateForwarder;
        this.accountService = accountService;
        this.logger = economyLogger;
        this.eventManager = eventManager;
        this.dataStore = dataStore;
    }

    //si solo si los valores de intercambio son mayor a 0 se realiza el intercambio
    public Result<BigDecimal> execute(Account account, ICurrency currencyFrom, ICurrency currencyTo, BigDecimal amountFrom, BigDecimal amountTo){
        //si el amountTo or amountFrom no son nullos y son negativos, error no se puede hacer el exchange entre estas dos monedas
        if (currencyFrom.getExchangeRate() <=0) {
            return Result.failure("The "+currencyFrom.getSingular() +" are not interchangeable.", ErrorCode.CURRENCY_HAS_NO_EXCHANGE_RATE);
        }

        if (currencyTo.getExchangeRate() <= 0){
            return Result.failure("The "+currencyTo.getSingular() +" are not interchangeable.", ErrorCode.CURRENCY_HAS_NO_EXCHANGE_RATE);
        }

        if(!currencyFrom.isInterchangeableWith(currencyTo)){
            return Result.failure( "The currencies are not interchangeable.", ErrorCode.CURRENCIES_NOT_INTERCHANGEABLE);
        }

        if (currencyFrom.equals(currencyTo)) {
            return Result.failure("Cannot exchange the same currency", ErrorCode.CURRENCY_MUST_BE_DIFFERENT);
        }

        if (account.isBlocked()){
            return Result.failure("Account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }
        if (!account.canReceiveCurrency()) {
            return Result.failure("Target account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        if(!currencyTo.isValidAmount(amountTo)){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        if (amountFrom == null) { //calculo atumatico segun el ratio
            amountFrom = amountTo.multiply(BigDecimal.valueOf(currencyFrom.getExchangeRate()))
                    .divide(BigDecimal.valueOf(currencyTo.getExchangeRate()), 4, RoundingMode.HALF_UP);
        }

        if (!currencyFrom.isValidAmount(amountFrom)) {
            return Result.failure("Decimal not supported for currency ", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<Account>  result =this.dataStore.exchange(account.getPlayer(),currencyFrom,amountFrom,currencyTo,amountTo);

        if(!result.isSuccess()){
            this.logger.log("[EXCHANGE failed] Account: " + account.getNickname() + " exchanged " + currencyFrom.format(amountFrom) + " to " + currencyTo.format(amountTo) + " Error: " + result.getErrorMessage() + " Code: " + result.getErrorCode());
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        this.accountService.syncOnlineAccount(result.getValue());

        this.updateForwarder.sendUpdateMessage(PlayerTargetMessage.builder()
                .setType(Message.Type.ACCOUNT)
                .setTarget(account.getUuid())
                .setTargetPlayer(account.getPlayer())
                .build());
        this.logger.log("[EXCHANGE] Account: " + account.getNickname() + " exchanged " + currencyFrom.format(amountFrom) + " to " + currencyTo.format(amountTo));
        this.eventManager.emit(new ExchangeEvent(account.getPlayer(),currencyFrom,currencyTo,amountFrom,currencyTo.getExchangeRate(),amountTo));

        return Result.success(amountFrom);
    }
}