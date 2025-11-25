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

package BlockDynasty.adapters.integrations.spongeEconomyApi;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.service.economy.Currency;

import java.math.BigDecimal;

public class CurrencyAdapter implements Currency {
    private BlockDynasty.Economy.domain.entities.currency.ICurrency currency;

    public CurrencyAdapter(BlockDynasty.Economy.domain.entities.currency.ICurrency currency
    ) {
        this.currency = currency;
    }

    @Override
    public Component displayName() {
        return Component.text(currency.getSingular());
    }

    @Override
    public Component pluralDisplayName() {
        return Component.text(currency.getPlural());
    }

    @Override
    public Component symbol() {
        return Component.text(currency.getSymbol());
    }

    @Override
    public Component format(BigDecimal amount) {
        return Component.text(currency.format(amount));
    }

    @Override
    public Component format(BigDecimal amount, int numFractionDigits) {
        return Component.text(currency.format(amount));
    }

    @Override
    public int defaultFractionDigits() {
        return 0;
    }

    @Override
    public boolean isDefault() {
        return currency.isDefaultCurrency();
    }
}
