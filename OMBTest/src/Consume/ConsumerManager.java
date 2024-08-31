package Consume;

import Broker.DataBlock;

import java.nio.channels.SocketChannel;
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
        System.out.println(consumerMap.get(token));
    }

    public synchronized boolean removeConsumer(String token){
        if(!consumerMap.containsKey(token))
            return false;
        consumerMap.remove(token);
        return true;
    }

    public Consumer getConsumer(String token) throws Exception {
        System.out.println("------");
        System.out.println(consumerMap.keySet().size());
        for (String tok:consumerMap.keySet()){
            System.out.println(tok);
        }
        if(!consumerMap.containsKey(token))
            throw new Exception("Token is not in the system");
        return consumerMap.get(token);
    }

    public List<Consumer> getConsumers() throws Exception {
        return consumerMap.values().stream().toList();
    }

    public void addData(String token, List<DataBlock> dataBlocks){
        if(!consumerMap.containsKey(token))
            return;
        consumerMap.get(token).addDataBlocksToQueue(dataBlocks);
    }


}
