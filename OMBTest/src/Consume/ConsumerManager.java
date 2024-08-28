package Consume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumerManager {
    private static ConsumerManager consumerManager;
    private final Map<String, Consumer> consumerMap = new HashMap<>();

    private ConsumerManager(){}

    public static ConsumerManager getInstance(){
        if(consumerManager==null)
            consumerManager = new ConsumerManager();
        return consumerManager;
    }

    public synchronized void addConsumer(String token, Consumer consumer) throws Exception {
        if(consumerMap.containsKey(token))
            throw new Exception("User already is in the system");
        consumerMap.put(token, consumer);
    }

    public synchronized boolean removeConsumer(String token){
        if(!consumerMap.containsKey(token))
            return false;
        consumerMap.remove(token);
        return true;
    }

    public synchronized void subscribeConsumer(Consumer consumer){
        //consumerMap.put(consumer.getUnique(), consumer);
        //consumerPipeMap.put(consumer.getUnique(), new ArrayList<>());
    }

    public synchronized void unsubscribeConsumer(String unique){
        consumerMap.remove(unique);
    }


}
