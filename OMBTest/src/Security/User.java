package Security;

import Network.Useful.HashProducer;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

public abstract class User {
    protected SocketChannel socketChannel;
    protected String username;
    protected String token;
    protected byte role;


    public User(String username, SocketChannel socketChannel, String type){
        this.username = username;
        this.socketChannel = socketChannel;
        this.token = TokenProcess.createToken(type);
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

    public byte getRole() {
        return role;
    }

    public void setRole(byte role) {
        this.role = role;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
