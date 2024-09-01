package Produce;

import Broker.Partition;

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

    public void addProducerPipe(String token, Partition partition){
        ProducerPipe producerPipe = new ProducerPipe(partition);
        if(!map.containsKey(token)){
            map.put(token, new ArrayList<>());
        }
        List<ProducerPipe> producerPipes = map.get(token);
        producerPipes.add(producerPipe);
        map.put(token, producerPipes);
    }

    public ProducerPipe getPipe(String token, String partitionName) throws Exception {
        if(!map.containsKey(token))
            throw new Exception("No user found");

        return map.get(token).stream().filter(s->s.getPartition().getName().equals(partitionName))
                .toList().getFirst();
    }

    public Map<String, List<ProducerPipe>> getPipes(){
        return map;
    }
}
