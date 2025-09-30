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
import repository.Models.CurrencyDb;

import java.util.UUID;

public class CurrencyMapper {

    // De Modelo de Dominio → Entidad JPA
    public static CurrencyDb toEntity(Currency domain) {
        if (domain == null) {
            return null; // Manejo de caso nulo
        }
        CurrencyDb entity = new CurrencyDb();
        entity.setUuid(domain.getUuid().toString());
        entity.setSingular(domain.getSingular());
        entity.setPlural(domain.getPlural());
        entity.setColor(domain.getColor()); // Si tu dominio tiene color
        entity.setDecimalSupported(domain.isDecimalSupported());
        entity.setTransferable(domain.isTransferable());
        entity.setDefaultCurrency(domain.isDefaultCurrency());
        entity.setDefaultBalance(domain.getDefaultBalance());
        entity.setExchangeRate(domain.getExchangeRate());
        entity.setSymbol(domain.getSymbol()); // Si tu dominio tiene símbolo
        return entity;
    }

    // De Entidad JPA → Modelo de Dominio
    public static Currency toDomain(CurrencyDb entity) {
        if (entity == null) {
            return null; // Manejo de caso nulo
        }
        return new Currency(
                UUID.fromString(entity.getUuid()),
                entity.getSingular(),
                entity.getPlural(),
                entity.getSymbol(),
                entity.getColor(),
                entity.isDecimalSupported(),
                entity.isTransferable(),
                entity.isDefaultCurrency(),
                entity.getDefaultBalance(),
                entity.getExchangeRate()
        );
    }
}