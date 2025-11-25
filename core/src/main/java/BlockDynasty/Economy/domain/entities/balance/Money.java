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

package BlockDynasty.Economy.domain.entities.balance;

import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;

public class Money implements IMoney{
    private ICurrency currency;
    private BigDecimal amount;

    public Money(ICurrency currency){
        this.currency= currency;
        this.amount = currency.getDefaultBalance();
    }

    public Money(ICurrency currency, BigDecimal amount){
        this.currency= currency;
        this.amount = amount;
    }

    public Money(Money money){
        this.currency = Currency.builder().copy(money.getCurrency()).build();
        this.amount = money.getAmount();
    }

    public void setCurrency(ICurrency currency){
        this.currency = currency;
    }

    public BigDecimal getAmount(){
        return this.amount;
    }

    public ICurrency getCurrency() {
        return currency;
    }

    public String format(){
        return this.currency.format(amount);
    }

    public boolean hasEnough(BigDecimal amount){
        return this.amount.compareTo(amount) >= 0;
    };

    public Result<Void> setAmount(BigDecimal amount){
        if(!this.currency.isValidAmount(amount)){
            return Result.failure("Invalid amount for currency", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }
        if(amount.doubleValue() < 0){
            return Result.failure("Amount must be greater than -1", ErrorCode.INVALID_AMOUNT);
        }
        this.amount = amount;
        return Result.success(null);
    }

    public Result<Void> subtract(BigDecimal amount) {
        if(!this.currency.isValidAmount(amount)){
            return Result.failure("Invalid amount for currency", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.failure("Amount must be greater than -1", ErrorCode.INVALID_AMOUNT);
        }
        if (!this.hasEnough(amount)) {
            return Result.failure("Insufficient funds", ErrorCode.INSUFFICIENT_FUNDS);
        }
        this.amount = this.amount.subtract(amount);
        return Result.success(null);
    }

    public Result<Void> add(BigDecimal amount) {
        if(!this.currency.isValidAmount(amount)){
            return Result.failure("Invalid amount for currency", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.failure("Amount must be greater than -1", ErrorCode.INVALID_AMOUNT);
        }
        this.amount = this.amount.add(amount);
        return Result.success(null);
    }

    @Override
    public String toString() {
        return "Money{" +
                "currency=" + currency +
                ", amount=" + amount.toString() +
                '}';
    }
}
