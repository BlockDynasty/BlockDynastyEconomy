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
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import mockClass.CourierTest;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.TransferFundsUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mockClass.LoggerTest;
import repositoryTest.FactoryRepo;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TransferUseCaseTest {
    Account nullplague;
    Account cris;
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
    SearchAccountUseCase searchAccountUseCase;
    SearchCurrencyUseCase searchCurrencyUseCase;
    TransferFundsUseCase transferFundsUseCase;
    ICurrency coin;
    ICurrency dinero;

    @BeforeEach
    void setUp() {
        coin = Currency.builder().setSingular("Coin").setPlural("Coins").build();
        dinero = Currency.builder().setSingular("dinero").setPlural("dinero").build();

        nullplague = new Account(UUID.randomUUID(), "nullplague");
        cris = new Account(UUID.randomUUID(), "cris");

        nullplague.setMoney(coin, BigDecimal.valueOf(0));
        nullplague.setMoney(dinero, BigDecimal.valueOf(10000));
        cris.setMoney(coin, BigDecimal.valueOf(0));
        cris.setMoney(dinero, BigDecimal.valueOf(0));

        repository = FactoryRepo.getDb();

        repository.saveCurrency(coin);
        repository.saveCurrency(dinero);
        repository.saveAccount(nullplague);
        repository.saveAccount(cris);

        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5 ,repository, currencyService);

        //accountManager.addAccountToCache(account1); //se conecto el player1
        //accountManager.addAccountToCache(account2); //se conecto el player2

        searchAccountUseCase = new SearchAccountUseCase(accountService,repository);
        searchCurrencyUseCase = new SearchCurrencyUseCase(currencyService, repository);
        transferFundsUseCase = new TransferFundsUseCase(currencyService,accountService,repository,new CourierTest(),new LoggerTest(),new EventManager());
    }

    @Test
    void TransferTest (){
    /*try {

        }catch (AccountNotFoundException e){
            fail("Account not found");
        }catch (InsufficientFundsException e){
            fail("Insufficient funds");
        }catch (CurrencyNotFoundException e){
            fail("Currency not found");
        }catch (AccountCanNotReciveException e){
            fail("Account can not recive");
        }catch (CurrencyNotPayableException e){
            fail("Currency not payable");
        }catch (DecimalNotSupportedException e){
            fail("Decimal not supported");
        }*/
        Result<Void> result = transferFundsUseCase.execute("nullplague","cris","dinero", BigDecimal.valueOf(10000));

        assertEquals(BigDecimal.valueOf(0).setScale(2), searchAccountUseCase.execute("nullplague").getValue().getMoney("dinero").getAmount().setScale(2));
        assertEquals(BigDecimal.valueOf(10000).setScale(2), searchAccountUseCase.execute("cris").getValue().getMoney("dinero").getAmount().setScale(2));
    }

    @Test
    void TransferTestWithNegativeAmount (){
        Result<Void> result = transferFundsUseCase.execute("nullplague","cris","dinero", BigDecimal.valueOf(-1));
        assertEquals(result.getErrorCode(), ErrorCode.INVALID_AMOUNT);
    }

    @Test
    void TransferTestWithZeroAmount (){
        Result<Void> result = transferFundsUseCase.execute("nullplague","cris","dinero", BigDecimal.valueOf(0));
        assertEquals(result.getErrorCode(), ErrorCode.INVALID_AMOUNT);
    }
    @Test
    void TransferTestWithInsufficientFunds (){
        Result<Void> result = transferFundsUseCase.execute("nullplague","cris","dinero", BigDecimal.valueOf(100000));
        assertEquals(result.getErrorCode(),ErrorCode.INSUFFICIENT_FUNDS);
    }

    @Test
    void TransferTestWithNullAccount (){
        Result<Void> result = transferFundsUseCase.execute("nullplague","tom","dinero", BigDecimal.valueOf(10000));
        assertEquals(result.getErrorCode(),ErrorCode.ACCOUNT_NOT_FOUND);
    }

    @Test
    void TransferTestWithNullCurrency (){
        Result<Void> result = transferFundsUseCase.execute("nullplague","cris","plata", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND,result.getErrorCode());
    }

    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
