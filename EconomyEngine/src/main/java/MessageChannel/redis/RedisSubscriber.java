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

package MessageChannel.redis;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.IOfferService;
import MessageChannel.Subscriber;
import utils.Console;
import lib.abstractions.PlatformAdapter;
import redis.clients.jedis.*;

public class RedisSubscriber extends Subscriber {
    private final HostAndPort hostAndPort;
    private final JedisClientConfig config;
    private Thread listenerThread;
    private boolean running = false;
    private final String channelName;

    public RedisSubscriber(RedisData redisData, PlatformAdapter platformAdapter,
                           IOfferService offerService, ICurrencyService currencyService,
                           IAccountService accountService, EventManager eventManager) {
        super(platformAdapter, offerService, currencyService, accountService, eventManager);

        this.hostAndPort = new HostAndPort(redisData.getHost(), redisData.getPort());
        this.config = DefaultJedisClientConfig.builder()
                .user(redisData.getUsername())
                .password(redisData.getPassword())
                .build();

        this.channelName = redisData.getChannelName();

        startListening();
    }

    public void startListening() {
        if (running) return;
        running = true;

        Runnable runnable = () -> {
            try {
                UnifiedJedis jedis = new UnifiedJedis(hostAndPort, config);
                jedis.subscribe(new JedisSubscriber(this), channelName);
            } catch (Exception e) {
                Console.logError("Redis subscription error: " + e.getMessage());
                running = false;
            }
        };

        listenerThread = new Thread(runnable);
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void stopListening() {
        running = false;
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
    }

    private class JedisSubscriber extends JedisPubSub {
        private final Subscriber subscriber;

        public  JedisSubscriber(Subscriber subscriber) {
            this.subscriber = subscriber;

        }
        @Override
        public void onMessage(String channel, String message) {
            if (!channel.equals(channelName)) {
                return;
            }
            subscriber.processMessage(message);
        }
    }
}