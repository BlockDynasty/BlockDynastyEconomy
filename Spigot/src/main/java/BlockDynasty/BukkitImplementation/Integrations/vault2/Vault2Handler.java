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

package BlockDynasty.BukkitImplementation.Integrations.vault2;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.Integrations.vault.IVaultHandler;
import BlockDynasty.BukkitImplementation.utils.Console;
import api.IApi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class Vault2Handler implements IVaultHandler {
    private Vault2Hook economy = null;
    private final IApi api;
    private final BlockDynastyEconomy plugin;

    public Vault2Handler(BlockDynastyEconomy plugin, IApi api) {
        this.api = api;
        this.plugin = plugin;
    }

    @Override
    public void hook() {
        try {
            if (this.economy == null) {
                this.economy = new Vault2Hook(api);
            }

            ServicesManager sm = Bukkit.getServicesManager();
            sm.register(net.milkbowl.vault.economy.EconomyMultiCurrency.class, this.economy, plugin, ServicePriority.Highest);

            Console.log("Vault 2.0 link enabled.");
        } catch (Exception e) {
            Console.logError(e.getMessage());
        }
    }

    @Override
    public void unhook() {
        ServicesManager sm = Bukkit.getServicesManager();
        if(this.economy != null){
            sm.unregister(net.milkbowl.vault.economy.EconomyMultiCurrency.class, this.economy);
            this.economy = null;
        }
    }
}
