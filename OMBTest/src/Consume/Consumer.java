package Consume;


import Broker.DataBlock;
import Network.Useful.HashProducer;
import Security.User;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Consumer extends User {

    private final ConsumerThread consumerThread = new ConsumerThread();

    public Consumer(String username, SocketChannel socketChannel) {
        super(username, socketChannel);
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
