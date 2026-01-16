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
package BlockDynasty.BukkitImplementation.adapters.proxy;

import abstractions.platform.IProxySubscriber;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;


/**
 * BlockDynasty Bungee-Spigot Messaging Listener
 *
 * This listener is used to update currencies and balance for players
 * on different servers. This is important to sustain synced balances
 * and currencies on all of the servers.
 */

public class ProxySubscriberImp implements PluginMessageListener {
    private final IProxySubscriber proxySubscriber;

    public ProxySubscriberImp(IProxySubscriber proxySubscriber) {
        this.proxySubscriber = proxySubscriber;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player notInUse, byte[] message) {
        this.proxySubscriber.onPluginMessageReceived(channel, message);
    }
}

