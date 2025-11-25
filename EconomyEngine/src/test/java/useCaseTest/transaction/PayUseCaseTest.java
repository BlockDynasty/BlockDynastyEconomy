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

import static org.junit.jupiter.api.Assertions.*;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.transactionsEvents.PayEvent;
import mockClass.CourierTest;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mockClass.LoggerTest;
import repositoryTest.FactoryRepo;

import java.math.BigDecimal;
import java.util.UUID;


public class PayUseCaseTest {
    Account nullplague;
    Account cris;
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
    EventManager eventManager;
    SearchAccountUseCase searchAccountUseCase;
    SearchCurrencyUseCase searchCurrencyUseCase;
    EditCurrencyUseCase editCurrencyUseCase;
    PayUseCase payUseCase;
    ICurrency coin;
    ICurrency dinero;
    ICurrency plata;

    @BeforeEach
    void setUp() {
        coin = Currency.builder().setSingular("Coin").setPlural("Coins").build();
        dinero = Currency.builder().setSingular("dinero").setPlural("dinero").build();
        plata = Currency.builder().setSingular("plata").setPlural("plata").build();

        nullplague = new Account(UUID.randomUUID(), "nullplague");
        cris = new Account(UUID.randomUUID(), "cris");

        nullplague.setMoney(plata, BigDecimal.valueOf(1000));
        nullplague.setMoney(coin, BigDecimal.valueOf(0));
        nullplague.setMoney(dinero, BigDecimal.valueOf(10000));
        cris.setMoney(plata, BigDecimal.valueOf(1000));
        cris.setMoney(coin, BigDecimal.valueOf(0));
        cris.setMoney(dinero, BigDecimal.valueOf(0));

        repository = FactoryRepo.getDb();

        repository.saveCurrency(plata);
        repository.saveCurrency(coin);
        repository.saveCurrency(dinero);
        repository.saveAccount(nullplague);
        repository.saveAccount(cris);


        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5 ,repository, currencyService);
        eventManager = new EventManager();

        accountService.addAccountToOnline(nullplague); //se conecto el player1
        accountService.addAccountToOnline(cris); //se conecto el player2

        searchAccountUseCase = new SearchAccountUseCase(accountService,repository);
        searchCurrencyUseCase = new SearchCurrencyUseCase(currencyService, repository);
        payUseCase = new PayUseCase(currencyService,accountService,repository,new CourierTest(),new LoggerTest(),eventManager);
        editCurrencyUseCase= new EditCurrencyUseCase(currencyService ,new CourierTest(),repository);

        eventManager.subscribe(PayEvent.class, event -> { System.out.println( event.getPayer().getNickname() + " realizo un pago a "+ event.getReceived().getNickname()+ " de un monto de " +event.getAmount()); } );
        eventManager.subscribe(PayEvent.class, event -> {System.out.println("OTRO EVENTO MAS, INYECTANDO COSAS AL CASO DE USO");});
    }

    @Test
    void payUseCseTest (){
        Result<Void> result = payUseCase.execute("nullplague","cris","dinero", BigDecimal.valueOf(10000));
        assertTrue(result.isSuccess());

        assertEquals(BigDecimal.valueOf(0).setScale(2), searchAccountUseCase.execute("nullplague").getValue().getMoney(dinero).getAmount());
        assertEquals(BigDecimal.valueOf(10000).setScale(2), searchAccountUseCase.execute("cris").getValue().getMoney(dinero).getAmount());
    }

    @Test
    void payUseCaseTestWithNullAccount(){
        Result<Void> result = payUseCase.execute("nullplague", "robert", "dinero", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones
    }

    @Test
    void payUseCaseTestWithNullCurrency(){
        Result<Void> result = payUseCase.execute("nullplague", "cris", "oro", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones
    }

    @Test
    void payUseCseTestWithoutFounds(){
        Result<Void> result = payUseCase.execute("nullplague", "cris", "dinero", BigDecimal.valueOf(10001));
        assertEquals(ErrorCode.INSUFFICIENT_FUNDS, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones
    }

    @Test
    void payUseCaseTestWithCurrencyNotPayable(){
        editCurrencyUseCase.togglePayable("plata");
        Result<Void> result = payUseCase.execute("nullplague", "cris", "plata", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.CURRENCY_NOT_PAYABLE, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones
    }

    @Test
    void setPayUseCaseTestAccountCanNotRecibe(){
        cris.setCanReceiveCurrency(false);
        Result<Void> result = payUseCase.execute("nullplague", "cris", "dinero", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.ACCOUNT_CAN_NOT_RECEIVE, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones

        assertEquals(BigDecimal.valueOf(10000), searchAccountUseCase.execute("nullplague").getValue().getMoney(dinero).getAmount());
        assertEquals(BigDecimal.valueOf(0), searchAccountUseCase.execute("cris").getValue().getMoney(dinero).getAmount());
    }


    //@AfterEach
    //void clearDb(){
      //  repository.clearAll();
    //}
}
