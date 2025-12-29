package lib.abstractions;

public interface IProxySubscriber {
    void onPluginMessageReceived(String channel, byte[] bytecode);
}
