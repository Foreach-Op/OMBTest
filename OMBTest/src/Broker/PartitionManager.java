package Broker;

import java.util.HashMap;

public class PartitionManager {

    private static PartitionManager partitionManager;

    private final HashMap<String, Partition> partitionHashMap = new HashMap<>();

    private PartitionManager(){}

    public static PartitionManager getInstance(){
        if(partitionManager==null)
            partitionManager = new PartitionManager();
        return partitionManager;
    }

    public synchronized boolean addPartition(Partition partition){
        if(partitionHashMap.containsKey(partition.getName()))
            return false;
        partitionHashMap.put(partition.getName(), partition);
        return true;
    }

    public Partition getPartition(String partitionName) throws Exception {
        if(partitionHashMap.containsKey(partitionName))
            return partitionHashMap.get(partitionName);
        throw new Exception("Partition not found");
    }

}
