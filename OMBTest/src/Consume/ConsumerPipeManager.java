package Consume;

import Broker.Partition;
import Consume.Consumption.ConsumingMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumerPipeManager {
    private static ConsumerPipeManager consumerPipeManager;
    private final Map<String, List<ConsumerPipe>> map = new HashMap<>();

    private ConsumerPipeManager(){}

    public static ConsumerPipeManager getInstance(){
        if(consumerPipeManager==null)
            consumerPipeManager = new ConsumerPipeManager();
        return consumerPipeManager;
    }

    public synchronized void addConsumerPipe(String token, Partition partition, ConsumingMethod consumingMethod) throws Exception {
        ConsumerPipe consumerPipe = new ConsumerPipe(partition, consumingMethod);
        Consumer consumer = ConsumerManager.getInstance().getConsumer(token);
        consumer.addConsumerPipe(consumerPipe);
        if(!map.containsKey(token)){
            map.put(token, new ArrayList<>());
        }
        List<ConsumerPipe> consumerPipes = map.get(token);
        consumerPipes.add(consumerPipe);
        map.put(token, consumerPipes);
    }

    public Map<String, List<ConsumerPipe>> getAllPipes(){
        return map;
    }
    public List<ConsumerPipe> getPipes(String token) throws Exception {
        if(!map.containsKey(token)){
            throw new Exception("No User");
        }
        return map.get(token);
    }
}
