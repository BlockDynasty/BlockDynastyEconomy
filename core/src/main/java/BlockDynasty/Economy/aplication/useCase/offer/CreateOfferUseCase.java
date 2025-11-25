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

package BlockDynasty.Economy.aplication.useCase.offer;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.events.offersEvents.OfferCreated;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.IOfferService;
import BlockDynasty.Economy.domain.services.courier.Courier;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateOfferUseCase {
    private final IOfferService offerService;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    private final Courier courier;
    private final EventManager eventManager;

    public CreateOfferUseCase(IOfferService offerService, IAccountService accountService,
                              Courier courier, EventManager eventManager, ICurrencyService currencyService, IRepository repository) {
        this.offerService = offerService;
        this.searchCurrencyUseCase = new SearchCurrencyUseCase( currencyService,repository);
        this.getAccountByUUIDUseCase = new GetAccountByUUIDUseCase( accountService);
        this.courier = courier;
        this.eventManager = eventManager;
    }

    public Result<Void> execute (UUID playerSender, UUID playerReciber, String currencyNameValue, BigDecimal amountCurrencyValue, String currencyNameOffer, BigDecimal amountCurrencyOffer) {
        if (playerSender.equals(playerReciber)) {
            return Result.failure( "Sender and receiver cannot be the same player", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        Result<Account> accountSenderResult = getAccountByUUIDUseCase.execute(playerSender);
        if (!accountSenderResult.isSuccess()) {
            return Result.failure(accountSenderResult.getErrorMessage(), accountSenderResult.getErrorCode());
        }

        Result<Account> accountReciberResult = getAccountByUUIDUseCase.execute(playerReciber);
        if (!accountReciberResult.isSuccess()) {
            return Result.failure(accountReciberResult.getErrorMessage(), accountReciberResult.getErrorCode());
        }

        if(accountSenderResult.getValue().isBlocked()){
            return Result.failure("Sender account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }

        if(accountReciberResult.getValue().isBlocked()){
            return Result.failure("Receiver account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }

        Result<ICurrency> currencyValueResult = this.searchCurrencyUseCase.getCurrency(currencyNameValue);
        if (!currencyValueResult.isSuccess()) {
            return Result.failure(currencyValueResult.getErrorMessage(), currencyValueResult.getErrorCode());
        }
        Result<ICurrency> currencyOfferResult = this.searchCurrencyUseCase.getCurrency(currencyNameOffer);
        if (!currencyOfferResult.isSuccess()) {
            return Result.failure(currencyOfferResult.getErrorMessage(), currencyOfferResult.getErrorCode());
        }

        //if(this.offerService.hasOfferTo(playerReciber)){ //todo test, permitir ofertas multiples de diferentes jugadores
        //    return Result.failure("There is already an offer pending", ErrorCode.OFFER_ALREADY_EXISTS);
        //}

        if (currencyValueResult.getValue().getUuid().equals(currencyOfferResult.getValue().getUuid())) {
           return Result.failure("The currencies must be different", ErrorCode.CURRENCY_MUST_BE_DIFFERENT);
        }

        //si el monto es menor o igual a 0
        if (amountCurrencyValue.compareTo(BigDecimal.ZERO) <= 0 || amountCurrencyOffer.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }

    // si la moneda soporta decimales y el monto no es entero
        if (!currencyOfferResult.getValue().isValidAmount(amountCurrencyOffer)) {
            return Result.failure("Amount must be an integer", ErrorCode.INVALID_AMOUNT);
        }

    // si la moneda soporta decimales y el monto no es entero
        if (!currencyValueResult.getValue().isValidAmount(amountCurrencyValue)) {
           return Result.failure("Amount must be an integer", ErrorCode.INVALID_AMOUNT);
        }

        if (!accountSenderResult.getValue().hasEnough(currencyValueResult.getValue(), amountCurrencyValue)) {
            return Result.failure("Insufficient funds", ErrorCode.INSUFFICIENT_FUNDS);
        }

        Offer offer =offerService.createOffer(accountSenderResult.getValue().getPlayer(), accountReciberResult.getValue().getPlayer(), amountCurrencyValue, amountCurrencyOffer, currencyValueResult.getValue(), currencyOfferResult.getValue());
        eventManager.emit(new OfferCreated(offer));
        courier.sendUpdateMessage("event",new OfferCreated(offer).toJson(),playerReciber.toString());
        return Result.success();
    }
}
