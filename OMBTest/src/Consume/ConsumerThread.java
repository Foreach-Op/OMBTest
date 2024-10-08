package Consume;

import Broker.DataBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConsumerThread extends Thread{

    private final String token;
    private final Set<ConsumerPipe> channels;
    private volatile boolean isRunning = true;

    public ConsumerThread(String token, Set<ConsumerPipe> channels) {
        this.token = token;
        this.channels = channels;
    }

    public void exit(){
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning){
            try {
                List<DataBlock> dataBlocks = new ArrayList<>();
                synchronized (channels){
                    for (ConsumerPipe consumerPipe : channels) {
                        //lock.lock();
                        DataBlock dataBlock = consumerPipe.consume();
                        if(dataBlock == null){
                            continue;
                        }
                        dataBlocks.add(dataBlock);
                    }
                }
                //Thread.sleep(1);
                ConsumerManager.getInstance().addData(token, dataBlocks);

            }
//            catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            finally {
                //lock.unlock();
            }
        }
    }
}
