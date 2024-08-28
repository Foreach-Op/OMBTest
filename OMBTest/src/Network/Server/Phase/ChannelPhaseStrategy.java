package Network.Server.Phase;

import Broker.Partition;
import Broker.PartitionManager;
import Consume.ConsumerPipe;
import Consume.ConsumerPipeManager;
import Consume.ConsumerPipeManagerNew;
import Consume.Consumption.ConsumingMethod;
import Network.Useful.Constants;
import Network.Useful.ORequest;

import java.nio.channels.SocketChannel;

public class ChannelPhaseStrategy implements PhaseStrategy{
    @Override
    public void execute(ORequest request) {
        String message = request.getMessage();
        String[] messageArr = message.split(" ");

    }

    private void handleUser(byte userType, String partitionName, String token){
        if(userType == Constants.PRODUCER){
            handleProducer();
        } else if (userType == Constants.CONSUMER) {
            handleConsumer(partitionName, token);
        }
    }

    public void handleConsumer(String partitionName, String token){
        try {
            Partition partition = PartitionManager.getInstance().getPartition(partitionName);
            ConsumerPipe consumerPipe = new ConsumerPipe(partition, ConsumingMethod.QUEUE);
            ConsumerPipeManagerNew.getInstance().addConsumerPipe(token, consumerPipe);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void handleProducer(){

    }

}
