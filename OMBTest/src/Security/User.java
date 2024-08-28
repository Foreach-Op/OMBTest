package Security;

import java.nio.channels.SocketChannel;

public abstract class User {
    protected SocketChannel socketChannel;
    protected String username;
    protected String role;
    protected String token;

    public User(String username, SocketChannel socketChannel){
        this.username = username;
        createToken();
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public abstract void createToken();
}
