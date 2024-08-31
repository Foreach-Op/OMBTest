package Network.Client;

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
import java.util.Scanner;

public class ClassicClientC {
    public final String username = "admin";
    public final String password = "admin";
    public String token = "";
    public void start(int port){
        try (Socket socket = new Socket("localhost", port);
             Scanner scanner = new Scanner(System.in);
             OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {
            boolean isAuthenticated = false;
            isAuthenticated = authenticate(outputStream, inputStream);
            connectToPartition(outputStream, inputStream);
            while (isAuthenticated){
                Protocol protocol = new DataConveyingProtocol();
                OResponse response = protocol.getResponse(inputStream);
                System.out.println(response.getMessage());
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

    public boolean authenticate(OutputStream output, InputStream inputStream){
        Protocol protocol = new AuthenticationProtocol();
        String message = username + " " + password;
        try {
            protocol.sendRequest(new ORequest.RequestBuilder(Constants.AUTHENTICATION_PHASE).setUserType(Constants.CONSUMER).setMessage(message).build(),
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

    public void connectToPartition(OutputStream output, InputStream inputStream){
        Protocol protocol = new ChannelRequestProtocol();
        try {
            protocol.sendRequest(new ORequest.RequestBuilder(Constants.CHANNEL_CREATE_PHASE)
                            .setMessage("channel1")
                            .setToken(token).build(),
                    output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            OResponse response = protocol.getResponse(inputStream);
            if(response.getResponseStatus() == Constants.RESPONSE_STATUS_SUCCESS){
                String msg = response.getMessage();
                System.out.println("Channel created: " + msg);
            }else {
                System.err.println(response.getMessage());
                throw new RuntimeException(response.getMessage());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
