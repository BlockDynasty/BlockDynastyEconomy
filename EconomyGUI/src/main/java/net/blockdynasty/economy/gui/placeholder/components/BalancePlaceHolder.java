package net.blockdynasty.economy.gui.placeholder.components;

import net.blockdynasty.economy.core.aplication.useCase.transaction.balance.GetBalanceUseCase;
import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.result.Result;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class BalancePlaceHolder {
    private final GetBalanceUseCase getBalanceUseCase;
    private final Formatter formatter;
    private static final Map<String, Locale> LOCALE_MAP = Map.ofEntries(
            Map.entry("english", Locale.ENGLISH), Map.entry("en", Locale.ENGLISH),
            Map.entry("french", Locale.FRENCH), Map.entry("fr", Locale.FRENCH),
            Map.entry("german", Locale.GERMAN), Map.entry("de", Locale.GERMAN),
            Map.entry("italian", Locale.ITALIAN), Map.entry("it", Locale.ITALIAN),
            Map.entry("japanese", Locale.JAPANESE), Map.entry("ja", Locale.JAPANESE),
            Map.entry("korean", Locale.KOREAN), Map.entry("ko", Locale.KOREAN),
            Map.entry("chinese", Locale.CHINESE), Map.entry("zh", Locale.CHINESE),
            Map.entry("simplified_chinese", Locale.SIMPLIFIED_CHINESE), Map.entry("zh_cn", Locale.SIMPLIFIED_CHINESE),
            Map.entry("traditional_chinese", Locale.TRADITIONAL_CHINESE), Map.entry("zh_tw", Locale.TRADITIONAL_CHINESE),
            Map.entry("france", Locale.FRANCE), Map.entry("germany", Locale.GERMANY),
            Map.entry("italy", Locale.ITALY), Map.entry("japan", Locale.JAPAN),
            Map.entry("korea", Locale.KOREA), Map.entry("uk", Locale.UK),
            Map.entry("canada", Locale.CANADA), Map.entry("canada_french", Locale.CANADA_FRENCH),
            Map.entry("us", Locale.US)
    );

    public BalancePlaceHolder(GetBalanceUseCase getBalanceUseCase) {
        this.getBalanceUseCase = getBalanceUseCase;
        this.formatter = new Formatter();
    }

    public String handle(UUID playerId, String placeholder) {
        return processInternal(placeholder,playerId);
    }

    private String processInternal(String placeholder, UUID playerId) {
        if (placeholder.equals("balance_default")) {
            Result<Money> balanceResult = getBalanceUseCase.execute(playerId);
            if (!balanceResult.isSuccess()) return balanceResult.getErrorMessage();
            return balanceResult.getValue().getAmount().toString();
        }

        if (placeholder.equals("balance_default_formatted")) {
            Result<Money> balanceResult = getBalanceUseCase.execute(playerId);
            if (!balanceResult.isSuccess()) return balanceResult.getErrorMessage();
            Money money = balanceResult.getValue();
            ICurrency currency = money.getCurrency();
            BigDecimal amount = money.getAmount();
            return currency.format(amount);
        }

        String[] parts = placeholder.split("_");
        if (parts.length < 2) return "Invalid placeholder format";

        String currencyName = parts[1];

        Result<Money> balanceResult = getBalanceUseCase.execute(playerId, currencyName);
        if (!balanceResult.isSuccess()) return balanceResult.getErrorMessage();
        Money money = balanceResult.getValue();

        //locale donde esta??
        return formatter.format(placeholder,parts,money);
    }
}