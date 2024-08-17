package Consume.Consumption;

import Broker.DataBlock;
import Broker.Partition;
import Consume.ConsumerPipe;

public interface ConsumptionStrategy {
    DataBlock consume(ConsumerPipe consumerPipe);
}
