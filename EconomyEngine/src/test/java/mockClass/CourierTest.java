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

package mockClass;

import BlockDynasty.Economy.domain.services.courier.Courier;

public class CourierTest implements Courier {

    public void sendUpdateMessage(String type, String name) {
        System.out.println("[BUNGEE CHANNEL SEND] " + type + " " + name);
    }

    @Override
    public void sendUpdateMessage(String type, String data, String target) {
        System.out.println("[BUNGEE CHANNEL SEND] " + type + " " + data+ " target: " + target);
    }


}
