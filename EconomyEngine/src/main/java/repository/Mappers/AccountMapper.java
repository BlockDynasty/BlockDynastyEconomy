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

package repository.Mappers;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.wallet.Wallet;
import repository.Models.AccountDb;
import repository.Models.WalletDb;

import java.util.UUID;

public class AccountMapper {
    public static AccountDb toEntity(Account domain) {
        if (domain == null) {
            return null;
        }
        AccountDb accountDb = new AccountDb();
        accountDb.setUuid(domain.getUuid().toString());
        accountDb.setNickname(domain.getNickname());
        accountDb.setCanReceiveCurrency(domain.canReceiveCurrency());
        accountDb.setBlock(domain.isBlocked());

        // Create wallet or find existing one (this would need repository access)
        WalletDb walletDb = WalletMapper.toEntity(domain.getWallet());
        accountDb.setWallet(walletDb);

        return accountDb;
    }

    public static Account toDomain(AccountDb entity) {
        if (entity == null) {
            return null;
        }
        Wallet wallet = WalletMapper.toDomain(entity.getWallet());
        return new Account(entity.getId(),UUID.fromString(entity.getUuid()), entity.getNickname(), wallet, entity.isCanReceiveCurrency(),entity.isBlocked());
    }
}