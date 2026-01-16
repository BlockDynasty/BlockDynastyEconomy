package abstractions.platform;

public interface IProxySubscriber {
    void onPluginMessageReceived(String channel, byte[] bytecode);
}
