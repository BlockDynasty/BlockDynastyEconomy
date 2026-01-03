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

import BlockDynasty.Economy.domain.events.Event;
import BlockDynasty.Economy.domain.events.EventHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    private final Map<Class<? extends Event>, List<EventHandler<?>>> handlers = new HashMap<>();

    public <T extends Event> void subscribe(Class<T> eventType, EventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }

    public <T extends Event> void unsubscribe(Class<T> eventType, EventHandler<T> handler) {
        List<EventHandler<?>> eventHandlers = handlers.get(eventType);
        if (eventHandlers != null) {
            eventHandlers.remove(handler);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void emit(T event) {
        List<EventHandler<?>> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers != null) {
            for (EventHandler<?> handler : new ArrayList<>(eventHandlers)) {
                ((EventHandler<T>) handler).handle(event);
                if (event.isCancelled()) break; //cortar propagación
            }
        }
    }

    public void processNetworkEvent(String jsonEvent) {
        Event event = EventRegistry.deserializeEvent(jsonEvent);
        if (event != null){
            emit(event);
        }
    }
}