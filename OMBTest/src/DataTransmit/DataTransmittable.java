package DataTransmit;

import Broker.DataBlock;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public interface DataTransmittable {
    void transmitData(String token, DataBlock dataBlock, SocketChannel socketChannel, Selector selector) throws Exception;
}
