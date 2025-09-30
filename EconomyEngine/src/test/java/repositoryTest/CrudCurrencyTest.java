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

import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.entities.ICurrencyRepository;

import repository.CurrencyRepository;
import repositoryTest.ConnectionHandler.MockConnectionHibernateH2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CrudCurrencyTest {
        ICurrencyRepository currencyRepository = new CurrencyRepository(new MockConnectionHibernateH2().getSession());
        @BeforeEach
        public void setUp() {
        }

        @Test
        public void testCreate() {
            Currency currency = new Currency(UUID.randomUUID(),"dinero","dinero");
            this.currencyRepository.create(currency);
        }

        @Test
        public void testFindAll() {
            // Create the currency within a transaction
            Currency currency = new Currency(UUID.randomUUID(), "dinero", "dineros");
            Currency currency2 = new Currency(UUID.randomUUID(), "coin", "coins");

            currencyRepository.create(currency);
            currencyRepository.create(currency2);
            List<Currency> currencies = currencyRepository.findAll();
            assertEquals(2, currencies.size(), "Should find two currencies");
        }
        @Test
        public void testFindByName() {
            // Create the currency within a transaction
            Currency currency = new Currency(UUID.randomUUID(), "dinero", "dineros");

            currencyRepository.create(currency);
            Currency currencyDb = currencyRepository.findByName(currency.getSingular());
            assertEquals("dinero", currencyDb.getSingular());

        }
        @Test
        public void testFindByUuid() {
            // Create the currency within a transaction
            Currency currency = new Currency(UUID.randomUUID(), "dinero", "dinero");

            currencyRepository.create(currency);
            Currency currencyDb = currencyRepository.findByUuid(currency.getUuid().toString());

            assertEquals("dinero", currencyDb.getSingular());

        }
        @Test
        public void testSave() {
            // Create the currency within a transaction
            Currency currency = new Currency(UUID.randomUUID(), "dinero", "dinero"); //dos monedas iguales cargadas en memoria = error

            currencyRepository.create(currency);
            currency.setSingular("dinerito");
            currencyRepository.save(currency);

            Currency currencyDb = currencyRepository.findByUuid(currency.getUuid().toString());

            assertEquals("dinerito", currencyDb.getSingular());
        }
        @Test
        public void testDelete() {
            // Create the currency within a transaction
            Currency currency = new Currency(UUID.randomUUID(), "dinero", "dinero");

           currencyRepository.create(currency);
            currencyRepository.delete(currency);

            assertThrows(CurrencyNotFoundException.class, () -> currencyRepository.findByName(currency.getSingular()));
        }
        @Test
        public void testUpdate() {
            // Create the currency within a transaction
            Currency currency = new Currency(UUID.randomUUID(), "dinero", "dinero");
            currencyRepository.create(currency);
            currency.setSingular("dinerito");
            currencyRepository.update(currency);

            Currency currencyFind =currencyRepository.findByName(currency.getSingular());

            assertEquals(currencyFind.getSingular(), currency.getSingular());
        }

}
