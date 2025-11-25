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

package BlockDynasty.Economy.domain.persistence.transaction;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.result.TransferResult;

import java.math.BigDecimal;

// transactions interface for handling various financial operations
public interface ITransactions {
    Result<TransferResult> transfer(String fromUuid, String toUuid, ICurrency currency, BigDecimal amount);
    Result<Account> withdraw(String accountUuid, ICurrency currency, BigDecimal amount);
    Result<Account> deposit(String accountUuid, ICurrency currency, BigDecimal amount);
    Result<Account> exchange(String fromUuid, ICurrency fromCurrency, BigDecimal amountFrom,  ICurrency toCurrency,BigDecimal amountTo);
    Result<TransferResult> trade(String fromUuid, String toUuid, ICurrency fromCurrency, ICurrency toCurrency, BigDecimal amountFrom, BigDecimal amountTo);
    Result<Account> setBalance(String accountUuid, ICurrency currency, BigDecimal amount);
}
