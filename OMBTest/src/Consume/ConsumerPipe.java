package Consume;

import Broker.DataBlock;
import Broker.Partition;
import Consume.Consumption.ConsumeHandler;
import Consume.Consumption.ConsumingMethod;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsumerPipe {
    private final Consumer consumer;
    private final Partition partition;
    private ConsumeHandler consumeHandler;
    private ConsumingMethod consumingMethod;
    private LocalDateTime localDateTime;
    private int header;
    private int totalPipedData;

    public ConsumerPipe(Consumer consumer, Partition partition, ConsumingMethod consumingMethod){
        this.consumer = consumer;
        this.partition = partition;
        this.consumingMethod = consumingMethod;
        this.consumeHandler = new ConsumeHandler();
        this.header = 0;
        this.totalPipedData = 0;
    }

    public DataBlock consume(){
        DataBlock dataBlock = consumeHandler.consume(this);
        if(dataBlock != null)
            totalPipedData += 1;
        return dataBlock;
    }

    public Consumer getConsumer() {
        return consumer;
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

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
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
}
