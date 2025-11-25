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
    private List<ICurrency> interchangeableWith;

    private Currency() {}

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

    public void addInterchangeableCurrency(ICurrency currency) {
        if (!this.interchangeableWith.contains(currency)) {
            this.interchangeableWith.add(currency);
        }
    }

    public void removeInterchangeableCurrency(ICurrency currency) {
        this.interchangeableWith.remove(currency);
    }

    public void setInterchangeableCurrencies(List<ICurrency> interchangeableWith) {
        this.interchangeableWith = interchangeableWith;
    }

    public List<ICurrency> getInterchangeableCurrencies() {
        return interchangeableWith;
    }

    public boolean isInterchangeableWith(ICurrency currency) {
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String uuid = UUID.randomUUID().toString();
        private String singular = "";
        private String plural = "";
        private String symbol= "";
        private String texture = "";
        private String color= "WHITE";
        private boolean decimalSupported= true;
        private boolean transferable= true;
        private boolean defaultCurrency= false;
        private BigDecimal defaultBalance= BigDecimal.ZERO;
        private double exchangeRate = 1;
        private List<ICurrency> interchangeableWith= new ArrayList<>();

        public Builder setUuid(UUID uuid) {
            this.uuid = uuid.toString();
            return this;
        }

        public Builder setSingular(String singular) {
            this.singular = singular;
            return this;
        }

        public Builder setPlural(String plural) {
            this.plural = plural;
            return this;
        }

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder setTexture(String texture) {
            this.texture = texture;
            return this;
        }

        public Builder setColor(String color) {
            this.color = color;
            return this;
        }

        public Builder setDecimalSupported(boolean decimalSupported) {
            this.decimalSupported = decimalSupported;
            return this;
        }

        public Builder setTransferable(boolean transferable) {
            this.transferable = transferable;
            return this;
        }

        public Builder setDefaultCurrency(boolean defaultCurrency) {
            this.defaultCurrency = defaultCurrency;
            return this;
        }
        public Builder setDefaultBalance(BigDecimal defaultBalance) {
            this.defaultBalance = defaultBalance;
            return this;
        }
        public Builder setExchangeRate(double exchangeRate) {
            this.exchangeRate = exchangeRate;
            return this;
        }
        public Builder setInterchangeableWith(List<ICurrency> interchangeableWith) {
            this.interchangeableWith = interchangeableWith;
            return this;
        }

        public Builder copy(ICurrency currency) {
            this.uuid = currency.getUuid().toString();
            this.singular = currency.getSingular();
            this.plural = currency.getPlural();
            this.symbol = currency.getSymbol();
            this.texture = currency.getTexture();
            this.color = currency.getColor();
            this.decimalSupported = currency.isDecimalSupported();
            this.transferable = currency.isTransferable();
            this.defaultCurrency = currency.isDefaultCurrency();
            this.defaultBalance = currency.getDefaultBalance();
            this.exchangeRate = currency.getExchangeRate();
            this.interchangeableWith = new ArrayList<>(currency.getInterchangeableCurrencies());
            return this;
        }
        public ICurrency build() {
            Currency currency = new Currency();
            currency.uuid = this.uuid;
            currency.singular = this.singular;
            currency.plural = this.plural;
            currency.symbol = this.symbol;
            currency.texture = this.texture;
            currency.color = this.color;
            currency.decimalSupported = this.decimalSupported;
            currency.transferable = this.transferable;
            currency.defaultCurrency = this.defaultCurrency;
            currency.defaultBalance = this.defaultBalance;
            currency.exchangeRate = this.exchangeRate;
            currency.interchangeableWith = this.interchangeableWith;
            return currency;
        }
    }
}

