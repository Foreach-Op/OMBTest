package Consume;


import Broker.DataBlock;

import java.io.IOException;
import java.net.Socket;

public class Consumer {

    private final ConsumerSocket consumerSocket = new ConsumerSocket();
    private String name;
    private String hostname;
    private int port;

    public Consumer(String name, String hostname, int port){
        this.name = name;
        this.hostname = hostname;
        this.port = port;
        this.consumerSocket.connect(hostname, port);
    }

    public void sendData(DataBlock dataBlock){
        try {
            consumerSocket.sendData(dataBlock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
