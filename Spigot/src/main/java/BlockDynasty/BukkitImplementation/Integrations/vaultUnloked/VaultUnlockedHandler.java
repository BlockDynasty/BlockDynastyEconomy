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

package BlockDynasty.BukkitImplementation.Integrations.vaultUnloked;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.Integrations.vault.IVaultHandler;
import BlockDynasty.BukkitImplementation.Integrations.vault.VaultHook;
import BlockDynasty.BukkitImplementation.utils.Console;
import api.IApi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultUnlockedHandler implements IVaultHandler {
    private VaultUnlockedHook vaultUnlockedHook=null;
    private final BlockDynastyEconomy plugin;
    private final IApi api;

    public VaultUnlockedHandler(BlockDynastyEconomy plugin, IApi api) {
        this.plugin = plugin;
        this.api = api;
    }

    public void hook() {
        try {
            if (this.vaultUnlockedHook == null) {
                this.vaultUnlockedHook = new VaultUnlockedHook(api);
            }

            ServicesManager sm = Bukkit.getServicesManager();
            sm.register(net.milkbowl.vault.economy.Economy.class, new VaultHook(api), plugin, ServicePriority.Highest);
            sm.register(net.milkbowl.vault2.economy.Economy.class, this.vaultUnlockedHook, plugin, ServicePriority.Highest);

            Console.log("Vault Unlocked detect, link enabled.");
        } catch (Exception e) {
            Console.logError(e.getMessage());
        }
    }

    public void unhook() {
        ServicesManager sm = Bukkit.getServicesManager();
        if (this.vaultUnlockedHook != null) {
            sm.unregister(net.milkbowl.vault2.economy.Economy.class, this.vaultUnlockedHook);
            this.vaultUnlockedHook = null;
        }
    }
}
