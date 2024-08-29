package Produce;

import Network.Useful.HashProducer;
import Security.User;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class Producer extends User {

    public Producer(String username, SocketChannel socketChannel) {
        super(username, socketChannel);
    }

}
