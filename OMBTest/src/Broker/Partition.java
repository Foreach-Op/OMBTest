package Broker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Partition {

    private String name;
    // private final List<DataBlock> partitionList;
    private final DataBlock[] partitionArr;
    private final Lock lock = new ReentrantLock(true);
    private int header;
    private int partitionLimit;
    private int totalAddedData;

    public Partition(String name){
        this(name,100);
    }

    public Partition(String name, int partitionLimit){
        this.name = name;
        this.partitionLimit = partitionLimit;
        this.header = 0;
        this.totalAddedData = 0;
        this.partitionArr = new DataBlock[partitionLimit];
    }

    public void add(DataBlock data){
        lock.lock();
        try {
            partitionArr[header++] = data;
            if(partitionLimit > header)
                header %= partitionLimit;
            totalAddedData++;
        }finally {
            lock.unlock();
        }
    }

    public void add(int index, DataBlock data){
        lock.lock();
        try {
            if(header >= partitionLimit)
                header %= partitionLimit;
            // partitionList.add(index, data);
            partitionArr[index] = data;
        }finally {
            lock.unlock();
        }
    }

    public DataBlock get(int index){
        if(index >= partitionArr.length)
            return null;
        return partitionArr[index];
    }

    public int size(){
        return partitionArr.length;
    }
    public int getPartitionLimit(){
        return partitionLimit;
    }

    public int getTotalAddedData(){
        return totalAddedData;
    }

    public String getName(){
        return name;
    }

    public void setPartitionLimit(int partitionLimit){
        this.partitionLimit = partitionLimit;
    }
}
