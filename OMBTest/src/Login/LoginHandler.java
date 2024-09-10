package Login;

import Security.User;

import java.nio.channels.SocketChannel;

public class LoginHandler {
    public User handleLogin(byte userType, String username, SocketChannel socketChannel) throws Exception {
        Loggable loggable = LoggableFactory.getLoggable(userType);
        return loggable.login(username, socketChannel);
    }
}
