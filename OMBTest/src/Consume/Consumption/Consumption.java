package Consume.Consumption;

import Broker.DataBlock;
import Broker.Partition;
import Consume.ConsumerPipe;

public class Consumption {
    private final ConsumptionStrategy consumeStrategy;

    public Consumption(ConsumptionStrategy consumeStrategy){
        this.consumeStrategy = consumeStrategy;
    }

    public DataBlock executeConsumption(ConsumerPipe consumerPipe){
        return consumeStrategy.consume(consumerPipe);
    }
}
