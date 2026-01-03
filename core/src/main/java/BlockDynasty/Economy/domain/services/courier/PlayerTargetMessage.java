package BlockDynasty.Economy.domain.services.courier;

import BlockDynasty.Economy.domain.entities.account.Player;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.UUID;

public class PlayerTargetMessage extends Message<PlayerTargetMessage>{
    private String targetPlayer;

    protected PlayerTargetMessage() {
        super();
    }

    public Player getTargetPlayer() {
        Gson gson = new Gson();
        try {
            gson.fromJson(targetPlayer, Player.class);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Invalid JSON format for Message", e);
        }
        return gson.fromJson(targetPlayer,Player.class);
    }

    public static PlayerMessageBuilder builder() {
        return new PlayerMessageBuilder();
    }

    public static class PlayerMessageBuilder extends Builder<PlayerTargetMessage> {
        public PlayerMessageBuilder() {
            super(PlayerTargetMessage.class);
        }

        public PlayerMessageBuilder setTargetPlayer(Player player) {
            Gson gson = new Gson();
            message.targetPlayer = gson.toJson(player);
            return this;
        }

        @Override
        public PlayerMessageBuilder setType(Type type) {
            super.setType(type);
            return this;
        }

        @Override
        public PlayerMessageBuilder setTarget(UUID target) {
            super.setTarget(target);
            return this;
        }

        @Override
        public PlayerMessageBuilder setData(String data) {
            super.setData(data);
            return this;
        }

        @Override
        public PlayerMessageBuilder fromJson(String json) {
            super.fromJson(json);
            return this;
        }
    }

}
