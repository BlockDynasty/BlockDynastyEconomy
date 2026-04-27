package net.blockdynasty.economy.gui.placeholder.components;

import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.libs.util.colors.ChatColor;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Formatter {
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

    public String format(String placeholder, String[] parts, Money money) {
        ICurrency currency = money.getCurrency();
        BigDecimal amountBD = money.getAmount();
        boolean isFormatted = placeholder.contains("_formatted");
        Locale locale = getLocaleFromPlaceholder(parts);
        boolean hasLocale = locale != null;
        locale = hasLocale ? locale : Locale.US;

        double amount = amountBD.doubleValue();
        String prefix = isFormatted ? ChatColor.formatColorToPlaceholder(currency.getColor()) : "";

        if (hasLocale) {
            NumberFormat formatter = NumberFormat.getNumberInstance(locale);
            int fractionDigits = currency.isDecimalSupported() ? 2 : 0;
            formatter.setMaximumFractionDigits(fractionDigits);
            formatter.setMinimumFractionDigits(fractionDigits);

            double amountToFormat = currency.isDecimalSupported() ? amount : Math.floor(amount);
            return prefix + formatter.format(amountToFormat);
        }

        if (isFormatted) {
            return prefix + currency.format(amountBD);
        }

        return currency.isDecimalSupported() ? String.valueOf(amount) : String.valueOf((int) Math.floor(amount));
    }

    private Locale getLocaleFromPlaceholder(String[] parts) {
        if (parts.length >= 3) {
            String possibleLocale = parts[parts.length - 1].toLowerCase();
            return LOCALE_MAP.get(possibleLocale);
        }
        return null;
    }
}