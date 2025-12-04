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

package repository.Models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "currency")
public class CurrencyDb {
    @Id
    @Column(name = "uuid", unique = true) // Asegurar unicidad
    private String uuid;

    @Column(name = "name_singular")
    private String singular;

    @Column(name = "name_plural")
    private String plural;

    @Column(name = "symbol")
    private String symbol;

    @Column(name= "texture")
    private String texture;

    @Column(name = "color")
    private String color;

    @Column(name = "decimal_supported")
    private boolean decimalSupported;

    @Column(name = "transferable")
    private boolean transferable;

    @Column(name = "default_currency")
    private boolean defaultCurrency;

    @Column(name = "default_balance")
    private BigDecimal defaultBalance;

    @Column(name = "exchange_rate")
    private double exchangeRate;

    //@OnDelete(action = OnDeleteAction.CASCADE)
    //@OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "currency_interchangeable",
            joinColumns = @JoinColumn(name = "currency_uuid"),
            inverseJoinColumns = @JoinColumn(name = "interchangeable_uuid")
    )
    private List<CurrencyDb> interchangeableWith  = new ArrayList<>();

    //esto se agrega para que hibernate elimine en cascada los balances asociados a esta moneda
    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BalanceDb> balances = new ArrayList<>();

    public CurrencyDb() {
    }

    //setter y getter
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getSingular() {
        return singular;
    }
    public void setSingular(String singular) {
        this.singular = singular;
    }
    public String getPlural() {
        return plural;
    }
    public void setPlural(String plural) {
        this.plural = plural;

    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getTexture() {
        return texture;
    }
    public void setTexture(String texture) {
        this.texture = texture;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;

    }
    public boolean isDecimalSupported() {
        return decimalSupported;
    }
    public void setDecimalSupported(boolean decimalSupported) {
        this.decimalSupported = decimalSupported;
    }
    public boolean isTransferable() {
        return transferable;
    }
    public void setTransferable(boolean transferable) {
        this.transferable = transferable;
    }
    public boolean isDefaultCurrency() {
        return defaultCurrency;
    }
    public void setDefaultCurrency(boolean defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }
    public BigDecimal getDefaultBalance() {
        return defaultBalance;
    }
    public void setDefaultBalance(BigDecimal defaultBalance) {
        this.defaultBalance = defaultBalance;
    }
    public double getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    public List<CurrencyDb> getInterchangeableWith() {
        return interchangeableWith;
    }
    public void setInterchangeableWith(List<CurrencyDb> interchangeableWith) {
        this.interchangeableWith = interchangeableWith;
    }

}