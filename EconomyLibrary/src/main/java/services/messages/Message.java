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

//used in gui system
//File for message processing
public class Message {
    private static IMessages messages;

    public static void addLang(IMessages lang){
        messages=lang;
    }

    public static String process(java.util.Map<String, String> placeholders,String messageKey) {
        return messages.format(placeholders,messageKey);
    }

    public static String process(String messageKey) {
        return messages.format(messageKey);
    }

    public static String[] processLines(java.util.Map<String, String> placeholders,String messageKey) {
        return messages.formatLines(placeholders,messageKey);
    }

    public static String[] processLines(String messageKey) {
        return messages.formatLines(messageKey);
    }
}
