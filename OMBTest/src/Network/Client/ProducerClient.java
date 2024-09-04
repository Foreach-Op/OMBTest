package Network.Client;

import Broker.DataBlock;
import Network.Client.Protocol.AuthenticationProtocol;
import Network.Client.Protocol.ChannelRequestProtocol;
import Network.Client.Protocol.DataConveyingProtocol;
import Network.Client.Protocol.Protocol;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class ProducerClient extends Client{

    BlockingQueue<DataBlock> blockBlockingQueue = new LinkedBlockingDeque<>();


    public ProducerClient(String username, String password, String host, int port, List<String> channelList) {
        super(username, password, host, port, Constants.PRODUCER, channelList);
    }

    public void startSending(String message){
        startSending(message, channels);
    }

    public void startSending(String message, Set<String> channelList){
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
        while (isStarted){
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
                protocol.sendRequest(requestBuilder.build(), outputStream);
                //bufferedOutputStream.flush();
                try {
                    OResponse response = protocol.getResponse(inputStream);
                    if(response.getResponseStatus() == Constants.RESPONSE_STATUS_SUCCESS){
                        DataBlock db = response.getDataBlock();
                        System.out.println("Server Response: " + db);
                    }else {
                        System.err.println(response.getMessage());
                        throw new RuntimeException(response.getMessage());
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                //Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
