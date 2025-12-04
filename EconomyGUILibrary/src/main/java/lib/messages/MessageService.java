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

package lib.messages;

import java.util.Map;

@Deprecated
public class MessageService {
    private static MessageProvider provider = new DefaultProvider();

    public static void setProvider(MessageProvider customProvider) {
        provider = customProvider;
    }

    public static String get(String key) {
        return provider.getMessage(key);
    }

    public static String getMessage(String key, Map<String, String> variables) {
        String raw = get(key);
        return MessageParser.colorize(MessageParser.parse(raw, variables));
    }

    public static String getMessage(String key) {
        return MessageParser.colorize(get(key));
    }
}