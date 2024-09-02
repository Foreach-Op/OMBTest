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
        int partitionSize = partition.getPartitionLimit();
        LocalDateTime latestDataTime = consumerPipe.getLocalDateTime();
        DataBlock dataBlock = partition.get(header);
        //System.out.println(header);
        if(dataBlock == null){
            System.out.println("Data Block is null");
            return null;
        }
        if(latestDataTime != null){
            if(dataBlock.getAddedDateTime().isBefore(latestDataTime)){
                System.out.println(dataBlock.getDateTime()+" - "+latestDataTime);
                return null;
            }
        }
        header = (header+1) % partitionSize;
        consumerPipe.setHeader(header);
        consumerPipe.setLocalDateTime(dataBlock.getDateTime());
        return dataBlock;
    }
}
