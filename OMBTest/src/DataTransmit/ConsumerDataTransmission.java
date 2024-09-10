package DataTransmit;

import Broker.DataBlock;
import Consume.Consumer;
import Consume.ConsumerManager;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ConsumerDataTransmission implements DataTransmittable{
    @Override
    public void transmitData(String token, DataBlock dataBlock, SocketChannel socketChannel, Selector selector) throws Exception {
        System.out.println("Consumer Listening");
        Consumer consumer = ConsumerManager.getInstance().getConsumer(token);
        consumer.startThread();
        // SocketChannel socketChannel = SocketConsumerManager.getInstance().getSocketChannel(consumer);
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }
}
