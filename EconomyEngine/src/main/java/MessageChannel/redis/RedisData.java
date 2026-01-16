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

import platform.files.Configuration;
import services.configuration.IConfiguration;

import java.util.UUID;

public class RedisData {
    private String host= "localhost";
    private int port= 6379;
    private String password= "";
    private String username= "";

    public RedisData(IConfiguration configuration){
        this.host = configuration.getString("redis.host");
        this.port = configuration.getInt("redis.port");
        this.password = configuration.getString("redis.password");
        this.username = configuration.getString("redis.user");
    }
    public String getChannelName() {
        return "proxy:blockdynastyeconomy";
    }
    public String getHost() {
        return host;
    }
    public int getPort() {
        return port;

    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
}
