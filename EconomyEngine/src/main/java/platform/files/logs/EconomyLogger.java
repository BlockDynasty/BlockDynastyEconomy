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

package platform.files.logs;

import BlockDynasty.Economy.domain.services.log.Log;
import platform.files.IConfigurationEngine;
import services.Console;
import platform.files.Configuration;
import abstractions.platform.scheduler.IScheduler;
import services.configuration.IConfiguration;

public class EconomyLogger extends AbstractLogger{
    private boolean enable = true;

    public EconomyLogger(IConfigurationEngine configuration, IScheduler scheduler) {
        super(configuration, scheduler);
        this.enable = configuration.getBoolean("transaction_log");
    }

    @Override
    public void log(String message) {
        Console.debug(getName() + message);
        if (!enable) {
            return;
        }
        super.log(message);
    }

    @Override
    public String getName() {return "[ECONOMY-LOG]";}

    public static Log build(Configuration configuration, IScheduler scheduler) {return new EconomyLogger(configuration,scheduler);}
}
