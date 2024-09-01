package Network.Server.Phase;

import Broker.Partition;
import Broker.PartitionManager;
import Consume.ConsumerManager;
import Consume.ConsumerPipeManager;
import Consume.Consumption.ConsumingMethod;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;
import Produce.ProducerManager;
import Produce.ProducerPipe;
import Produce.ProducerPipeManager;
import Security.TokenProcess;
import Security.UserManager;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class DataPhaseStrategy implements PhaseStrategy{
    private SocketChannel socketChannel;
    private Selector selector;

    @Override
    public OResponse execute(ORequest request) {
        System.out.println("DataPhaseStrategy");
        String token = request.getToken();
        byte userType = TokenProcess.determineUserType(token);
        socketChannel = request.getSocketChannel();
        selector = request.getSelector();
        OResponse.ResponseBuilder responseBuilder = new OResponse.ResponseBuilder(request.getPhase());
        responseBuilder.setUserType(userType);
        try {
            TokenProcess.verifyToken(token);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_TOKEN_NOT_VERIFIED).setMessage(e.getMessage());
            return responseBuilder.build();
        }
        String message = request.getMessage();
        String[] messageArr = message.split(" ");
        try {
            handleUser(userType, message, token);
        } catch (Exception e) {
            System.err.println(e);
            responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_ERROR).setMessage(e.getMessage());
            return responseBuilder.build();
        }
        return responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_SUCCESS).setMessage("Success").build();
    }

    private void handleUser(byte userType, String partitionName, String token) throws Exception {
        if(userType == Constants.PRODUCER){
            handleProducer(partitionName, token);
            System.out.println("Producer Data");
        } else if (userType == Constants.CONSUMER) {
            handleConsumer(partitionName, token);
            System.out.println("Consumer Data");
        }
    }

    public void handleProducer(String partitionName, String token) throws Exception {
        ProducerPipe pipe = ProducerPipeManager.getInstance().getPipe(token, "channel1");
        pipe.produce(partitionName);
        System.out.println("Produced");
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    public void handleConsumer(String partitionName, String token) throws Exception {

    }
}
