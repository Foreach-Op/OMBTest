package Network.Server.Phase;

import Broker.Partition;
import Broker.PartitionManager;
import Consume.ConsumerPipe;
import Consume.ConsumerPipeManagerNew;
import Consume.Consumption.ConsumingMethod;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;
import Produce.ProducerPipe;
import Produce.ProducerPipeManager;
import Security.UserManager;

public class ChannelPhaseStrategy implements PhaseStrategy{
    @Override
    public OResponse execute(ORequest request) {
        String token = request.getToken();
        byte userType = request.getUserType();
        OResponse.ResponseBuilder responseBuilder = new OResponse.ResponseBuilder(request.getPhase());
        responseBuilder.setUserType(userType);
        try {
            verifyToken(token);
        } catch (Exception e) {
            responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_TOKEN_NOT_VERIFIED).setMessage(e.getMessage());
            return responseBuilder.build();
        }
        String message = request.getMessage();
        String[] messageArr = message.split(" ");
        try {
            handleUser(userType, message, token);
        } catch (Exception e) {
            responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_ERROR).setMessage(e.getMessage());
            return responseBuilder.build();
        }
        return responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_SUCCESS).setMessage("Success").build();
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
        Partition partition = PartitionManager.getInstance().addPartitionIfNotExists(partitionName);
        ProducerPipeManager.getInstance().addProducerPipe(token, partition);
    }

    public void handleConsumer(String partitionName, String token) throws Exception {
        Partition partition = PartitionManager.getInstance().getPartition(partitionName);
        ConsumerPipeManagerNew.getInstance().addConsumerPipe(token, partition, ConsumingMethod.QUEUE);
    }

    private void verifyToken(String token) throws Exception {
        UserManager.getUser(token);
    }

}
