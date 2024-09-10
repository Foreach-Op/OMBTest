package Channel;

import Broker.Partition;
import Broker.PartitionManager;
import Consume.Consumer;
import Consume.ConsumerManager;
import Consume.ConsumerPipe;
import Consume.Consumption.ConsumingMethod;

public class ConsumerChannelCreate implements ChannelCreatable{
    @Override
    public void createChannel(String token, String partitionName) throws Exception {
        Consumer consumer = ConsumerManager.getInstance().getConsumer(token);
        Partition partition = PartitionManager.getInstance().addPartitionIfNotExists(partitionName);
        ConsumerPipe consumerPipe = new ConsumerPipe(partition, ConsumingMethod.QUEUE);
        consumer.addConsumerPipe(consumerPipe);
    }
}
