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
import java.util.*;

public class ClassicClientC {
    public final String username = "admin";
    public final String password = "admin";
    public String token = "";
    public Map<String, List<DataBlock>> channelDataBlocks = new HashMap<>();
    public void start(int port){
        try (Socket socket = new Socket("localhost", port);
             OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {
            boolean isAuthenticated = false;
            List<String> channels = new ArrayList<>();
            channels.add("channel1");
            channels.add("channel2");
            isAuthenticated = authenticate(inputStream, outputStream);
            connectToPartition(inputStream, outputStream, channels);
            startListening(inputStream, outputStream);
            while (isAuthenticated){

                Protocol protocol = new DataConveyingProtocol();
                OResponse response = protocol.getResponse(inputStream);
                DataBlock dataBlock = response.getDataBlock();
                classifyDataBlock(dataBlock);
                // System.out.println(dataBlock);
                System.out.println(channelDataBlocks.get(dataBlock.getPartitionName()).getLast());
                // Send message to server

                // Receive response from server
//                System.out.println("From server: "+response.getMessage().trim());
//                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String response = reader.readLine();
//                System.out.println("Received from server: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(InputStream inputStream, OutputStream outputStream){
        Protocol protocol = new AuthenticationProtocol();
        String message = username + " " + password;
        try {
            protocol.sendRequest(new ORequest.RequestBuilder(Constants.AUTHENTICATION_PHASE).setUserType(Constants.CONSUMER).setMessage(message).build(),
                    outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            OResponse response = protocol.getResponse(inputStream);
            if(response.getResponseStatus() == Constants.RESPONSE_STATUS_SUCCESS){
                token = response.getMessage();
                System.out.println("Auth successful: " + token);
                return true;
            }else {
                System.err.println(response.getMessage());
                throw new RuntimeException(response.getMessage());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void connectToPartition(InputStream inputStream, OutputStream outputStream, List<String> channels){
        Protocol protocol = new ChannelRequestProtocol();
        for (String channel: channels){
            try {
                protocol.sendRequest(new ORequest.RequestBuilder(Constants.CHANNEL_CREATE_PHASE)
                                .setMessage(channel)
                                .setToken(token).build(),
                        outputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                OResponse response = protocol.getResponse(inputStream);
                if(response.getResponseStatus() == Constants.RESPONSE_STATUS_SUCCESS){
                    String msg = response.getMessage().trim();
                    System.out.println("Channel created: " + msg);
                    channelDataBlocks.put(msg, new ArrayList<>());
                }else {
                    System.err.println(response.getMessage());
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void startListening(InputStream inputStream, OutputStream outputStream){
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

    public void classifyDataBlock(DataBlock dataBlock){
        List<DataBlock> dataBlocks = channelDataBlocks.get(dataBlock.getPartitionName());
        dataBlocks.add(dataBlock);
    }
}
