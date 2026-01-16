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

package BlockDynasty.BukkitImplementation.Integrations.vault;

import BlockDynasty.BukkitImplementation.utils.Console;
import com.BlockDynasty.api.DynastyEconomy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultHandler implements  IVaultHandler {
    private VaultHook economy = null;
    private final BlockDynasty.BukkitImplementation.BlockDynastyEconomy plugin;


    public VaultHandler(BlockDynasty.BukkitImplementation.BlockDynastyEconomy plugin) {
        this.plugin = plugin;
    }

    public void hook() {
        try {
            if (this.economy == null) {
                this.economy = new VaultHook();
            }

            ServicesManager sm = Bukkit.getServicesManager();
            sm.register(net.milkbowl.vault.economy.Economy.class, this.economy, plugin, ServicePriority.Highest);

            Console.log("Vault link enabled.");
        } catch (Exception e) {
            Console.logError(e.getMessage());
        }
    }

    public void unhook() {
        ServicesManager sm = Bukkit.getServicesManager();
        if(this.economy != null){
            sm.unregister(net.milkbowl.vault.economy.Economy.class, this.economy);
            this.economy = null;
        }
    }

}
