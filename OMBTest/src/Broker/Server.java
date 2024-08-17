package Broker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;
    private final int port = 8080;

    public Server() {
        try {
            this.serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true){

        }
    }

    private void listAndAccept(){
        try {
            Socket clientSocket = serverSocket.accept();
            Connection connection = new Connection(clientSocket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
