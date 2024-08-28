package Consume;

import Broker.DataBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConsumerThread implements Runnable{

    private final Lock lock = new ReentrantLock();
    private final List<ConsumerPipe> channels = new ArrayList<>();
    private boolean isExited = false;

    public void addConsumerPipe(ConsumerPipe consumerPipe){
        try{
            lock.lock();
            channels.add(consumerPipe);
        }finally {
            lock.unlock();
        }
    }

    public void removeConsumerPipe(ConsumerPipe consumerPipe){
        try{
            lock.lock();
            channels.remove(consumerPipe);
        }finally {
            lock.unlock();
        }
    }

    public void exit(){
        isExited = true;
    }

    @Override
    public void run() {
        while (!isExited){
            try {
                lock.lock();
                for (ConsumerPipe consumerPipe : channels) {
                    DataBlock dataBlock = consumerPipe.consume();
                    if(dataBlock == null)
                        continue;
                    System.out.print("Consumed:");
                    System.out.println(dataBlock);
                }
            }finally {
                lock.unlock();
            }
        }
    }
}
