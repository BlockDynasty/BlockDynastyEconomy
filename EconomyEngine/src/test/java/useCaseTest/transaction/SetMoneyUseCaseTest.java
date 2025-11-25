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
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.ISetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import mockClass.CourierTest;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.transaction.SetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import BlockDynasty.Economy.domain.entities.currency.Currency;

import mockClass.LoggerTest;
import repositoryTest.FactoryRepo;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SetMoneyUseCaseTest {
    Account nullplague;
    ICurrency dinero;
    IRepository repository;
    ISetBalanceUseCase setBalanceUseCase;
    Core core;
    UseCaseFactory useCaseFactory;

    @BeforeEach
    void setUp() {
        nullplague = new Account(UUID.randomUUID(), "nullplague");
        dinero= Currency.builder().setSingular("dinero").setPlural("dinero").build();

        repository = FactoryRepo.getDb();

        this.core = new Core(repository, 5, new CourierTest(),new LoggerTest());

       useCaseFactory = core.getUseCaseFactory();

        setBalanceUseCase = useCaseFactory.setBalance();
        useCaseFactory.createCurrency().execute(dinero.getSingular(), dinero.getPlural());
        useCaseFactory.createAccount().execute(nullplague.getUuid(), nullplague.getNickname());
    }

    @Test
    void setBalanceUseCaseTest() {
        Result<Void> result = setBalanceUseCase.execute(nullplague.getNickname(), "dinero", BigDecimal.valueOf(1));
        assertTrue(result.isSuccess());

        Result<Money> accountResult = useCaseFactory.getBalance().execute( nullplague.getNickname(), "dinero");
        assertEquals(BigDecimal.valueOf(1), accountResult.getValue( ).getAmount());
    }

    @Test
    void  setBalanceUseCaseInvalidAmountTest(){
        Result<Void> result = setBalanceUseCase.execute(nullplague.getNickname(), "dinero", BigDecimal.valueOf(-1));
        assertFalse(result.isSuccess());
        assertEquals(ErrorCode.INVALID_AMOUNT, result.getErrorCode());

    }

    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
