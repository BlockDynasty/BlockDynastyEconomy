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
import me.lokka30.treasury.api.common.Cause;
import me.lokka30.treasury.api.common.service.Service;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionImportance;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/* This class exists solely for the purpose of testing the Treasure API */
public class TestTreasureApi implements CommandExecutor {
    public EconomyProvider getPrimaryEcoProvider() {
        final Optional<Service<EconomyProvider>> serviceOpt = ServiceRegistry.INSTANCE.serviceFor(EconomyProvider.class);
        if(serviceOpt.isEmpty()) {
            throw new RuntimeException("Expected an Economy Provider to be registered through Treasury; found none");
        }
        return serviceOpt.get().get();
    }

    public static void testDeposit(Player player){
        EconomyProvider provider = null;
        try {
            provider = new TestTreasureApi().getPrimaryEcoProvider();
        }
        catch (RuntimeException e) {
            Console.log("No Economy Provider found: " + e.getMessage());
        }
        if (provider != null) {
            BigDecimal amount = BigDecimal.valueOf(1000);
            Currency currency = provider.getPrimaryCurrency();

            provider.accountAccessor()
                    .player()
                    .withUniqueId(player.getUniqueId())
                    .get()
                    .thenCompose(account -> account.depositBalance(amount, Cause.player(player.getUniqueId()), currency))
                    .thenRun(() -> player.sendMessage(ChatColor.AQUA + "Welcome to the server! You have been awarded $" + amount + " for your first join."))
                    .exceptionally(ex -> {
                        player.sendMessage(ChatColor.RED + "An error occurred while trying to award your welcome gift: " + ex.getMessage());
                        return null;
                    });

            Currency currency2 = provider.getCurrencies().stream().filter( currency1 -> currency1.getIdentifier().equals("Dinero")).findFirst().orElse(null);
            if (currency2 != null) {
                provider.accountAccessor()
                        .player()
                        .withUniqueId(player.getUniqueId())
                        .get()
                        .thenCompose(account -> account.depositBalance(amount, Cause.player(player.getUniqueId()), currency2))
                        .thenRun(() -> player.sendMessage(ChatColor.AQUA + "Welcome to the server! You have been awarded $" + amount + " for your first join."))
                        .exceptionally(ex -> {
                            player.sendMessage(ChatColor.RED + "An error occurred while trying to award your welcome gift: " + ex.getMessage());
                            return null;
                        });
            }

        }
    }

    public static void testWithdraw(Player player){
        EconomyProvider provider = null;
        try {
            provider = new TestTreasureApi().getPrimaryEcoProvider();
        }
        catch (RuntimeException e) {
            Console.log("No Economy Provider found: " + e.getMessage());
        }
        if (provider != null) {
            BigDecimal amount = BigDecimal.valueOf(100);
            Currency currency = provider.getPrimaryCurrency();

            provider.accountAccessor()
                    .player()
                    .withUniqueId(player.getUniqueId())
                    .get()
                    .thenCompose(account -> account.withdrawBalance(amount, Cause.player(player.getUniqueId()), currency))
                    .thenRun(() -> player.sendMessage(ChatColor.AQUA + "Welcome to the server! You have been awarded $" + amount + " for your first join."))
                    .exceptionally(ex -> {
                        player.sendMessage(ChatColor.RED + "An error occurred while trying to award your welcome gift: " + ex.getMessage());
                        return null;
                    });

            Currency currency2 = provider.findCurrency("Dinero").orElse(null);
            if (currency2 != null) {
                provider.accountAccessor()
                        .player()
                        .withUniqueId(player.getUniqueId())
                        .get()
                        .thenCompose(account -> account.withdrawBalance(amount, Cause.player(player.getUniqueId()), currency2))
                        .thenRun(() -> player.sendMessage(ChatColor.AQUA + "Welcome to the server! You have been awarded $" + amount + " for your first join."))
                        .exceptionally(ex -> {
                            player.sendMessage(ChatColor.RED + "An error occurred while trying to award your welcome gift: " + ex.getMessage());
                            return null;
                        });


               EconomyTransaction eco= EconomyTransaction.newBuilder()
                       .withAmount(BigDecimal.valueOf(0))
                       .withCurrency(currency2)
                       .withType(EconomyTransactionType.SET)
                       .withCause(Cause.player(player.getUniqueId()))
                       .withImportance(EconomyTransactionImportance.HIGH)
                       .build();
                provider.accountAccessor()
                        .player()
                        .withUniqueId(player.getUniqueId())
                        .get()
                        .thenCompose(account -> account.doTransaction(eco)
                        .thenRun(() -> player.sendMessage(ChatColor.AQUA + "Welcome to the server! You have been awarded $" + amount + " for your first join."))
                        .exceptionally(ex -> {
                            player.sendMessage(ChatColor.RED + "An error occurred while trying to award your welcome gift: " + ex.getMessage());
                            return null;
                        }));
            }
        }
    }

    public void testCreateCurrency(){
        EconomyProvider provider = null;
        try {
            provider = new TestTreasureApi().getPrimaryEcoProvider();
        }
        catch (RuntimeException e) {
            Console.log("No Economy Provider found: " + e.getMessage());
        }
        if(provider != null) {
            Currency newCurrency = new Currency() {
                @Override
                public @NotNull String getIdentifier() {
                    return "Dinero";
                }

                @Override
                public @NotNull String getSymbol() {
                    return "";
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
                    return "Dinero";
                }

                @Override
                public int getPrecision() {
                    return 0;
                }

                @Override
                public boolean isPrimary() {
                    return false;
                }

                @Override
                public @NotNull BigDecimal getStartingBalance(@NotNull Account account) {
                    return BigDecimal.ZERO;
                }

                @Override
                public @NotNull BigDecimal getConversionRate() {
                    return BigDecimal.ONE;
                }

                @Override
                public @NotNull CompletableFuture<BigDecimal> parse(@NotNull String formattedAmount, @Nullable Locale locale) {
                    return null;
                }

                @Override
                public @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale) {
                    return "";
                }

                @Override
                public @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale, int precision) {
                    return "";
                }
            };
            provider.registerCurrency(newCurrency);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            testDeposit(player);
            testWithdraw(player);
            testCreateCurrency();
            return true;
        }
        return false;
    }
}
