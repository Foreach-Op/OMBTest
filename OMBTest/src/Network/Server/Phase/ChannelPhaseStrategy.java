package Network.Server.Phase;

import Broker.Partition;
import Broker.PartitionManager;
import Consume.ConsumerPipe;
import Consume.ConsumerPipeManager;
import Consume.ConsumerPipeManagerNew;
import Consume.Consumption.ConsumingMethod;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;
import Produce.Producer;
import Produce.ProducerManager;
import Produce.ProducerPipe;
import Produce.ProducerPipeManager;

import java.nio.channels.SocketChannel;

public class ChannelPhaseStrategy implements PhaseStrategy{
    @Override
    public OResponse execute(ORequest request) {
        String token = request.getToken();
        String message = request.getMessage();
        String[] messageArr = message.split(" ");
        return null;
    }

    private void handleUser(byte userType, String partitionName, String token) throws Exception {
        if(userType == Constants.PRODUCER){
            handleProducer(partitionName, token);
        } else if (userType == Constants.CONSUMER) {
            handleConsumer(partitionName, token);
        }
    }

    public void handleProducer(String partitionName, String token) throws Exception {
        Partition partition = new Partition(partitionName);
        PartitionManager.getInstance().addPartition(partition);
        ProducerPipe producerPipe = new ProducerPipe(partition);
        ProducerPipeManager.getInstance().addProducerPipe(token, producerPipe);
    }

    public void handleConsumer(String partitionName, String token) throws Exception {
        Partition partition = PartitionManager.getInstance().getPartition(partitionName);
        ConsumerPipe consumerPipe = new ConsumerPipe(partition, ConsumingMethod.QUEUE);
        ConsumerPipeManagerNew.getInstance().addConsumerPipe(token, consumerPipe);
    }

}
