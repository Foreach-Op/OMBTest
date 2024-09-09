package Consume;

import Broker.DataBlock;
import Broker.Partition;
import Consume.Consumption.ConsumeHandler;
import Consume.Consumption.ConsumingMethod;

import java.time.LocalDateTime;

public class ConsumerPipe {
    private final Partition partition;
    private final ConsumeHandler consumeHandler = new ConsumeHandler();
    private ConsumingMethod consumingMethod;
    private LocalDateTime latestDataTime;
    private int header;
    private int totalPipedData;

    public ConsumerPipe(Partition partition, ConsumingMethod consumingMethod){
        this(partition, consumingMethod, null);
    }

    public ConsumerPipe(Partition partition, ConsumingMethod consumingMethod, LocalDateTime latestDataTime){
        this.partition = partition;
        this.consumingMethod = consumingMethod;
        this.header = 0;
        this.totalPipedData = 0;
        this.latestDataTime = latestDataTime;
        // Where should a new consumer start to consume?
        // this.header = partition.getHeader() + 1;
    }

    public DataBlock consume(){
        DataBlock dataBlock = consumeHandler.consume(this);
        if(dataBlock != null)
            totalPipedData += 1;
        return dataBlock;
    }

    public Partition getPartition() {
        return partition;
    }

    public ConsumingMethod getConsumingMethod() {
        return consumingMethod;
    }

    public void setConsumingMethod(ConsumingMethod consumingMethod) {
        this.consumingMethod = consumingMethod;
    }

    public LocalDateTime getLatestDataTime() {
        return latestDataTime;
    }

    public void setLatestDataTime(LocalDateTime latestDataTime) {
        this.latestDataTime = latestDataTime;
    }

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }

    public int getTotalPipedData() {
        return totalPipedData;
    }

    @Override
    public String toString() {
        return "ConsumerPipe{" +
                "partition=" + partition +
                ", consumingMethod=" + consumingMethod +
                ", totalPipedData=" + totalPipedData +
                '}';
    }
}
