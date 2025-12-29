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

package BlockDynasty.Economy.domain.services.courier;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Message class representing a message in the courier system.
 * Includes type, target, data, and instance ID for origin tracking.
 * Supports JSON serialization/deserialization.
 * Uses Builder pattern for construction.
 */
public class Message {
    public enum Type {
        @SerializedName("account")
        ACCOUNT,
        @SerializedName("currency")
        CURRENCY,
        @SerializedName("event")
        EVENT;
    }

    private final static UUID serverId = UUID.randomUUID();
    private Type type;
    private UUID target;
    private String data;
    private UUID instanceId;

    private Message() {
        data= "Undefined/Empty";
    }

    public Type getType() {
        return type;
    }

    public UUID getTarget() {
        return target;
    }

    public String getData() {
        return data;
    }

    public String toJsonString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public boolean isType(Type type){
        return this.type == type;
    }

    public byte[] toJsonBytes() throws IOException {
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(outBytes)) {
            out.writeUTF(toJsonString());
        }
        return outBytes.toByteArray();
    }

    public boolean isSameOrigin(){
        if(instanceId != null){
            return instanceId.equals(serverId);
        }
        return false;
    }

    @Override
    public String toString() {
        return toJsonString();
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private Message message;

        public Builder() {
            message = new Message();
            message.instanceId = serverId;
        }

        public Builder setType(Type type) {
            message.type = type;
            return this;
        }

        public Builder setTarget(UUID target) {
            message.target = target;
            return this;
        }

        public Builder setData(String data) {
            message.data = data;
            return this;
        }

        public Builder fromJson(String json) {
            Gson gson = new Gson();
            try{
                this.message = gson.fromJson(json, Message.class);
            }catch(JsonSyntaxException e){
                throw new IllegalArgumentException("Invalid JSON format for Message", e);
            }
            return this;
        }

        public Message build() {
            if (message.type == null || message.target == null) {
                throw new IllegalStateException("Type and Target must be set");
            }
            return message;
        }
    }
}