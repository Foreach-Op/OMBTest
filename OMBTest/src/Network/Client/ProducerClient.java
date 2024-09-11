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

public class ProducerClient extends Client{

    BlockingQueue<DataBlock> blockBlockingQueue = new LinkedBlockingDeque<>();


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
        DataConveyingProtocol protocol = new DataConveyingProtocol();
        while (isRunning){
            try {
                DataBlock dataBlock = blockBlockingQueue.take();

                System.out.println(dataBlock + " is sending");
                ORequest.RequestBuilder requestBuilder = new ORequest.RequestBuilder(Constants.DATA_PHASE);
                requestBuilder.setToken(token);
                requestBuilder.setDataBlock(dataBlock);
                //ByteBuffer payload = protocol.wrap(requestBuilder.build());
                //byte[] byteArray = new byte[payload.remaining()];
                //payload.get(byteArray);
                //bufferedOutputStream.write(byteArray);
                //bufferedOutputStream.flush();
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
                //Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
