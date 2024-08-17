package Consume;


import Broker.DataBlock;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ConsumerSocket {

    private Socket socket;
    private PrintWriter out;


    public void connect(String host, int port){
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendData(DataBlock dataBlock) throws Exception {
        if(socket == null)
            throw new Exception("Not Connected");
        out.println(dataBlock);
    }
}
