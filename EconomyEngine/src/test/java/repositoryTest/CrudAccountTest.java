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

package repositoryTest;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.Exceptions.AccountNotFoundException;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IAccountRepository;
import repository.AccountRepository;
import repository.CurrencyRepository;
import repositoryTest.ConnectionHandler.MockConnectionHibernateH2;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CrudAccountTest {
   IAccountRepository accountRepository = new AccountRepository(new MockConnectionHibernateH2().getSession());
   CurrencyRepository currencyRepository = new CurrencyRepository(new MockConnectionHibernateH2().getSession());

    @Test
    public void testCreateAccount() {
        Currency currency = new Currency(UUID.randomUUID(),"dinero","dinero");
        Currency currency2 = new Currency(UUID.randomUUID(),"coin","coin");
        currencyRepository.create(currency2);
       currencyRepository.create(currency);

        Account account = new Account(UUID.randomUUID(), "nullplague");
        account.setMoney(currency, BigDecimal.valueOf(1000));
        account.setMoney(currency2, BigDecimal.valueOf(1000));
        accountRepository.create(account);

        account.add(currency, BigDecimal.valueOf(1000));
        accountRepository.update(account);

        Account foundAccount = accountRepository.findByUuid(account.getUuid().toString());
        assertEquals(account.getUuid(), foundAccount.getUuid(), "The account UUID should match");
        assertEquals(BigDecimal.valueOf(2000).setScale(2), foundAccount.getMoney(currency).getAmount(), "The account balance should match");

    }

    @Test
    public void testUpdateAccount() {
        Currency currency = new Currency(UUID.randomUUID(),"dinero","dinero");
        currencyRepository.create(currency);

        Account account = new Account(UUID.randomUUID(), "nullplague");
        accountRepository.create(account);

        account.setNickname("updatedName");
        account.setMoney(currency, BigDecimal.valueOf(1000));
        accountRepository.save(account);

        Account updatedAccount = accountRepository.findByUuid(account.getUuid().toString());
        assertEquals("updatedName", updatedAccount.getNickname());
        assertEquals(BigDecimal.valueOf(1000).setScale(2), updatedAccount.getMoney(currency).getAmount());
    }

    @Test
    public  void testDeleteAccount() {
        Currency currency = new Currency(UUID.randomUUID(),"dinero","dinero");
        currencyRepository.create(currency);

        Account account = new Account(UUID.randomUUID(), "nullplague");
        account.setMoney(currency, BigDecimal.valueOf(1000));
        accountRepository.create(account);

        accountRepository.delete(account);

        assertThrows(AccountNotFoundException.class ,()->{ accountRepository.findByUuid(account.getUuid().toString()); });
    }
}
