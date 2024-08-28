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

    public synchronized void addPartition(Partition partition) throws Exception {
        if(partitionHashMap.containsKey(partition.getName()))
            throw new Exception("Partition already exists");
        partitionHashMap.put(partition.getName(), partition);
    }

    public Partition getPartition(String partitionName) throws Exception {
        if(partitionHashMap.containsKey(partitionName))
            return partitionHashMap.get(partitionName);
        throw new Exception("Partition not found");
    }

}
