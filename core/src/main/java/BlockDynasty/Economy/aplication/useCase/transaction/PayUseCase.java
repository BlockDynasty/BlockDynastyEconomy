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
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IPayUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.transactionsEvents.PayEvent;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.TransferResult;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;

public class PayUseCase extends TransferFundsUseCase implements IPayUseCase {
    private final Log economyLogger;
    private final EventManager eventManager;
    public PayUseCase(ICurrencyService currencyService,IAccountService accountService, IRepository dataStore,
                      Courier updateForwarder, Log economyLogger, EventManager eventManager) {
        super(currencyService, accountService,dataStore, updateForwarder, economyLogger, eventManager);
        this.economyLogger = economyLogger;
        this.eventManager = eventManager;
    }

    @Override
    protected void logSuccess(Account accountFrom, Account accountTo, ICurrency currency, BigDecimal amount) {
        this.economyLogger.log("[PAY] Account: " + accountFrom.getNickname() + " paid " + currency.format(amount) + " to " + accountTo.getNickname());
    }
    @Override
    protected void logFailure(Account accountFrom, Account accountTo, ICurrency currency, BigDecimal amount, Result<TransferResult> result) {
        this.economyLogger.log("[PAY Failed] Account: " + accountFrom.getNickname() + " paid " + currency.format(amount) + " to " + accountTo.getNickname() + " Error: " + result.getErrorMessage() + " Code: " + result.getErrorCode());
    }
    @Override
    protected void emitEvent(Account accountFrom, Account accountTo, ICurrency currency, BigDecimal amount) {
        this.eventManager.emit(new PayEvent(accountFrom.getPlayer(), accountTo.getPlayer(), currency, amount));
    }
}
