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

package services;

import abstractions.platform.IConsole;
import services.configuration.IConfiguration;

public class Console {
    private static boolean debug ;
    private static IConsole console;

    public static void setConsole(IConsole console, IConfiguration configuration) {
       debug = configuration.getBoolean("debug");
       Console.console = console;
    }

    public static void debug(String message) {
        if(debug)console.debug(message);
    }

    public static void log(String message) {
        console.log(message);
    }

    public static void logError(String message) {
        console.logError(message);
    }
}
