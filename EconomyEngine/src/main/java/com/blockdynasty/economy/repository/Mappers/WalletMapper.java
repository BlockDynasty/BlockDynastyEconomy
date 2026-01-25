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
import BlockDynasty.Economy.domain.entities.wallet.Wallet;
import com.blockdynasty.economy.repository.Models.WalletDb;

import java.util.List;
import java.util.stream.Collectors;

public class WalletMapper {
    public static WalletDb toEntity(Wallet domain) {
        if (domain == null) {
            return null;
        }
        WalletDb entity = new WalletDb();
        entity.setBalances(
                domain.getBalances().stream()
                        .map(BalanceMapper::toEntity)
                        .collect(Collectors.toList())
        );
        return entity;
    }


    public static Wallet toDomain(WalletDb entity) {
        if (entity == null) {
            return null;
        }
        List<Money> monies = entity.getBalances().stream()
                .map(BalanceMapper::toDomain)
                .collect(Collectors.toList());
        return new Wallet(monies);
    }
}
