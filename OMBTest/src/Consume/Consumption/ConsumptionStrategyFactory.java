package Consume.Consumption;

import java.util.HashMap;
import java.util.Map;

import static Consume.Consumption.ConsumingMethod.*;

public class ConsumptionStrategyFactory {
    public static ConsumptionStrategy getConsumeStrategy(ConsumingMethod operation){
        Map<ConsumingMethod, ConsumptionStrategy> consumeStrategyMap = new HashMap<>();
        consumeStrategyMap.put(QUEUE, new QueueBasedConsumptionStrategy());
        consumeStrategyMap.put(STACK, new StackBasedConsumptionStrategy());
        ConsumptionStrategy consumptionStrategy = consumeStrategyMap.get(operation);
        return (consumptionStrategy!=null)?consumptionStrategy:consumeStrategyMap.get(QUEUE);
    }
}
