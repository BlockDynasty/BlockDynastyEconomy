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

package net.blockdynasty.providers.services;

import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/** ServiceProvider is a utility class for registering, retrieving, and unregistering service implementations.
 * It supports multiple implementations for the same service interface/class and allows selection based on predicates.
 * <h3>Rules:</h3>
 * <ul>
 *   <li>Multiple implementations can be registered for the same service interface/class.</li>
 *   <li>When retrieving a service, the first registered implementation is returned by default.</li>
 *   <li>A predicate can be provided to select a specific implementation when retrieving a service.</li>
 *   <li>Services are returned as dynamic proxies to prevent direct access to the original implementation.</li>
 *   <li>Unregistering a service removes only the exact instance provided.</li>
 * </ul>
 *
 * @version 1.0
 */
public class ServiceProvider {
    private static final Map<Class<?>, Queue<Object>> SERVICES = new ConcurrentHashMap<>();

    /** Registers a service implementation for the specified interface/class.
     *
     * @param clazz class/interface type of the service "ex: EconomyService.class"
     * @param service implementation of the service
     * @param <T> type of the service
     */
    public static <T> void register(Class<T> clazz, T service) {
        synchronized (SERVICES) {
            SERVICES.computeIfAbsent(clazz, k -> new LinkedList<>()).add(service);
        }
    }

    /** Retrieves a service implementation for the specified interface/class.
     *
     * @param clazz class/interface type of the service "ex: EconomyService.class"
     * @param <T> type of the service
     * @return implementation of the service or null if not found
     */
    public static <T> T get(Class<T> clazz) {
        synchronized (SERVICES) {
            Queue<Object> services = SERVICES.get(clazz);
            if (services == null || services.isEmpty()) {
                return null;
            }
            Object service= services.peek();
            if (service == null) {
                return null;
            }

            return createProxy(clazz, service);
        }
    }

    /** Retrieves a service implementation for the specified interface/class that matches the given selector.
     *
     * @param clazz class/interface type of the service "ex: EconomyService.class"
     * @param selector predicate to select the desired service implementation
     * @param <T> type of the service
     * @return implementation of the service or null if not found
     */
    public static <T> T get(Class<T> clazz, Predicate<T> selector) {
        synchronized (SERVICES) {
            Queue<Object> services = SERVICES.get(clazz);
            if (services == null || services.isEmpty()) {
                return null;
            }

            for (Object service : services) {
                @SuppressWarnings("unchecked")
                T typedService = (T) service;
                if (selector.test(typedService)) {
                    return createProxy(clazz, service);
                }
            }
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T createProxy(Class<T> clazz, Object service) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                (proxy, method, args) -> method.invoke(service, args)
        );
    }

    /** Unregisters a service implementation for the specified interface/class.
     * Only removes the exact instance provided as the service.
     * @param clazz class/interface type of the service "ex: EconomyService.class"
     * @param service original implementation of the service to be unregistered
     * @param <T> type of the service
     */
    public static <T> void unregister(Class<?> clazz, T service) {
        synchronized (SERVICES) {
            Queue<Object> services = SERVICES.get(clazz);
            if (services != null && !services.isEmpty()) {
                services.removeIf(s -> s == service);
                if (services.isEmpty()) {
                    SERVICES.remove(clazz);
                }
            }
        }
    }
}
