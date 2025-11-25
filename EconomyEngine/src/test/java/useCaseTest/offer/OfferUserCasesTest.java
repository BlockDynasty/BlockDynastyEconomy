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

package useCaseTest.offer;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import mockClass.CourierTest;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.aplication.services.OfferService;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.TradeCurrenciesUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.IOfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mockClass.LoggerTest;
import repositoryTest.FactoryRepo;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OfferUserCasesTest {
    private CreateOfferUseCase  createOfferUseCase;
    private AcceptOfferUseCase acceptOfferUseCase;
    private CancelOfferUseCase cancelOfferUseCase;

    private SearchAccountUseCase searchAccountUseCase;
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private TradeCurrenciesUseCase tradeCurrenciesUseCase ;
    private IAccountService accountService;
    private ICurrencyService currencyService;
    private IRepository dataStore;
    private IOfferService offerService;

    private Account nullplague;
    private Account cris;

    private ICurrency dollar= Currency.builder().setSingular("dollar").setPlural("dollars").build() ;
    private ICurrency coin= Currency.builder().setSingular("coin").setPlural("coins").build();;

    @BeforeEach
    public void setup() {
         //cargar en cache alguna cuenta para las pruebas
        this.dataStore = FactoryRepo.getDb();
        this.currencyService = new CurrencyService(dataStore);  //cargar en cache alguna moneda para las pruebas
        this.accountService = new AccountService(5 ,dataStore, currencyService); //cargar en cache alguna cuenta para las pruebas

        currencyService.add(dollar);
        currencyService.add(coin);

        nullplague = new Account(UUID.randomUUID(), "nullplague");
        cris = new Account(UUID.randomUUID(), "cris");

        nullplague.setMoney(dollar, BigDecimal.valueOf(1000));
        nullplague.setMoney(coin, BigDecimal.valueOf(1000));
        cris.setMoney(dollar, BigDecimal.valueOf(1000));
        cris.setMoney(coin, BigDecimal.valueOf(1000));

        dataStore.saveCurrency(dollar);
        dataStore.saveCurrency(coin);
        dataStore.saveAccount(nullplague);
        dataStore.saveAccount(cris);
        accountService.addAccountToOnline(nullplague);
        accountService.addAccountToOnline(cris);


        searchAccountUseCase = new SearchAccountUseCase( accountService, dataStore);
        searchCurrencyUseCase = new SearchCurrencyUseCase( currencyService, dataStore);
        EventManager eventManager = new EventManager();
        tradeCurrenciesUseCase = new TradeCurrenciesUseCase(currencyService, accountService,dataStore,new CourierTest(),new LoggerTest(),eventManager);
        offerService = new OfferService(new CourierTest(),eventManager,1);

        createOfferUseCase = new CreateOfferUseCase( offerService,accountService,new CourierTest(),eventManager, currencyService, dataStore);
        acceptOfferUseCase = new AcceptOfferUseCase( offerService,new CourierTest(),eventManager, tradeCurrenciesUseCase);
        cancelOfferUseCase = new CancelOfferUseCase( offerService,new CourierTest(),eventManager);
    }

    @Test
    public void testCreateOffer() {
        createOfferUseCase.execute( nullplague.getUuid(), cris.getUuid(),dollar.getSingular(), BigDecimal.valueOf(100), coin.getSingular(), BigDecimal.valueOf(200));
        assertEquals(true, offerService.hasOfferTo (cris.getUuid()), "Offer should be created successfully");
        assertEquals(false, offerService.hasOfferTo( nullplague.getUuid()), "Offer should be created successfully for the sender");
    }

    @Test
    public void testAcceptOffer() {
        createOfferUseCase.execute( nullplague.getUuid(), cris.getUuid(), dollar.getSingular(), BigDecimal.valueOf(100), coin.getSingular(), BigDecimal.valueOf(200));
        assertEquals(true, offerService.hasOfferTo(cris.getUuid()), "Offer should exist before acceptance");


        Result<Void> result =acceptOfferUseCase.execute(cris.getUuid() , nullplague.getUuid());

        assertEquals(true, result.isSuccess(), result.getErrorMessage()+ " " + result.getErrorCode());
        assertEquals(false, offerService.hasOfferTo(cris.getUuid()), "Offer should be removed after acceptance");
        assertEquals(BigDecimal.valueOf(900).setScale(2), nullplague.getMoney(dollar).getAmount(), "Sender's dollar balance should be reduced");
        assertEquals(BigDecimal.valueOf(1200).setScale(2), nullplague.getMoney(coin).getAmount(), "Sender's coin balance should be increased");
        assertEquals(BigDecimal.valueOf(1100).setScale(2), cris.getMoney(dollar).getAmount(), "Receiver's dollar balance should be increased");
        assertEquals(BigDecimal.valueOf(800).setScale(2), cris.getMoney(coin).getAmount(), "Receiver's coin balance should be reduced");
    }

    @Test
    public void testCancelOffer() {
        createOfferUseCase.execute( nullplague.getUuid(), cris.getUuid(), dollar.getSingular(), BigDecimal.valueOf(100), coin.getSingular(), BigDecimal.valueOf(200));
        assertEquals(true, offerService.hasOfferTo(cris.getUuid()), "Offer should exist before cancellation");

        Result<Void> result = cancelOfferUseCase.execute(cris.getPlayer());

        assertEquals(true, result.isSuccess(), result.getErrorMessage()+ " " + result.getErrorCode());
        assertEquals(false, offerService.hasOfferTo(cris.getUuid()), "Offer should be removed after cancellation");
    }

    @Test
    public void testExpiredOffer() {
        createOfferUseCase.execute( nullplague.getUuid(), cris.getUuid(), dollar.getSingular(), BigDecimal.valueOf(100), coin.getSingular(), BigDecimal.valueOf(200));
        assertEquals(true, offerService.hasOfferTo(cris.getUuid()), "Offer should exist before expiration");


        // Simulate waiting for the offer to expire, 60 seconds is the expiration time
        try {
            Thread.sleep(1100); // Wait for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        assertEquals(false, offerService.hasOfferTo(cris.getUuid()), "Offer should be removed after expiration");
        assertEquals(null, offerService.getOfferBuyer(cris.getUuid()), "Offer should exist before expiration");
        assertEquals(null, offerService.getOfferSeller(nullplague.getUuid()), "Offer should exist before expiration");
    }

}
