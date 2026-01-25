package com.blockdynasty.economy.MessageChannel;

import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.courier.Message;
import abstractions.platform.entity.IPlayer;
import com.blockdynasty.economy.platform.IPlatform;

public abstract class Publisher implements Courier {
    private final IPlatform platformAdapter;

    public Publisher(IPlatform platformAdapter) {
        this.platformAdapter = platformAdapter;
    }

    @Override
    public void sendUpdateMessage(Message message){
        if (shouldSkipProcessing(message)){
            return;
        }
        sendMessage(message);
    };

    protected abstract void sendMessage(Message message);

    private boolean shouldSkipProcessing(Message message) {
        switch (message.getType()) {
            case ACCOUNT:
            case EVENT:
                IPlayer player = platformAdapter.getPlayerByUUID(message.getTarget());
                return player != null && player.isOnline();
            default:
                return false;
        }
    }
}