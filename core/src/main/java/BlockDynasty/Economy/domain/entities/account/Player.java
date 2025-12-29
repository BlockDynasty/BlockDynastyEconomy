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

package BlockDynasty.Economy.domain.entities.account;

import java.util.UUID;

public class Player {
    private Long dbId;
    private UUID uuid;
    private String nickname;

    public Player(Long dbId, UUID uuid, String nickname) {
        this.dbId = dbId;
        this.uuid = uuid;
        this.nickname = nickname;
    }

    public Player(UUID uuid, String nickname) {
        this.uuid = uuid;
        this.nickname = nickname;
    }
    public Player(Player player) {
        this.dbId = player.getId();
        this.uuid = player.getUuid();
        this.nickname = player.getNickname();
    }
    public Long getId(){
        return dbId;
    }

    public Long setId(Long id){
        return this.dbId = id;
    }

    public UUID getUuid() {
        return uuid;
    }
    public String getNickname() {
        return nickname;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "Player{" +
                "uuid='" + uuid + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
