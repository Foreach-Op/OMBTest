package Produce;

import Broker.DataBlock;
import Broker.Partition;
import Consume.Consumer;
import Consume.Consumption.ConsumeHandler;
import Consume.Consumption.ConsumingMethod;

import java.time.LocalDateTime;

public class ProducerPipe {
    private final Producer producer;
    private final Partition partition;
    private LocalDateTime localDateTime;

    public ProducerPipe(Producer producer, Partition partition) {
        this.producer = producer;
        this.partition = partition;
    }

    public void produce(DataBlock dataBlock){
        partition.add(dataBlock);
    }

    public Producer getProducer(){
        return producer;
    }
}
