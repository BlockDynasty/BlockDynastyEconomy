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

package BlockDynasty.Economy.domain.entities.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Currency implements ICurrency{
    private String uuid;
    private String singular;
    private String plural;
    private String symbol ;
    private String texture;
    private String color ;
    private boolean decimalSupported ;
    private boolean transferable;
    private boolean defaultCurrency ;
    private BigDecimal defaultBalance ;
    private double exchangeRate ;
    private List<Currency> interchangeableWith;

    public Currency(UUID uuid, String singular, String plural) {
        this.defaultBalance = BigDecimal.ZERO;
        this.exchangeRate = 1;
        this.color = "WHITE";
        this.symbol = "";
        this.texture = "";
        this.decimalSupported = true;
        this.transferable = true;
        this.defaultCurrency = false;
        this.uuid = uuid.toString();
        this.singular = singular;
        this.plural = plural;
        this.interchangeableWith = new ArrayList<>();
    }

    public Currency() {
        this.defaultBalance = BigDecimal.ZERO;
        this.exchangeRate = 1;
        this.color = "WHITE";
        this.symbol = "";
        this.texture = "";
        this.decimalSupported = true;
        this.transferable = true;
        this.defaultCurrency = false;
        this.interchangeableWith = new ArrayList<>();
    }

    public Currency(UUID uuid, String singular, String plural, String symbol, String texture,String color, boolean decimalSupported, boolean transferable, boolean defaultCurrency, BigDecimal defaultBalance, double exchangeRate,List<Currency> interchangeableWith) {
        this.uuid = uuid.toString();
        this.singular = singular;
        this.plural = plural;
        this.symbol = symbol;
        this.texture = texture;
        this.color = color;
        this.decimalSupported = decimalSupported;
        this.transferable = transferable;
        this.defaultCurrency = defaultCurrency;
        this.defaultBalance = defaultBalance;
        this.exchangeRate = exchangeRate;
        this.interchangeableWith = interchangeableWith;
    }

    public Currency(Currency currency) {
        this.uuid = currency.uuid;
        this.singular = currency.singular;
        this.plural = currency.plural;
        this.symbol = currency.symbol;
        this.texture = currency.texture;
        this.color = currency.color;
        this.decimalSupported = currency.decimalSupported;
        this.transferable = currency.transferable;
        this.defaultCurrency = currency.defaultCurrency;
        this.defaultBalance = currency.defaultBalance;
        this.exchangeRate = currency.exchangeRate;
        this.interchangeableWith = currency.getInterchangeableWith();
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid.toString();
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public UUID getUuid() {
        return UUID.fromString(this.uuid);
    }

    public String getSingular() {
        return this.singular;
    }

    public String getPlural() {
        return this.plural;
    }

    public BigDecimal getDefaultBalance() {

        return this.defaultBalance;
    }

    public String format(BigDecimal amount) {
        StringBuilder amt = new StringBuilder();
        if (this.isDecimalSupported()) {
            amount = amount.setScale(2, RoundingMode.HALF_UP); // Limitar a 2 decimales para mostrar
            amt.append(NumberFormat.getInstance().format(amount));
        } else {
            String s = amount.setScale(0, RoundingMode.HALF_UP).toPlainString();
            amt.append(NumberFormat.getInstance().format(Double.parseDouble(s)));
        }
        amt.append(" ");

        if (!this.getSymbol().isEmpty()) {
            amt.append(this.getSymbol()); //si tiene simbolo usarlo
        }else { //sino usar nombre
            if (amount.compareTo(BigDecimal.ONE) != 0) {
                amt.append(this.getPlural().replace("_", " ")); //si es mayor a 1 usar nombre plural
            } else {
                amt.append(this.getSingular().replace("_", " ")); //si es igual a 1 usar nombre singular
            }
        }
        return amt.toString();
    }

    public boolean isValidAmount(BigDecimal amount) {
        if (!this.isDecimalSupported() && amount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
            return false; // Invalid if decimal not supported and has decimal part
        }
        return true;
    }

    public boolean isDefaultCurrency() {
        return this.defaultCurrency;
    }

    public void setStartBalance(BigDecimal startBalance){
        this.defaultBalance =startBalance;
    }

    public void setDefaultBalance(BigDecimal defaultBalance) {
        this.defaultBalance = defaultBalance;
    }

    public void setDefaultCurrency(boolean defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public boolean isTransferable() {
        return this.transferable;
    }

    public void setTransferable(boolean transferable) {
        this.transferable = transferable;
    }

    public boolean isDecimalSupported() {
        return this.decimalSupported;
    }

    public void setDecimalSupported(boolean decimalSupported) {
        this.decimalSupported = decimalSupported;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getTexture() {
        return this.texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void addInterchangeableCurrency(Currency currency) {
        if (!this.interchangeableWith.contains(currency)) {
            this.interchangeableWith.add(currency);
        }
    }

    public void removeInterchangeableCurrency(Currency currency) {
        this.interchangeableWith.remove(currency);
    }

    public void setInterchangeableWith(List<Currency> interchangeableWith) {
        this.interchangeableWith = interchangeableWith;
    }

    public List<Currency> getInterchangeableWith() {
        return interchangeableWith;
    }

    public boolean isInterchangeableWith(Currency currency) {
        return interchangeableWith.stream().anyMatch(c -> c.equals(currency));
    }

    @Override
    public String toString() {
        return "Currency{" +
                "uuid='" + uuid + '\'' +
                ", singular='" + singular + '\'' +
                ", plural='" + plural + '\'' +
                ", symbol='" + symbol + '\'' +
                ", color='" + color + '\'' +
                ", decimalSupported=" + decimalSupported +
                ", transferable=" + transferable +
                ", defaultCurrency=" + defaultCurrency +
                ", defaultBalance=" + defaultBalance +
                ", exchangeRate=" + exchangeRate +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return uuid.equals(currency.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}

