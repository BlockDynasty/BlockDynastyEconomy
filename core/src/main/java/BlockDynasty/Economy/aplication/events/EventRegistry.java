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
package BlockDynasty.Economy.aplication.events;

import BlockDynasty.Economy.domain.events.SerializableEvent;
import BlockDynasty.Economy.domain.events.offersEvents.*;
import BlockDynasty.Economy.domain.events.transactionsEvents.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class EventRegistry {
    private static final Map<String, Class<? extends SerializableEvent>> deserializerEvent = new HashMap<>();
    private static final Map<String, Class<? extends OfferEvent>> deserializersOffersEvent = new HashMap<>();

    private static void registerEventTypeTransaction(Class<? extends SerializableEvent> eventClass) {
        deserializerEvent.put(eventClass.getSimpleName(), eventClass);
    }

    private static void registerEventTypeOffers(Class<? extends OfferEvent> eventClass) {
        deserializersOffersEvent.put(eventClass.getSimpleName(), eventClass);
    }

    public static SerializableEvent deserializeEvent(String jsonString) {
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            String eventType = jsonObject.get("eventType").getAsString();

            Class<? extends SerializableEvent> eventClass = deserializerEvent.get(eventType);
            if (eventClass == null) {
                return null;
            }

            JsonObject data = jsonObject.get("data").getAsJsonObject();
            Gson gson = new GsonBuilder().create();

            return gson.fromJson(data, eventClass);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing event: " + e.getMessage(), e);
        }
    }

    public static OfferEvent deserializeOfferEvent(String jsonString) {
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            String eventType = jsonObject.get("eventType").getAsString();

            Class<? extends OfferEvent> eventClass = deserializersOffersEvent.get(eventType);
            if (eventClass == null) {
                return null;
            }

            JsonObject data = jsonObject.get("data").getAsJsonObject();
            Gson gson = new GsonBuilder().create();

            return gson.fromJson(data, eventClass);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing event: " + e.getMessage(), e);
        }
    }


    static {
        //for recreate and emit Events locally
        registerEventTypeTransaction(DepositEvent.class);
        registerEventTypeTransaction(WithdrawEvent.class);
        registerEventTypeTransaction(TransferEvent.class);
        registerEventTypeTransaction(SetEvent.class);
        registerEventTypeTransaction(TradeEvent.class);
        registerEventTypeTransaction(OfferCreated.class);
        registerEventTypeTransaction(OfferCanceled.class);
        registerEventTypeTransaction(OfferExpired.class);
        registerEventTypeTransaction(OfferAccepted.class);

        //for recreate Specific events to handle another operations like methods
        registerEventTypeOffers(OfferCreated.class);
        registerEventTypeOffers(OfferCanceled.class);
        registerEventTypeOffers(OfferExpired.class);
        registerEventTypeOffers(OfferAccepted.class);
    }
}