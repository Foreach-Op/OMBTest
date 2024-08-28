package Consume;

import Broker.DataBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class ConsumerPipeManager implements Runnable{
    private final List<ConsumerPipe> consumerPipeList;
    // Importance key, List value
    private HashMap<String, List<ConsumerPipe>> map;

    public ConsumerPipeManager(){
        this.consumerPipeList = new ArrayList<>();
    }

    public void addConsumerPipe(ConsumerPipe consumerPipe){
        consumerPipeList.add(consumerPipe);
    }

    @Override
    public void run() {
        for (ConsumerPipe consumerPipe : consumerPipeList) {
            DataBlock dataBlock = consumerPipe.consume();
            System.out.print("Consumed:");
            System.out.println(dataBlock);
        }
    }
}
