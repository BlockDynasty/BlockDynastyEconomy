package MessageChannelTest.mock;

import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.courier.Message;
import MessageChannel.Subscriber;

public class PublisherMock implements Courier {
    private Subscriber subscriber;
    public PublisherMock(Subscriber subscriber) {
        this.subscriber = subscriber;
    }
    @Override
    public void sendUpdateMessage(Message message) {
        subscriber.processMessage(message.toJsonString());
    }
}
