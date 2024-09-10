package Network.Server.Phase;

import Broker.DataBlock;
import Consume.Consumer;
import Consume.ConsumerManager;
import DataTransmit.DataTransmitHandler;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;
import Produce.Producer;
import Produce.ProducerManager;
import Produce.ProducerPipe;
import Security.TokenProcess;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class DataPhaseStrategy implements PhaseStrategy{

    private final DataTransmitHandler dataTransmitHandler = new DataTransmitHandler();

    @Override
    public OResponse execute(ORequest request, SocketChannel socketChannel, Selector selector) {
        String token = request.getToken();
        byte userType = TokenProcess.determineUserType(token);
        OResponse.ResponseBuilder responseBuilder = new OResponse.ResponseBuilder(request.getPhase());
        responseBuilder.setUserType(userType);
        try {
            TokenProcess.verifyToken(token);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_TOKEN_NOT_VERIFIED).setMessage(e.getMessage());
            return responseBuilder.build();
        }
        DataBlock dataBlock = request.getDataBlock();

        try {
            dataTransmitHandler.handleDataTransmit(userType, token, dataBlock, socketChannel, selector);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_ERROR).setMessage(e.getMessage());
            return responseBuilder.build();
        }
        return responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_SUCCESS).setDataBlock(dataBlock).setUserType(userType).build();
    }
}
