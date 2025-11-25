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

import BlockDynasty.Economy.Core;
import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IWithdrawUseCase;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import mockClass.CourierTest;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.SearchAccountUseCase;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WithdrawUseCaseTest {
    Account nullplague;
    ICurrency dinero;
    IRepository repository;
    ICurrencyService currencyService;
    AccountService accountService;
    IWithdrawUseCase withdrawUseCase;
    SearchAccountUseCase searchAccountUseCase;
    Core core;
    UseCaseFactory useCaseFactory;

    @BeforeEach
    void setUp() {
        nullplague = new Account(UUID.randomUUID(), "nullplague");
        dinero= Currency.builder().setSingular("dinero").setPlural("dinero").build();

        repository = FactoryRepo.getDb();

       this.core = new Core(repository, 5, new CourierTest(),new LoggerTest());
       currencyService = core.getServicesManager().getCurrencyService();
       useCaseFactory = core.getUseCaseFactory();

        useCaseFactory.createCurrency().execute(dinero.getSingular(), dinero.getPlural());
        useCaseFactory.createAccount().execute(nullplague.getUuid(), nullplague.getNickname());
        useCaseFactory.deposit().execute(nullplague.getUuid(), "dinero", BigDecimal.valueOf(5000));

        withdrawUseCase = useCaseFactory.withdraw();
        searchAccountUseCase = useCaseFactory.searchAccount();

    }

    @Test
    void withdrawUseCaseTestWithFounds() {
        Result<Void> result =withdrawUseCase.execute(nullplague.getUuid(), "dinero", BigDecimal.valueOf(5000));
        assertEquals(true, result.isSuccess());
        Result<Money> accountResult =useCaseFactory.getBalance().execute(nullplague.getUuid(),"dinero");
        assertEquals(0,accountResult.getValue().getAmount().compareTo(BigDecimal.valueOf(0 )));
    }

    @Test
    void withdrawUseCaseTestWithoutFounds(){
        Result<Void> result = withdrawUseCase.execute(nullplague.getUuid(), "dinero", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.INSUFFICIENT_FUNDS, result.getErrorCode() );
    }

    @Test
    void withdrawUseCaseTestWithNegativeAmount(){
        Result<Void> result = withdrawUseCase.execute(nullplague.getNickname(), "dinero", BigDecimal.valueOf(-10000));
        assertEquals(ErrorCode.INVALID_AMOUNT, result.getErrorCode());
    }

    @Test
    void withdrawUseCaseTestWithNullAccount(){
        Result<Void> result = withdrawUseCase.execute("cris", "dinero", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void withdrawUseCaseTestWithNullCurrency(){
        Result<Void> result = withdrawUseCase.execute("nullplague", "oro", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void withdrawUseCaseTestWithNullBalance(){
        currencyService.add(Currency.builder().setSingular("oro").setPlural("oro").build());
        Result<Void> result = withdrawUseCase.execute(nullplague.getUuid(), "oro", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.ACCOUNT_NOT_HAVE_BALANCE, result.getErrorCode(), result.getErrorMessage() );
    }

    @Test
    void withdrawUseCaseTestWithCurrencyNoSupportDecimals(){
       useCaseFactory.editCurrency().toggleDecimals("dinero");

        Result<Void> result = withdrawUseCase.execute("nullplague", "dinero", BigDecimal.valueOf(1000.50));
        assertEquals(ErrorCode.DECIMAL_NOT_SUPPORTED, result.getErrorCode());
    }
    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
