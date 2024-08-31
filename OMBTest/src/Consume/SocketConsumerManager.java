package Consume;

import Broker.DataBlock;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;

public class SocketConsumerManager {
    private static SocketConsumerManager socketConsumerManager;
    private final HashMap<SocketChannel, Consumer> socketChannelConsumerHashMap = new HashMap<>();

    private SocketConsumerManager(){}

    public static SocketConsumerManager getInstance(){
        if(socketConsumerManager == null)
            socketConsumerManager = new SocketConsumerManager();
        return socketConsumerManager;
    }

    public synchronized void addSocketConsumer(SocketChannel socketChannel, Consumer consumer){
        socketChannelConsumerHashMap.put(socketChannel, consumer);
    }

    public Consumer getConsumer(SocketChannel socketChannel){
        return socketChannelConsumerHashMap.get(socketChannel);
    }

    public DataBlock[] getData(SocketChannel socketChannel, int amount){
        return socketChannelConsumerHashMap.get(socketChannel).removeDataBlocks(amount);
    }

    public boolean isTherePipe(SocketChannel socketChannel){
        Consumer consumer = socketChannelConsumerHashMap.get(socketChannel);
        if(consumer == null)
            return false;
        boolean isEmpty = true;
        try {
            isEmpty = ConsumerPipeManager.getInstance().getPipes(consumer.getToken()).isEmpty();
        } catch (Exception e) {
            throw new RuntimeException(e);
            //return false;
        }
        return isEmpty;
    }

}
