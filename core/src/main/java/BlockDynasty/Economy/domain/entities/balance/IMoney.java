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

import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;

import java.math.BigDecimal;

public interface IMoney {
    Result<Void> subtract(BigDecimal amount);
    Result<Void> add(BigDecimal amount);
    Result<Void> setAmount(BigDecimal amount);

    void setCurrency(Currency currency);
    BigDecimal getAmount();
    Currency getCurrency();
    boolean hasEnough(BigDecimal amount);
}
