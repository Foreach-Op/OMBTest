package Network.Client;

import Network.Client.Protocol.AuthenticationProtocol;
import Network.Client.Protocol.Protocol;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClassicClient {
    public final String username = "admin";
    public final String password = "admin2";
    public String token = "";
    public void start(int port){
        try (Socket socket = new Socket("localhost", port);
             Scanner scanner = new Scanner(System.in);
             OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {
            boolean isAuthenticated = false;
            while (true){
                // Send message to server

                if(!isAuthenticated){
                    isAuthenticated = authenticate(outputStream, inputStream);
                }

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
}
