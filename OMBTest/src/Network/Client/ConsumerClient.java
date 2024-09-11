package Network.Client;

import Broker.DataBlock;
import Network.Client.Protocol.DataConveyingProtocol;
import Network.Client.Protocol.Protocol;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ConsumerClient extends Client{

    public ConsumerClient(String username, String password, String host, int port, List<String> channelList) {
        super(username, password, host, port, Constants.CONSUMER, channelList);
    }

    public void startListening(){
        Protocol protocol = new DataConveyingProtocol();
        try {
            DataBlock dataBlock = new DataBlock("Start Listening", "dummy");
            ORequest.RequestBuilder requestBuilder = new ORequest.RequestBuilder(Constants.DATA_PHASE);
            requestBuilder.setToken(token);
            requestBuilder.setDataBlock(dataBlock);
            protocol.sendRequest(requestBuilder.build(), outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DataBlock receiveData(String channel){
        BlockingQueue<DataBlock> dataBlocks = channelDataBlocks.get(channel);
        return dataBlocks.poll();
    }

    public void classifyDataBlock(DataBlock dataBlock){
        BlockingQueue<DataBlock> dataBlocks = channelDataBlocks.get(dataBlock.getPartitionName());
        try {
            if(dataBlocks.remainingCapacity() == 0)
                dataBlocks.poll();
            dataBlocks.put(dataBlock);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        connect();
        startListening();
        while (isRunning){
            Protocol protocol = new DataConveyingProtocol();
            try {
                OResponse response = protocol.getResponse(inputStream);
                DataBlock dataBlock = response.getDataBlock();
                //System.out.println(dataBlock);
                classifyDataBlock(dataBlock);
            } catch (Exception e) {
                isRunning = false;
                System.err.println(e.getMessage());
            }
        }
    }
}
