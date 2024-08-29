package Produce;

import Broker.DataBlock;
import Broker.Partition;
import Consume.ConsumerPipe;
import Consume.ConsumerPipeManagerNew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProducerPipeManager {
    private static ProducerPipeManager producerPipeManager;
    private final Map<String, List<ProducerPipe>> map = new HashMap<>();

    private ProducerPipeManager(){}

    public static ProducerPipeManager getInstance(){
        if(producerPipeManager==null)
            producerPipeManager = new ProducerPipeManager();
        return producerPipeManager;
    }

    public synchronized void addProducerPipe(String token, Partition partition){
        ProducerPipe producerPipe = new ProducerPipe(partition);
        if(!map.containsKey(token)){
            map.put(token, new ArrayList<>());
        }
        List<ProducerPipe> producerPipes = map.get(token);
        producerPipes.add(producerPipe);
        map.put(token, producerPipes);
    }

    public Map<String, List<ProducerPipe>> getPipes(){
        return map;
    }
}
