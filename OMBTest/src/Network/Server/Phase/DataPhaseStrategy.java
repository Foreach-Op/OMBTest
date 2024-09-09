package Network.Server.Phase;

import Broker.DataBlock;
import Consume.Consumer;
import Consume.ConsumerManager;
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
    private SocketChannel socketChannel;
    private Selector selector;

    @Override
    public OResponse execute(ORequest request, SocketChannel socketChannel, Selector selector) {
        String token = request.getToken();
        byte userType = TokenProcess.determineUserType(token);
        this.socketChannel = socketChannel;
        this.selector = selector;
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
            handleUser(userType, token, dataBlock);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_ERROR).setMessage(e.getMessage());
            return responseBuilder.build();
        }
        return responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_SUCCESS).setDataBlock(dataBlock).setUserType(userType).build();
    }

    private void handleUser(byte userType, String token, DataBlock dataBlock) throws Exception {
        if(userType == Constants.PRODUCER){
            handleProducer(dataBlock, token);
        } else if (userType == Constants.CONSUMER) {
            handleConsumer(dataBlock, token);
        }
    }

    public void handleProducer(DataBlock dataBlock, String token) throws Exception {
        // ProducerPipe pipe = ProducerPipeManager.getInstance().getPipe(token, dataBlock.getPartitionName());
        Producer producer = ProducerManager.getInstance().getProducer(token);
        ProducerPipe pipe = producer.getPipe(dataBlock.getPartitionName());
        pipe.produce(dataBlock);
        System.out.println("Produced: "+dataBlock);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    public void handleConsumer(DataBlock dataBlock, String token) throws Exception {
        System.out.println("Consumer Listening");
        Consumer consumer = ConsumerManager.getInstance().getConsumer(token);
        consumer.startThread();
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }
}
