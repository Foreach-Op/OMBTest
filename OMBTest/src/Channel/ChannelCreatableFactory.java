package Channel;

import Login.ConsumerLoginProcess;
import Login.Loggable;
import Login.ProducerLoginProcess;
import Network.Useful.Constants;

public class ChannelCreatableFactory {
    public static ChannelCreatable getChannelCreatable(byte userType){
        return switch (userType) {
            case (Constants.CONSUMER) -> new ConsumerChannelCreate();
            case (Constants.PRODUCER) -> new ProducerChannelCreate();
            default -> null;
        };
    }
}
