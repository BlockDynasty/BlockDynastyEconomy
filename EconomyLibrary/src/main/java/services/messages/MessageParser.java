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

package services.messages;

import java.util.Map;

@Deprecated
public class MessageParser {
    public static String parse(String message, Map<String, String> variables) {
        if (message == null) return "";
        String parsed = message;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            parsed = parsed.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return parsed;
    }

    public static String colorize(String message) {
        // Implement color code translation if needed, or leave as-is for platform-specific
        return message;
    }
}