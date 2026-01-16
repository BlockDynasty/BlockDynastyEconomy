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
package BlockDynasty.BukkitImplementation.Integrations.treasuryEconomy.accounts;

import com.BlockDynasty.api.DynastyEconomy;
import me.lokka30.treasury.api.economy.account.accessor.AccountAccessor;
import me.lokka30.treasury.api.economy.account.accessor.NonPlayerAccountAccessor;
import me.lokka30.treasury.api.economy.account.accessor.PlayerAccountAccessor;
import org.jetbrains.annotations.NotNull;

public class AccountTreasury implements AccountAccessor {
    private static DynastyEconomy api;
    public AccountTreasury(@NotNull DynastyEconomy api) {
        AccountTreasury.api = api;
    }
    @Override
    public @NotNull PlayerAccountAccessor player() {
        return new PlayerAccountAccessorTreasury(api);
    }

    @Override
    public @NotNull NonPlayerAccountAccessor nonPlayer() {
        return new NonPlayerAccountAccessorTreasury(api);
    }
}
