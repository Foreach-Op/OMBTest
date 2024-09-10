package Channel;

public class ChannelCreationHandler {
    public void handleChannelCreation(byte userType, String token, String partitionName) throws Exception {
        ChannelCreatable channelCreatable = ChannelCreatableFactory.getChannelCreatable(userType);
        channelCreatable.createChannel(token, partitionName);
    }
}
