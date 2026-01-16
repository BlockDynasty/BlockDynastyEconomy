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

package BlockDynasty.BukkitImplementation.Integrations.treasuryEconomy;

import BlockDynasty.BukkitImplementation.utils.Console;
import me.lokka30.treasury.api.common.service.ServicePriority;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import org.bukkit.Bukkit;

public class TreasuryHook {
    public static void register(){
        if(Bukkit.getPluginManager().isPluginEnabled("Treasury")) {
            EconomyProvider econProvider = new economyHook();
            ServiceRegistry.INSTANCE.registerService(
                    EconomyProvider.class,
                    econProvider,
                    "BlockDynastyEconomy",
                    ServicePriority.NORMAL
            );
            Console.log("Treasury economy plugin detected, registering BlockDynastyEconomy as economy provider...");
        }
    }
}
