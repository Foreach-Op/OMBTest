package Network.Server.Phase;

import Broker.Partition;
import Broker.PartitionManager;
import Consume.Consumer;
import Consume.ConsumerManager;
import Consume.ConsumerPipe;
import Consume.Consumption.ConsumingMethod;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;
import Produce.Producer;
import Produce.ProducerManager;
import Produce.ProducerPipe;
import Security.TokenProcess;
import Security.UserManager;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ChannelPhaseStrategy implements PhaseStrategy{

    @Override
    public OResponse execute(ORequest request, SocketChannel socketChannel, Selector selector) {
        System.out.println("ChannelPhaseStrategy");
        String token = request.getToken();
        byte userType = TokenProcess.determineUserType(token);
        OResponse.ResponseBuilder responseBuilder = new OResponse.ResponseBuilder(request.getPhase());
        responseBuilder.setUserType(userType);
        try {
            TokenProcess.verifyToken(token);
        } catch (Exception e) {
            responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_TOKEN_NOT_VERIFIED).setMessage(e.getMessage());
            return responseBuilder.build();
        }
        String partitionName = request.getMessage();
        try {
            handleUser(userType, partitionName, token);
        } catch (Exception e) {
            responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_ERROR).setMessage(e.getMessage());
            return responseBuilder.build();
        }
        return responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_SUCCESS).setMessage(partitionName).build();
    }

    private void handleUser(byte userType, String partitionName, String token) throws Exception {
        if(userType == Constants.PRODUCER){
            handleProducer(partitionName, token);
            System.out.println("Producer Channel");
        } else if (userType == Constants.CONSUMER) {
            handleConsumer(partitionName, token);
            System.out.println("Consumer Channel");
        }
    }

    public void handleProducer(String partitionName, String token) throws Exception {
        Producer producer = ProducerManager.getInstance().getProducer(token);
        Partition partition = PartitionManager.getInstance().addPartitionIfNotExists(partitionName);
        ProducerPipe producerPipe = new ProducerPipe(partition);
        producer.addProducerPipe(producerPipe);
    }

    public void handleConsumer(String partitionName, String token) throws Exception {
        Consumer consumer = ConsumerManager.getInstance().getConsumer(token);
        // Partition partition = PartitionManager.getInstance().getPartition(partitionName);
        Partition partition = PartitionManager.getInstance().addPartitionIfNotExists(partitionName);
        ConsumerPipe consumerPipe = new ConsumerPipe(partition, ConsumingMethod.QUEUE);
        consumer.addConsumerPipe(consumerPipe);
    }

}
