package Network.Client;

import Network.Client.Protocol.AuthenticationProtocol;
import Network.Client.Protocol.Protocol;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClassicClient {
    public void start(int port){
        try (Socket socket = new Socket("localhost", port);
             Scanner scanner = new Scanner(System.in)) {
            while (true){
                // Send message to server
                Protocol protocol = new AuthenticationProtocol();
                OutputStream output = socket.getOutputStream();
                String message = scanner.nextLine();

                protocol.sendRequest(new ORequest.RequestBuilder(Constants.CMD_PHASE, Constants.AUTH_FAIL).setMessage(message).build(),
                        output);
                System.out.println("Message: "+message);

                // Receive response from server
                InputStream inputStream = socket.getInputStream();
                OResponse response = protocol.getResponse(inputStream);
                System.out.println("From server: "+response.getMessage().trim());
//                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String response = reader.readLine();
//                System.out.println("Received from server: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
