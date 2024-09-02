package Network.Client;

import Broker.DataBlock;
import Network.Client.Protocol.AuthenticationProtocol;
import Network.Client.Protocol.ChannelRequestProtocol;
import Network.Client.Protocol.DataConveyingProtocol;
import Network.Client.Protocol.Protocol;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClassicClientP {
    public final String username = "admin";
    public final String password = "admin";
    public List<String> channelList = new ArrayList<>();

    public String token = "";
    public void start(int port){
        try (Socket socket = new Socket("localhost", port);
             Scanner scanner = new Scanner(System.in);
             OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {
            List<String> channels = new ArrayList<>();
            channels.add("channel1");
            channels.add("channel2");
            boolean isAuthenticated = authenticate(inputStream, outputStream);
            connectToPartition(inputStream, outputStream, channels);
            int i = 0;
            while (isAuthenticated){
                String message = "Data Block From 1-" + i;
                startSending(inputStream, outputStream, message, channelList);
                Thread.sleep(1000);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(InputStream inputStream, OutputStream output){
        Protocol protocol = new AuthenticationProtocol();
        String message = username + " " + password;
        try {
            protocol.sendRequest(new ORequest.RequestBuilder(Constants.AUTHENTICATION_PHASE).setUserType(Constants.PRODUCER).setMessage(message).build(),
                    output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            OResponse response = protocol.getResponse(inputStream);
            if(response.getResponseStatus() == Constants.AUTH_SUCCESS){
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

    public void connectToPartition(InputStream inputStream, OutputStream output, List<String> channels){
        Protocol protocol = new ChannelRequestProtocol();
        for (String channel:channels){
            try {
                protocol.sendRequest(new ORequest.RequestBuilder(Constants.CHANNEL_CREATE_PHASE)
                                .setMessage(channel)
                                .setToken(token).build(),
                        output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                OResponse response = protocol.getResponse(inputStream);
                if(response.getResponseStatus() == Constants.RESPONSE_STATUS_SUCCESS){
                    String msg = response.getMessage().trim();
                    System.out.println("Channel created: " + msg);
                    channelList.add(msg);
                }else {
                    System.err.println(response.getMessage());
                    throw new RuntimeException(response.getMessage());
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void startSending(InputStream inputStream, OutputStream outputStream, String message, List<String> channels){
        Protocol protocol = new DataConveyingProtocol();

        for(String channel: channels){
            try {
                DataBlock dataBlock = new DataBlock(message, channel);
                ORequest.RequestBuilder requestBuilder = new ORequest.RequestBuilder(Constants.DATA_PHASE);
                requestBuilder.setToken(token);
                requestBuilder.setDataBlock(dataBlock);
                protocol.sendRequest(requestBuilder.build(), outputStream);
                System.out.println(dataBlock);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
