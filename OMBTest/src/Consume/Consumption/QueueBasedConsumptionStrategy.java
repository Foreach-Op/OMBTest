package Consume.Consumption;

import Broker.DataBlock;
import Broker.Partition;
import Consume.ConsumerPipe;

import java.time.LocalDateTime;


public class QueueBasedConsumptionStrategy implements ConsumptionStrategy {
    @Override
    public DataBlock consume(ConsumerPipe consumerPipe) {
        Partition partition = consumerPipe.getPartition();
        int header = consumerPipe.getHeader();
        DataBlock dataBlock = partition.get(header);
        if(dataBlock == null){
            return null;
        }
        int partitionSize = partition.getPartitionLimit();
        LocalDateTime latestDataTime = consumerPipe.getLatestDataTime();

        header = (header+1) % partitionSize;
        // System.out.println(header);
        consumerPipe.setHeader(header);
        if(latestDataTime != null){
            LocalDateTime addedDateTime = dataBlock.getAddedDateTime();
            if(addedDateTime.isBefore(latestDataTime) || addedDateTime.isEqual(latestDataTime)){
                System.out.println(dataBlock.getCreatedDateTime()+" - "+latestDataTime);
                return null;
            }
        }
        consumerPipe.setLatestDataTime(dataBlock.getAddedDateTime());
        System.out.println("Consumed: " + dataBlock);
        return dataBlock;
    }
}
