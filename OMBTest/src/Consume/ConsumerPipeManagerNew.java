package Consume;

import Broker.DataBlock;

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

    public synchronized void addConsumerPipe(String token, ConsumerPipe consumerPipe){
        if(!map.containsKey(token)){
            map.put(token, new ArrayList<>());
        }
        List<ConsumerPipe> consumerPipes = map.get(token);
        consumerPipes.add(consumerPipe);
        map.put(token, consumerPipes);
    }
}
