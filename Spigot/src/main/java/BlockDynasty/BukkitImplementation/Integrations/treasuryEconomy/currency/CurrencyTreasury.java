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
package BlockDynasty.BukkitImplementation.Integrations.treasuryEconomy.currency;

import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CurrencyTreasury implements me.lokka30.treasury.api.economy.currency.Currency{
    BlockDynasty.Economy.domain.entities.currency.ICurrency currency;

    public CurrencyTreasury(BlockDynasty.Economy.domain.entities.currency.ICurrency currency ) {
        this.currency = currency;
    }

    @Override
    public @NotNull String getIdentifier() {
        return currency.getSingular();
    }

    @Override
    public @NotNull String getSymbol() {
        return currency.getSymbol();
    }

    @Override
    public char getDecimal(@Nullable Locale locale) {
        return 0;
    }

    @Override
    public @NotNull Map<Locale, Character> getLocaleDecimalMap() {
        return Map.of();
    }

    @Override
    public @NotNull String getDisplayName(@NotNull BigDecimal value, @Nullable Locale locale) {
        return currency.getSingular();
    }

    @Override
    public int getPrecision() {
        return currency.getDefaultBalance().precision();
    }

    @Override
    public boolean isPrimary() {
        return currency.isDefaultCurrency();
    }

    @Override
    public @NotNull BigDecimal getStartingBalance(@NotNull Account account) {
        return currency.getDefaultBalance();
    }

    @Override
    public @NotNull BigDecimal getConversionRate() {
        return BigDecimal.valueOf(currency.getExchangeRate());
    }

    @Override
    public @NotNull BigDecimal to(@NotNull Currency currency, @NotNull BigDecimal amount) {
        return Currency.super.to(currency, amount);
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> parse(@NotNull String formattedAmount, @Nullable Locale locale) {
        return CompletableFuture.completedFuture(BigDecimal.ZERO);
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale) {
        return currency.format(amount);
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale, int precision) {
        return currency.format(amount);
    }
}
