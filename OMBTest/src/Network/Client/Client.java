package Network.Client;

import Broker.DataBlock;
import Network.Client.Protocol.AuthenticationProtocol;
import Network.Client.Protocol.ChannelRequestProtocol;
import Network.Client.Protocol.Protocol;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Client extends Thread{

    protected final String username;
    protected final String password;
    protected final String host;
    protected final int port;
    protected final byte userType;
    protected String token;
    protected int capacity;
    protected volatile boolean isRunning = true;
    protected Socket socket;
    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected BufferedWriter bufferedWriter;
    protected BufferedOutputStream bufferedOutputStream;
    protected final Set<String> channels = new HashSet<>();
    protected final Map<String, BlockingQueue<DataBlock>> channelDataBlocks = new HashMap<>();
    protected final BlockingQueue<DataBlock> dataBlocks;

    public Client(String username, String password, String host, int port, byte userType, List<String> channelList){
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.userType = userType;
        this.channels.addAll(channelList);
        this.capacity = 10;
        this.dataBlocks = new ArrayBlockingQueue<>(capacity);
        for (String s: channels){
            channelDataBlocks.put(s, new ArrayBlockingQueue<>(capacity));
        }
    }

    public void connect(){
        boolean tryToConnect = true;
        while (tryToConnect){
            tryToConnect = false;
            try {
                socket = new Socket(host, port);
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedOutputStream = new BufferedOutputStream(outputStream);
                authenticate();
                connectToChannels();
            } catch (IOException e) {
                tryToConnect = true;
                try {
                    Thread.sleep(5000);
                    System.out.println("Trying Again");
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

    }

    private void authenticate(){
        Protocol protocol = new AuthenticationProtocol();
        String message = username + " " + password;
        ORequest.RequestBuilder requestBuilder = new ORequest.RequestBuilder(Constants.AUTHENTICATION_PHASE);
        requestBuilder.setUserType(userType).setMessage(message);
        try {
            protocol.sendRequest(requestBuilder.build(), outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            OResponse response = protocol.getResponse(inputStream);
            if(response.getResponseStatus() == Constants.RESPONSE_STATUS_SUCCESS){
                token = response.getMessage();
                System.out.println("Auth successful: " + token);
            }else {
                System.err.println(response.getMessage());
                throw new RuntimeException(response.getMessage());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void connectToChannels(){
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
                    //channelDataBlocks.put(msg, new ArrayList<>());
                }else {
                    System.err.println(response.getMessage());
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
