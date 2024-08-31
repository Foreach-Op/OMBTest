package Consume;


import Broker.DataBlock;
import Security.User;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Consumer extends User {

    private final ConsumerThread consumerThread;
    private final BlockingQueue<DataBlock> dataBlockQueue = new LinkedBlockingQueue<>();

    public Consumer(String username, SocketChannel socketChannel) {
        super(username, socketChannel, "C");
        SocketConsumerManager.getInstance().addSocketConsumer(socketChannel, this);
        consumerThread = new ConsumerThread(token);
        consumerThread.start();
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

    public void addConsumerPipe(ConsumerPipe consumerPipe){
        consumerThread.addConsumerPipe(consumerPipe);
    }

    public void removeConsumerPipe(ConsumerPipe consumerPipe){
        consumerThread.removeConsumerPipe(consumerPipe);
    }

    public void terminate(){
        consumerThread.exit();
    }

//    public Consumer(String name){
//        this(name,null);
//    }
//
//    public Consumer(String name, SocketChannel socketChannel){
//        this.name = name;
//        this.socketChannel = socketChannel;
//        ConsumerManager.getInstance().subscribeConsumer(this);
//    }

//    public void sendData(DataBlock dataBlock){
//        try {
//            consumerSocket.sendData(dataBlock);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public String getName() {
        return super.username;
    }

}
