package Login;

import Security.User;

import java.nio.channels.SocketChannel;

public interface Loggable {
    User login(String username, SocketChannel socketChannel) throws Exception;
}
