package Produce;

import Consume.Consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProducerManager {
    private static ProducerManager producerManager;
    private final Map<String, Producer> producerMap = new HashMap<>();

    private ProducerManager(){}

    public static ProducerManager getInstance(){
        if(producerManager==null)
            producerManager = new ProducerManager();
        return producerManager;
    }

    public synchronized void addProducer(String token, Producer producer) throws Exception {
        if(producerMap.containsKey(token))
            throw new Exception("User already is in the system");
        producerMap.put(token, producer);
    }

    public synchronized boolean removeProducer(String token){
        if(!producerMap.containsKey(token))
            return false;
        producerMap.remove(token);
        return true;
    }

    public Producer getProducer(String token) throws Exception {
        if(!producerMap.containsKey(token))
            throw new Exception("Token is not in the system");
        return producerMap.get(token);
    }

    public List<Producer> getProducers() throws Exception {
        return producerMap.values().stream().toList();
    }


}
