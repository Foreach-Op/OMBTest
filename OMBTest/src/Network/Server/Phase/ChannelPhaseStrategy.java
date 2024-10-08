package Network.Server.Phase;

import Broker.Partition;
import Broker.PartitionManager;
import Channel.ChannelCreationHandler;
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

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ChannelPhaseStrategy implements PhaseStrategy{
    private final ChannelCreationHandler channelCreationHandler = new ChannelCreationHandler();

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
            channelCreationHandler.handleChannelCreation(userType, token, partitionName);
        } catch (Exception e) {
            responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_ERROR).setMessage(e.getMessage());
            return responseBuilder.build();
        }
        return responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_SUCCESS).setMessage(partitionName).build();
    }
}
