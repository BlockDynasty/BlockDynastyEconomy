package MessageChannel;

import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.courier.Message;
import lib.abstractions.PlatformAdapter;

public abstract class Publisher implements Courier {
    private final PlatformAdapter platformAdapter;

    public Publisher(PlatformAdapter platformAdapter) {
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
                lib.abstractions.IPlayer player = platformAdapter.getPlayerByUUID(message.getTarget());
                return player != null && player.isOnline();
            default:
                return false;
        }
    }
}