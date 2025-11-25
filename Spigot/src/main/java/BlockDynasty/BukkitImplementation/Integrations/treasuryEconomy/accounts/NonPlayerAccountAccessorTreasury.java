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

import api.IApi;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.accessor.NonPlayerAccountAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class NonPlayerAccountAccessorTreasury extends NonPlayerAccountAccessor {
    private IApi api;

    public NonPlayerAccountAccessorTreasury(@NotNull IApi api) {
        this.api = api;
    }

    @Override
    protected @NotNull CompletableFuture<NonPlayerAccount> getOrCreate(@NotNull NonPlayerAccountCreateContext context) {
        return CompletableFuture.completedFuture(new NonPlayerAccountTreasury(api,context.getName()));
    }
}
