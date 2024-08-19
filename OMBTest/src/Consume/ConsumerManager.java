package Consume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumerManager {
    private static ConsumerManager consumerManager;
    private Map<String, Consumer> consumerMap = new HashMap<>();
    private Map<String, List<ConsumerPipe>> consumerPipeMap = new HashMap<>();

    private ConsumerManager(){}

    public static ConsumerManager getInstance(){
        if(consumerManager==null)
            consumerManager = new ConsumerManager();
        return consumerManager;
    }

    public synchronized void subscribeConsumer(Consumer consumer){
        //consumerMap.put(consumer.getUnique(), consumer);
        //consumerPipeMap.put(consumer.getUnique(), new ArrayList<>());
    }

    public synchronized void unsubscribeConsumer(String unique){
        consumerMap.remove(unique);
    }

    public synchronized void subscribePipe(String unique, ConsumerPipe consumerPipe){
        List<ConsumerPipe> consumerPipeList = consumerPipeMap.get(unique);
        consumerPipeList.add(consumerPipe);
    }

    public synchronized void unsubscribePipe(String unique, ConsumerPipe consumerPipe){
        consumerPipeMap.get(consumerMap.get(unique));
    }
}
