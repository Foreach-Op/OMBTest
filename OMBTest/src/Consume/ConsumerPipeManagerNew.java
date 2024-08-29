package Consume;

import Broker.DataBlock;
import Broker.Partition;
import Consume.Consumption.ConsumingMethod;
import Produce.ProducerPipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumerPipeManagerNew {
    private static ConsumerPipeManagerNew consumerPipeManager;
    private final Map<String, List<ConsumerPipe>> map = new HashMap<>();

    private ConsumerPipeManagerNew(){}

    public static ConsumerPipeManagerNew getInstance(){
        if(consumerPipeManager==null)
            consumerPipeManager = new ConsumerPipeManagerNew();
        return consumerPipeManager;
    }

    public synchronized void addConsumerPipe(String token, Partition partition, ConsumingMethod consumingMethod){
        ConsumerPipe consumerPipe = new ConsumerPipe(partition, consumingMethod);

        if(!map.containsKey(token)){
            map.put(token, new ArrayList<>());
        }
        List<ConsumerPipe> consumerPipes = map.get(token);
        consumerPipes.add(consumerPipe);
        map.put(token, consumerPipes);
    }

    public Map<String, List<ConsumerPipe>> getPipes(){
        return map;
    }
}
