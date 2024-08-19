package Produce;

import Security.User;

import java.nio.channels.SocketChannel;

public class Producer extends User {

    public Producer(String username, String password, SocketChannel socketChannel) {
        super(username, password, socketChannel);
    }
}
