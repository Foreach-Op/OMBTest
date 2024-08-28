package Produce;

import Network.Useful.HashProducer;
import Security.User;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class Producer extends User {

    public Producer(String username, SocketChannel socketChannel) {
        super(username, socketChannel);
    }

    @Override
    public void createToken() {
        String remoteAddress = "";
        try {
            remoteAddress = socketChannel.getRemoteAddress().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String t = "producer"+username+remoteAddress;
        long hash = HashProducer.calculateHash(t.getBytes());
        super.token = String.valueOf(hash);
    }
}
