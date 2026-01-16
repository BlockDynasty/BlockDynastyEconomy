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

package spongeV13.adapters.platformAdapter.NBTApi;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.value.Value;

public final class CustomKeys {
    public static final Key<Value<String>> NAME_CURRENCY = Key.from(ResourceKey.sponge("name_currency"), String.class);
    public static final Key<Value<String>> UUID_CURRENCY = Key.from(ResourceKey.sponge("uuid_currency"), String.class);
    public static final Key<Value<String>> VALUE = Key.from(ResourceKey.sponge("value"), String.class);
}