package Broker;

import Consume.ConsumerPipe;

import java.util.HashMap;
import java.util.List;

public class PartitionManager {

    private HashMap<String, Partition> partitionHashMap;
    private List<ConsumerPipe> consumerPipeList;

    // One thread for each consumer

}
