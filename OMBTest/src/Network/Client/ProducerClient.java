package Network.Client;

import Broker.DataBlock;
import Network.Client.Protocol.DataConveyingProtocol;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerClient extends Client{

    private final BlockingQueue<DataBlock> blockBlockingQueue = new LinkedBlockingQueue<>();
    private final DataConveyingProtocol protocol = new DataConveyingProtocol();

    public ProducerClient(String username, String password, String host, int port, List<String> channelList) {
        super(username, password, host, port, Constants.PRODUCER, channelList);
    }

    public void sendMessage(String message){
        sendMessage(message, channels);
    }

    public void sendMessage(String message, Set<String> channelList){
        for(String channel: channelList){
            if(!channels.contains(channel)){
                continue;
            }
            try {
                DataBlock dataBlock = new DataBlock(message, channel);
                blockBlockingQueue.put(dataBlock);
                // channelDataBlocks.get(channel).put(dataBlock);
                //System.out.println(dataBlock);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {
        connect();
        while (isRunning){
            try {
                DataBlock dataBlock = blockBlockingQueue.take();

                System.out.println(dataBlock + " is sending");
                ORequest.RequestBuilder requestBuilder = new ORequest.RequestBuilder(Constants.DATA_PHASE);
                requestBuilder.setToken(token);
                requestBuilder.setDataBlock(dataBlock);

                try {
                    protocol.sendRequest(requestBuilder.build(), outputStream);
                    OResponse response = protocol.getResponse(inputStream);
                    if(response.getResponseStatus() == Constants.RESPONSE_STATUS_SUCCESS){
                        DataBlock db = response.getDataBlock();
                        System.out.println("Server Response: " + db);
                    }else {
                        System.err.println(response.getDataBlock().getMessage());
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
