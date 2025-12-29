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

import BlockDynasty.Economy.domain.services.courier.Message;
import MessageChannel.Publisher;
import utils.Console;
import lib.abstractions.PlatformAdapter;
import redis.clients.jedis.*;

public class RedisPublisher extends Publisher{
    private final String channelName;private final HostAndPort hostAndPort;
    private final JedisClientConfig config;

    public RedisPublisher(RedisData redisData, PlatformAdapter platformAdapter) {
        super(platformAdapter);
        this.channelName = redisData.getChannelName();

        this.hostAndPort = new HostAndPort(redisData.getHost(), redisData.getPort());
        this.config = DefaultJedisClientConfig.builder()
                .user(redisData.getUsername())
                .password(redisData.getPassword())
                .build();
    }

    @Override
    protected void sendMessage(Message message) {
        try (UnifiedJedis jedis = new UnifiedJedis(hostAndPort, config)) {
            jedis.publish(channelName, message.toJsonString());
        } catch (Exception e) {
            Console.logError("Redis publish error: " + e.getMessage());
        }
    }
}