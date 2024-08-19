package Consume;


import Broker.DataBlock;
import Security.User;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public class Consumer extends User {


    public Consumer(String username, String password, SocketChannel socketChannel) {
        super(username, password, socketChannel);
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
