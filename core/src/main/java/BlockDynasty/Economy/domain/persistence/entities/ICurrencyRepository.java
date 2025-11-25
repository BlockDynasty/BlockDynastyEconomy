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

package BlockDynasty.Economy.domain.persistence.entities;

import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;

import java.util.List;

public interface ICurrencyRepository {
    List<ICurrency> findAll();
    ICurrency findByName(String name);
    ICurrency findByUuid(String uid);
    ICurrency findDefaultCurrency();
    void save(ICurrency currency);
    void delete(ICurrency currency);
    void update(ICurrency currency);
    void create(ICurrency currency);
}
