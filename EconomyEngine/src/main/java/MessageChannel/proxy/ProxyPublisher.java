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

package MessageChannel.proxy;

import BlockDynasty.Economy.domain.services.courier.Message;
import MessageChannel.Publisher;
import platform.IPlatform;
import services.Console;

import java.io.IOException;

public class ProxyPublisher extends Publisher {
    private final IPlatform platformAdapter;

    public ProxyPublisher(IPlatform platformAdapter) {
        super(platformAdapter);
        this.platformAdapter = platformAdapter;
    }

    @Override
    protected void sendMessage(Message message) {
        try {
            platformAdapter.sendPluginMessage(ProxyData.getChannelName(), message.toJsonBytes());
        } catch (IOException e) {
            Console.logError("Proxy channel error :" + e.getMessage());
        }
    }
}
