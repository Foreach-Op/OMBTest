package DataTransmit;

import Login.ConsumerLoginProcess;
import Login.Loggable;
import Login.ProducerLoginProcess;
import Network.Useful.Constants;

public class DataTransmittableFactory {
    public static DataTransmittable getDataTransmittable(byte userType){
        return switch (userType) {
            case (Constants.CONSUMER) -> new ConsumerDataTransmission();
            case (Constants.PRODUCER) -> new ProducerDataTransmission();
            default -> null;
        };
    }
}
