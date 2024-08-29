package Produce;

import Broker.DataBlock;
import Broker.Partition;
import Consume.Consumer;
import Consume.Consumption.ConsumeHandler;
import Consume.Consumption.ConsumingMethod;

import java.time.LocalDateTime;

public class ProducerPipe {
    private final Partition partition;
    private LocalDateTime localDateTime;

    public ProducerPipe(Partition partition) {
        this.partition = partition;
    }

    public void produce(DataBlock dataBlock){
        partition.add(dataBlock);
    }

    @Override
    public String toString() {
        return "ProducerPipe{" +
                "partition=" + partition +
                ", localDateTime=" + localDateTime +
                '}';
    }
}
