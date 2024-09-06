package Consume;


import Broker.DataBlock;
import Broker.Partition;
import Consume.Consumption.ConsumingMethod;
import Security.User;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Consumer extends User {

    private final ConsumerThread consumerThread;
    private final List<ConsumerPipe> consumerPipes = new ArrayList<>();
    private final BlockingQueue<DataBlock> dataBlockQueue = new LinkedBlockingQueue<>();

    public Consumer(String username, SocketChannel socketChannel) {
        super(username, socketChannel, "C");
        SocketConsumerManager.getInstance().addSocketConsumer(socketChannel, this);
        consumerThread = new ConsumerThread(token, consumerPipes);
    }

    public void addConsumerPipe(ConsumerPipe consumerPipe) {
        synchronized (consumerPipes){
            consumerPipes.add(consumerPipe);
        }
    }

    public void addDataBlocksToQueue(List<DataBlock> dataBlocks){
        dataBlockQueue.addAll(dataBlocks);
    }

    public DataBlock[] removeDataBlocks(int amount){
        int size = Math.min(amount, dataBlockQueue.size());
        DataBlock[] dataBlocks = new DataBlock[size];
        for (int i = 0; i < size; i++) {
            dataBlocks[i] = dataBlockQueue.poll();
        }
        return dataBlocks;
    }

    public void startThread(){
        consumerThread.start();
    }

    public void terminateThread(){
        consumerThread.exit();
    }

    public String getName() {
        return super.username;
    }
}
