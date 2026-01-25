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

package com.blockdynasty.economy.repository.Mappers;

import BlockDynasty.Economy.domain.entities.balance.Money;
import com.blockdynasty.economy.repository.Models.BalanceDb;

public class BalanceMapper {
    public static BalanceDb toEntity(Money domain) {
        if (domain == null) return null;

        BalanceDb entity = new BalanceDb();
        entity.setAmount(domain.getAmount());
        // El ID y relaciones se establecerán en el WalletMapper
        return entity;
    }


    public static Money toDomain(BalanceDb entity) {
        if (entity == null) {
            return null;
        }
        return new Money(CurrencyMapper.toDomain(entity.getCurrency()), entity.getAmount() );
    }
}