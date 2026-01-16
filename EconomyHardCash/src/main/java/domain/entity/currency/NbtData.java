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

package domain.entity.currency;

import java.util.HashMap;
import java.util.Map;

//warning: atributes can be null
public class NbtData {
    private String nameCurrency;
    private String uuidCurrency;
    private String value;
    private Map<String,String > nbtMap;

    public NbtData() {
        this.uuidCurrency = null;
        this.nameCurrency = null;
        this.value = null;
        this.nbtMap = new HashMap<>();
        this.nbtMap.put("uuidCurrency", null);
        this.nbtMap.put("nameCurrency", null);
        this.nbtMap.put("value", null);
    }
    public NbtData(String nameCurrency,String uuidCurrency, String value) {
        this.uuidCurrency = uuidCurrency;
        this.nameCurrency = nameCurrency;
        this.value = value;
        this.nbtMap = new HashMap<>();
        this.nbtMap.put("uuidCurrency",uuidCurrency);
        this.nbtMap.put("nameCurrency",nameCurrency);
        this.nbtMap.put("value",value);
    }

    public NbtData(Map<String,String> nbtMap) {
        this.nbtMap = nbtMap;
        this.uuidCurrency = nbtMap.getOrDefault("uuidCurrency",null);
        this.nameCurrency = nbtMap.getOrDefault("nameCurrency",null);
        this.value = nbtMap.getOrDefault("value",null);
    }

    public String getItemType() {
        return nameCurrency;
    }

    public String getUuidCurrency() {
        return uuidCurrency;
    }

    public String getValue() {
        return value;
    }

    public  Map<String,String> getNbtMap() {
        return nbtMap;
    }
}
