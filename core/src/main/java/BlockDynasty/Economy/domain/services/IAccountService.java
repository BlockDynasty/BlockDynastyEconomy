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

package BlockDynasty.Economy.domain.services;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;

import java.util.*;

public interface IAccountService {
    void removeAccountOnline(UUID uuid);
    void removeAccountOnline(String name);
    void addAccountToOnline(Account account);
    List<Account> getAccountsOnline();
    List<Account> getAccountsOffline();
    Result<Account> getAccount(String name);
    Result<Account> getAccount(UUID uuid);
    Result<Account> getAccount(Player player);
    Account getAccountOnline(String name);
    Account getAccountOnline(UUID uuid);

    Result<Void> checkNameChange(Account account , String newName);
    Result<Void> checkUuidChange(Account account, UUID newUuid);
    void addAccountToTopList(Account account,String currencyName);
    List<Account> getAccountsTopList(String currency);
    void clearTopList();

    void syncOnlineAccount (Account account);
    void syncDbWithOnlineAccounts();
    void syncOnlineAccount(Player player);
}
