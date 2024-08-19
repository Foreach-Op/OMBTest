package Security;

import java.nio.channels.SocketChannel;

public abstract class User {
    protected SocketChannel socketChannel;
    protected String username;
    protected String password;
    protected String token;

    public User(String username, String password, SocketChannel socketChannel){
        this.username = username;
        this.password = password;
        this.socketChannel = socketChannel;
    }

}
