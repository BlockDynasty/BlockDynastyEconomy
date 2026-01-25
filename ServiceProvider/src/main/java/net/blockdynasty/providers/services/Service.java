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

/**
 * Generic service interface for services with an ID.
 * Utility interface to represent a service with an identifier to use in predicates.
 *
 * @param <T> the type of the service ID
 */
public interface Service<T> {

    /**
     * Gets the ID of the service.
     *
     * @return the service ID
     */
    T getId();
}
