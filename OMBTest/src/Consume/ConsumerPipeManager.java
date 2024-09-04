package Consume;

import Broker.Partition;
import Consume.Consumption.ConsumingMethod;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsumerPipeManager {
    private static ConsumerPipeManager consumerPipeManager;
    private final Map<String, List<ConsumerPipe>> map = new HashMap<>();

    private ConsumerPipeManager(){}

    public static ConsumerPipeManager getInstance(){
        if(consumerPipeManager==null)
            consumerPipeManager = new ConsumerPipeManager();
        return consumerPipeManager;
    }

    public void addConsumerPipe(String token, Partition partition, ConsumingMethod consumingMethod) throws Exception {
        ConsumerPipe consumerPipe = new ConsumerPipe(partition, consumingMethod);
        consumerPipe.setHeader(partition.getHeader());
        Consumer consumer = ConsumerManager.getInstance().getConsumer(token);
        consumer.addConsumerPipe(consumerPipe);
        if(!map.containsKey(token)){
            map.put(token, new ArrayList<>());
        }
        List<ConsumerPipe> consumerPipes = map.get(token);
        consumerPipes.add(consumerPipe);
        map.put(token, consumerPipes);
    }

    public void removeConsumerPipe(String token, String partitionName) throws Exception {
        Consumer consumer = ConsumerManager.getInstance().getConsumer(token);
        if(!map.containsKey(token)){
            throw new Exception("Token not found");
        }
        List<ConsumerPipe> consumerPipes = map.get(token);
        ConsumerPipe removed = consumerPipes.stream()
                .filter(s->s.getPartition().getName().equals(partitionName))
                .toList().getFirst();
        consumerPipes.remove(removed);
        consumer.removeConsumerPipe(removed);
    }

    public Map<String, List<ConsumerPipe>> getAllPipes(){
        return map;
    }
    public List<ConsumerPipe> getPipes(String token) throws Exception {
        if(!map.containsKey(token)){
            throw new Exception("No User found");
        }
        return map.get(token);
    }
}
