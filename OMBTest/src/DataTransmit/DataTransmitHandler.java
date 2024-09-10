package DataTransmit;

import Broker.DataBlock;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class DataTransmitHandler {
    public void handleDataTransmit(byte userType, String token, DataBlock dataBlock, SocketChannel socketChannel, Selector selector) throws Exception {
        DataTransmittable dataTransmittable = DataTransmittableFactory.getDataTransmittable(userType);
        dataTransmittable.transmitData(token, dataBlock, socketChannel, selector);
    }
}
