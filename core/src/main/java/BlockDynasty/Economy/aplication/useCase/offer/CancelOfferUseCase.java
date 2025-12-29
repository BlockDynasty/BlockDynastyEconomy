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
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.events.offersEvents.OfferAccepted;
import BlockDynasty.Economy.domain.events.offersEvents.OfferCanceled;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IOfferService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.courier.Message;

public class CancelOfferUseCase {
    private final IOfferService offerService;
    private final Courier courier;
    private final EventManager eventManager;

    public CancelOfferUseCase(IOfferService offerService, Courier courier, EventManager eventmanager) {
        this.offerService = offerService;
        this.courier = courier;
        this.eventManager = eventmanager;
    }

    public Result<Void> execute (Player playerCancel){
        Offer offer = offerService.getOfferBuyer(playerCancel.getUuid());
        if (offer == null){
            offer= offerService.getOfferSeller(playerCancel.getUuid());
        }
        boolean deleted = offerService.cancelOffer(playerCancel.getUuid());
        if (!deleted ) {
            return Result.failure("Offer not found for cancer offer", ErrorCode.OFFER_NOT_FOUND);
        }
        eventManager.emit(new OfferCanceled(offer));

        courier.sendUpdateMessage(Message.builder()
                .setType(Message.Type.EVENT)
                .setData(new OfferCanceled(offer).toJson())
                .setTarget(offer.getVendedor().getUuid())
                .build());

        courier.sendUpdateMessage(Message.builder()
                .setType(Message.Type.EVENT)
                .setData(new OfferCanceled(offer).toJson())
                .setTarget(offer.getComprador().getUuid())
                .build());
        return Result.success();
    }
}
