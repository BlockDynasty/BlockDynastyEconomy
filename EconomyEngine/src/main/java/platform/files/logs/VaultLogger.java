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

public class VaultLogger extends  AbstractLogger{
    private boolean enable = false;

    public VaultLogger(IConfigurationEngine configuration, IScheduler scheduler) {
        super(configuration, scheduler);
        this.enable = configuration.getBoolean("transaction_log_vault");
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
    public String getName() {
        return "[VAULT-LOG] ";
    }

    public static Log build(IConfigurationEngine configuration, IScheduler scheduler) {return new VaultLogger(configuration,scheduler);}
}
