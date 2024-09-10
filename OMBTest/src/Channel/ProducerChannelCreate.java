package Channel;

import Broker.Partition;
import Broker.PartitionManager;
import Consume.Consumer;
import Consume.ConsumerManager;
import Consume.ConsumerPipe;
import Consume.Consumption.ConsumingMethod;
import Produce.Producer;
import Produce.ProducerManager;
import Produce.ProducerPipe;

public class ProducerChannelCreate implements ChannelCreatable{
    @Override
    public void createChannel(String token, String partitionName) throws Exception {
        Producer producer = ProducerManager.getInstance().getProducer(token);
        Partition partition = PartitionManager.getInstance().addPartitionIfNotExists(partitionName);
        ProducerPipe producerPipe = new ProducerPipe(partition);
        producer.addProducerPipe(producerPipe);
    }
}
