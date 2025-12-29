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

package BlockDynasty.Economy.aplication.services;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.events.EventRegistry;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.events.offersEvents.*;
import BlockDynasty.Economy.domain.services.IOfferService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.courier.Message;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class OfferService implements IOfferService {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Map<Offer, ScheduledFuture<?>> ofertasPendientes = new ConcurrentHashMap<>();
    private final Courier courier;
    private final EventManager eventManager;
    private int delay = 60; // Default delay in seconds

    public OfferService(Courier courier,EventManager eventManager, int delay) {
        this.courier = courier;
        this.eventManager = eventManager;
        if (delay > 0) {
            this.delay = delay;
        }
    }

    public OfferService(Courier courier,EventManager eventManager) {
        this.courier = courier;
        this.eventManager = eventManager;
    }

    public Offer createOffer(Player playerSender, Player playerReceiver, BigDecimal amountCurrencyValue, BigDecimal amountCurrencyOffer, ICurrency currencyValue, ICurrency currencyOffer) {
        Offer offer = new Offer(playerSender, playerReceiver, amountCurrencyValue, amountCurrencyOffer, currencyValue, currencyOffer);
        addOffer(offer);
        return offer;
    }

    public void addOffer(Offer offer) {
        ScheduledFuture<?> oldTask = this.ofertasPendientes.get(offer);
        if (oldTask != null) {
            oldTask.cancel(false);
            this.ofertasPendientes.remove(offer);
        }

        ScheduledFuture<?> expirationTask = scheduler.schedule(() -> {
            expireOfferLocally(offer, true); // Added parameter to indicate local expiration
        }, this.delay, TimeUnit.SECONDS);

        ofertasPendientes.put(offer, expirationTask);
    }

    // New method for local expirations with network broadcast
    public void expireOfferLocally(Offer offer, boolean broadcastNetwork) {
        this.ofertasPendientes.remove(offer);

        if (broadcastNetwork) {
            this.eventManager.emit(new OfferExpired(offer));

            courier.sendUpdateMessage(Message.builder()
                    .setType(Message.Type.EVENT)
                    .setData(new OfferExpired(offer).toJson())
                    .setTarget(offer.getComprador().getUuid())
                    .build());

            courier.sendUpdateMessage(Message.builder()
                    .setType(Message.Type.EVENT)
                    .setData(new OfferExpired(offer).toJson())
                    .setTarget(offer.getVendedor().getUuid())
                    .build());
        }
    }

    // Update the original method to use the new one
    public void expireOffer(Offer offer) {
        expireOfferLocally(offer, true);
    }

    // Update the event-based expiration to not broadcast again
    public void expireOfferFromEvent(Offer offer) {
        ScheduledFuture<?> oldTask = this.ofertasPendientes.get(offer);
        if (oldTask != null) {
            oldTask.cancel(false);
            expireOfferLocally(offer, false); // Don't broadcast network events
        }
    }

    //can be canceled by either the buyer or the seller
    public boolean cancelOffer(UUID initiator) {
        Map.Entry<Offer, ScheduledFuture<?>> entryToRemove = this.ofertasPendientes.entrySet().stream()
                .filter(entry -> {
                    Offer offer = entry.getKey();
                    return offer.getVendedor().getUuid().equals(initiator) || offer.getComprador().getUuid().equals(initiator);
                }).findFirst().orElse(null);
        if (entryToRemove != null) {
            Offer offer = entryToRemove.getKey();
            entryToRemove.getValue().cancel(false);
            this.ofertasPendientes.remove(offer);
            return true;
        }
        return false;
    }

    public boolean acceptOffer(UUID playerReceiver, UUID playerSender) {
        Offer offerToAccept = getOffer(playerSender, playerReceiver);

        if (offerToAccept != null) {
            ScheduledFuture<?> task = this.ofertasPendientes.get(offerToAccept);
            if (task != null) {
                task.cancel(false);
                this.ofertasPendientes.remove(offerToAccept);
                return true;
            }
        }
        return false;
    }

    public boolean hasOfferTo(UUID player) {
        for (Offer offer : this.ofertasPendientes.keySet()) {
            if (offer.getComprador().getUuid().equals(player)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void processNetworkEvent(String jsonEvent) {
        OfferEvent offerEvent=EventRegistry.deserializeOfferEvent(jsonEvent);
        if (offerEvent != null){
            offerEvent.syncOffer(this);
            //eventManager.emit(offerEvent);
        }
    }

    public Offer getOfferSeller(UUID playerId) {
        return this.ofertasPendientes.keySet().stream()
                .filter(offer ->offer.getVendedor().getUuid().equals(playerId))
                .findFirst()
                .orElse(null);
    }

    public Offer getOfferBuyer(UUID playerId) {
        return this.ofertasPendientes.keySet().stream()
                .filter(offer -> offer.getComprador().getUuid().equals(playerId))
                .findFirst()
                .orElse(null);
    }

    public Offer getOffer(UUID player1Id, UUID player2Id) {
        return this.ofertasPendientes.keySet().stream()
                .filter(offer -> (offer.getVendedor().getUuid().equals(player1Id) && offer.getComprador().getUuid().equals(player2Id)) ||
                        (offer.getVendedor().getUuid().equals(player2Id) && offer.getComprador().getUuid().equals(player1Id)))
                .findFirst()
                .orElse(null);
    }

    public List<Offer> getOffersSeller(UUID playerId) {
        return this.ofertasPendientes.keySet().stream()
                .filter(offer -> offer.getVendedor().getUuid().equals(playerId))
                .collect(Collectors.toList());
    }

    public List<Offer> getOffersBuyer(UUID playerId) {
        return this.ofertasPendientes.keySet().stream()
                .filter(offer -> offer.getComprador().getUuid().equals(playerId))
                .collect(Collectors.toList());
    }
}
