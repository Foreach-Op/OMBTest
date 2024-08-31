package Consume;

import Broker.DataBlock;
import Network.Server.Protocol.DataConveyingProtocol;
import Network.Useful.Constants;
import Network.Useful.OResponse;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConsumerThread extends Thread{

    private final String token;
    private final Lock lock = new ReentrantLock();
    private final List<ConsumerPipe> channels = new ArrayList<>();
    private boolean isExited = false;

    public ConsumerThread(String token) {
        this.token = token;
    }

    public void addConsumerPipe(ConsumerPipe consumerPipe){
        synchronized (channels){
            channels.add(consumerPipe);
        }
//        try{
//            lock.lock();
//        }finally {
//            lock.unlock();
//        }
    }

    public void removeConsumerPipe(ConsumerPipe consumerPipe){
        synchronized (channels){
            channels.remove(consumerPipe);
        }
//        try{
//            lock.lock();
//            channels.remove(consumerPipe);
//        }finally {
//            lock.unlock();
//        }
    }

    public void exit(){
        isExited = true;
    }

    @Override
    public void run() {
        while (!isExited){
            try {
                List<DataBlock> dataBlocks = new ArrayList<>();
                synchronized (channels){
                    for (ConsumerPipe consumerPipe : channels) {
                        //lock.lock();
                        DataBlock dataBlock = consumerPipe.consume();
                        if(dataBlock == null){
                            System.out.println("Not Consumed");
                            continue;
                        }
                        dataBlocks.add(dataBlock);

                        //System.out.print("Consumed:");
                        //System.out.println(dataBlock);
                    }
                }
                Thread.sleep(100);
                ConsumerManager.getInstance().addData(token, dataBlocks);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                //lock.unlock();
            }
        }
    }
}
