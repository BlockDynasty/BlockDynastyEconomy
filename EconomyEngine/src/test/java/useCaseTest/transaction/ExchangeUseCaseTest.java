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

package useCaseTest.transaction;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import mockClass.CourierTest;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mockClass.LoggerTest;
import repositoryTest.FactoryRepo;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeUseCaseTest {
    Account player;
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
    SearchAccountUseCase searchAccountUseCase;
    SearchCurrencyUseCase searchCurrencyUseCase;
    EditCurrencyUseCase editCurrencyUseCase;
    ExchangeUseCase exchangeUseCase;
    ICurrency coin;
    ICurrency dinero;

    @BeforeEach
    void setUp() {
        // Keep the same currency setup
        coin =Currency.builder().setSingular("Coin").setPlural("Coins").build();
        coin.setExchangeRate(1.0);
        coin.setDecimalSupported(false); // Coins don't support decimals

        dinero = Currency.builder().setSingular("dinero").setPlural("dinero").build();
        dinero.setExchangeRate(30000.0);

        // Increase the initial dinero balance to have enough for the test
        player = new Account(UUID.randomUUID(), "player");
        player.setMoney(coin, BigDecimal.valueOf(100));
        player.setMoney(dinero, BigDecimal.valueOf(40000)); // Increase from 10 to 40000

        // Initialize repository and save data
        repository = FactoryRepo.getDb();
        repository.saveCurrency(coin);
        repository.saveCurrency(dinero);

        try {
            repository.createAccount(player);
        } catch (Exception e) {
            fail("Error saving account: " + e.getMessage());
        }

        // Initialize caches
        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5 , repository, currencyService);

        // Initialize use cases
        searchAccountUseCase = new SearchAccountUseCase(accountService, repository);
        searchCurrencyUseCase = new SearchCurrencyUseCase(currencyService, repository);
        editCurrencyUseCase = new EditCurrencyUseCase(currencyService, new CourierTest(),repository);

        // Initialize the exchange use case to test
        exchangeUseCase = new ExchangeUseCase(currencyService,accountService,
                repository, new CourierTest(), new LoggerTest(),new EventManager());
    }

    @Test
    void exchangeCurrencySuccessTest() {
        //quiero 1 coin a cambio de dinero
        editCurrencyUseCase.addInterchangeableCurrency("Dinero", "coin");
        Result<BigDecimal> result = exchangeUseCase.execute("player", "Dinero", "coin", null, BigDecimal.valueOf(1));

        System.out.println(result.getErrorCode());
        assertTrue(result.isSuccess());

        // The result should be the amount of dinero received
        assertEquals(BigDecimal.valueOf(30000).doubleValue(), result.getValue().doubleValue());

        // Verify balances were updated correctly
        Account updatedAccount = searchAccountUseCase.execute("player").getValue();
        assertEquals(BigDecimal.valueOf(101).setScale(2), updatedAccount.getMoney(coin).getAmount().setScale(2));
        assertEquals(BigDecimal.valueOf(10000).doubleValue(), updatedAccount.getMoney(dinero).getAmount().doubleValue());
    }

    @Test
    void exchangeCurrencyNotInterchangeable() {
        editCurrencyUseCase.setCurrencyRate("coin",0);

        Result<BigDecimal> result = exchangeUseCase.execute("player", "Dinero", "coin", null, BigDecimal.valueOf(1));

        assertFalse(result.isSuccess());
        System.out.println(result.getErrorCode());
    }

    //errores a evaluar:  cuenta no encontrada,moneda no encontrada, monto negativo, saldo insuficiente.
    //pd:estos intercambios se calculan de manera automatica segun el rate de las monedas. O tambien se aceptan 2 montos de entradas para cambiar lo solicitado

    @AfterEach
    void clearDb() {
        // Optional cleanup
    }
}