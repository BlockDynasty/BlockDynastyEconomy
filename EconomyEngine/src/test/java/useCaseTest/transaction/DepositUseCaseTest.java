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
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import mockClass.CourierTest;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mockClass.LoggerTest;
import repositoryTest.FactoryRepo;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DepositUseCaseTest {
    Account nullplague;
    ICurrency dinero;
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
    DepositUseCase depositUseCase;
    SearchAccountUseCase searchAccountUseCase;
    SearchCurrencyUseCase searchCurrencyUseCase;
    CreateAccountUseCase createAccountUseCase;
    CreateCurrencyUseCase createCurrenyUseCase;

    @BeforeEach
    void setUp() {
        nullplague = new Account(UUID.randomUUID(), "nullplague");
        dinero= Currency.builder().setSingular("dinero").setPlural("dinero").build();

        repository = FactoryRepo.getDb();
        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5 ,repository, currencyService);

        searchAccountUseCase = new SearchAccountUseCase(accountService, repository);
        searchCurrencyUseCase = new SearchCurrencyUseCase(currencyService, repository);
        createAccountUseCase = new CreateAccountUseCase(accountService, currencyService,repository);
        createCurrenyUseCase = new CreateCurrencyUseCase(currencyService, accountService, new CourierTest(), repository);
        depositUseCase = new DepositUseCase( accountService,currencyService,repository,new CourierTest(),new LoggerTest(),new EventManager());

        createCurrenyUseCase.execute(dinero.getSingular(), dinero.getPlural());
        createAccountUseCase.execute(nullplague.getUuid(), nullplague.getNickname());
    }

    @Test
    void depositUseCaseTest() {
        Result<Void> result = depositUseCase.execute(nullplague.getUuid(), "dinero", BigDecimal.valueOf(5000));

        assertTrue( result.isSuccess(),"Expected success, but got: " + result.getErrorMessage() + " " + result.getErrorCode());
        assertEquals(BigDecimal.valueOf(5000).setScale(2), searchAccountUseCase.execute("nullplague").getValue().getMoney("dinero").getAmount().setScale(2));
    }

    /*@Test
    void depositUseCaseTestWithoutBalance(){
        Result<Void> result = depositUseCase.execute(nullplague.getUuid(), "oro", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, result.getErrorCode(),  result.getErrorMessage()+"  "+result.getErrorCode());;
    }*/


    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
