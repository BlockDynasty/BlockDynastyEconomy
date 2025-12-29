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

package BlockDynasty.Economy.domain.persistence.entities;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.Player;

import java.util.List;
import java.util.UUID;

public interface IAccountRepository {
    List<Account> findAll();
    Account findByUuid(UUID uuid);
    Account findByNickname(String nickname);
    Account findByPlayer(Player player);
    void save(Player account, Account newStateAccount);
    void save(Account account);
    void delete(Player account);
    void update(Player account,Account newStateAccount);
    void update(Account account);
    void create(Account account);
    List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset);

}
