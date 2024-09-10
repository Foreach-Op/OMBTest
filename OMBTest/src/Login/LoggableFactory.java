package Login;

import Network.Useful.Constants;

public class LoggableFactory {
    public static Loggable getLoggable(byte userType){
        return switch (userType) {
            case (Constants.CONSUMER) -> new ConsumerLoginProcess();
            case (Constants.PRODUCER) -> new ProducerLoginProcess();
            default -> null;
        };
    }
}
