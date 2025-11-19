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
import java.util.UUID;

public interface ICurrency {

     void setUuid(UUID uuid);
     void setSingular(String singular);
     void setPlural(String plural);
     UUID getUuid();
     String getSingular();
     String getPlural();
     BigDecimal getDefaultBalance() ;
     String format(BigDecimal amount);
     boolean isValidAmount(BigDecimal amount);
     boolean isDefaultCurrency();
     void setStartBalance(BigDecimal startBalance);
     void setDefaultBalance(BigDecimal defaultBalance);
     void setDefaultCurrency(boolean defaultCurrency);
     boolean isTransferable();
     void setTransferable(boolean transferable);
     boolean isDecimalSupported();
     void setDecimalSupported(boolean decimalSupported);
     String getColor();
     void setColor(String color);
     String getSymbol();
     String getTexture();
     void setSymbol(String symbol);
     double getExchangeRate();
     void setExchangeRate(double exchangeRate);
     boolean equals(Object o) ;
     int hashCode();
}
