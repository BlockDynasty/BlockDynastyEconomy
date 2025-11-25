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

package repository.Mappers;

import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import repository.Models.CurrencyDb;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CurrencyMapper {

    // De Modelo de Dominio → Entidad JPA
    public static CurrencyDb toEntity(ICurrency domain) {
        if (domain == null) return null;

        CurrencyDb entityDb = new CurrencyDb();
        entityDb.setUuid(domain.getUuid().toString());
        entityDb.setSingular(domain.getSingular());
        entityDb.setPlural(domain.getPlural());
        entityDb.setColor(domain.getColor());
        entityDb.setDecimalSupported(domain.isDecimalSupported());
        entityDb.setTransferable(domain.isTransferable());
        entityDb.setDefaultCurrency(domain.isDefaultCurrency());
        entityDb.setDefaultBalance(domain.getDefaultBalance());
        entityDb.setExchangeRate(domain.getExchangeRate());
        entityDb.setSymbol(domain.getSymbol());
        entityDb.setTexture(domain.getTexture());
        entityDb.setInterchangeableWith(convertListToEntity(domain.getInterchangeableCurrencies()));
        return entityDb;
    }

    // De Entidad JPA → Modelo de Dominio
    public static ICurrency toDomain(CurrencyDb entity) {
        if (entity == null) return null;
        return Currency.builder()
                .setUuid(UUID.fromString(entity.getUuid()))
                .setSingular(entity.getSingular())
                .setPlural(entity.getPlural())
                .setSymbol(entity.getSymbol())
                .setTexture(entity.getTexture())
                .setColor(entity.getColor())
                .setDecimalSupported(entity.isDecimalSupported())
                .setTransferable(entity.isTransferable())
                .setDefaultCurrency(entity.isDefaultCurrency())
                .setDefaultBalance(entity.getDefaultBalance())
                .setExchangeRate(entity.getExchangeRate())
                .setInterchangeableWith(convertListToDomain(entity.getInterchangeableWith()))
                .build();
    }

    // Actualizar campos (excepto ID/UUID)
    public static void update(ICurrency currency,CurrencyDb currencyDb) {
        currencyDb.setSingular(currency.getSingular());
        currencyDb.setPlural(currency.getPlural());
        currencyDb.setColor(currency.getColor());
        currencyDb.setDecimalSupported(currency.isDecimalSupported());
        currencyDb.setTransferable(currency.isTransferable());
        currencyDb.setDefaultCurrency(currency.isDefaultCurrency());
        currencyDb.setDefaultBalance(currency.getDefaultBalance());
        currencyDb.setExchangeRate(currency.getExchangeRate());
        currencyDb.setSymbol(currency.getSymbol());
        currencyDb.setTexture(currency.getTexture());
        currencyDb.setInterchangeableWith(convertListToEntity(currency.getInterchangeableCurrencies()));
    }

    public static ICurrency toDomainReference(CurrencyDb entity) {
        if (entity == null) return null;

        return Currency.builder()
                .setUuid(UUID.fromString(entity.getUuid()))
                .setSingular(entity.getSingular())
                .setPlural(entity.getPlural())
                .setSymbol(entity.getSymbol())
                .setTexture(entity.getTexture())
                .setColor(entity.getColor())
                .setDecimalSupported(entity.isDecimalSupported())
                .setTransferable(entity.isTransferable())
                .setDefaultCurrency(entity.isDefaultCurrency())
                .setDefaultBalance(entity.getDefaultBalance())
                .setExchangeRate(entity.getExchangeRate())
                .build();
    }
    public static CurrencyDb toEntityReference(ICurrency domain) {
        if (domain == null) return null;
        CurrencyDb entityDb = new CurrencyDb();
        entityDb.setUuid(domain.getUuid().toString());
        return entityDb;
    }

    private static List<CurrencyDb> convertListToEntity(List<ICurrency> domainList) {
        return domainList.stream()
                .map(CurrencyMapper::toEntityReference)
                .collect(Collectors.toList());
    }

    private static List<ICurrency> convertListToDomain(List<CurrencyDb> currencyDbs) {
        return currencyDbs.stream()
                .map(CurrencyMapper::toDomainReference)
                .collect(Collectors.toList());
    }

}