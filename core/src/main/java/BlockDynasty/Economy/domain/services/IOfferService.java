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

package BlockDynasty.Economy.domain.services;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.entities.offers.Offer;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IOfferService {
    Offer createOffer(Player playerSender, Player playerReciber, BigDecimal amountCurrencyValue, BigDecimal amountCurrencyOffer, ICurrency currencyValue, ICurrency currencyOffer );
    Offer getOfferBuyer(UUID playerId);
    Offer getOfferSeller(UUID playerId);
    Offer getOffer(UUID player1Id, UUID player2Id);
    List<Offer> getOffersBuyer(UUID playerId);
    List<Offer> getOffersSeller(UUID playerId);
    boolean cancelOffer(UUID player);
    boolean acceptOffer(UUID playerReceiver, UUID playerSender);
    boolean hasOfferTo(UUID player);
    void expireOffer(Offer offer);
    void processNetworkEvent(String jsonEvent);
}
