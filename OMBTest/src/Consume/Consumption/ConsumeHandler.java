package Consume.Consumption;

import Broker.DataBlock;
import Consume.ConsumerPipe;

public class ConsumeHandler {

    public DataBlock consume(ConsumerPipe consumerPipe){
        ConsumptionStrategy consumptionStrategy = ConsumptionStrategyFactory.getConsumeStrategy(consumerPipe.getConsumingMethod());
        Consumption consumption = new Consumption(consumptionStrategy);
        return consumption.executeConsumption(consumerPipe);
    }
}
