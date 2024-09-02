package Network.Useful;

import Broker.DataBlock;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ORequest {
    private byte phase;
    private byte userType;
    private String message;
    private String token;
    private boolean isChecksumValid;
    private DataBlock dataBlock;
    private SocketChannel socketChannel;
    private Selector selector;

    private ORequest(RequestBuilder builder) {
        this.phase=builder.phase;
        this.userType =builder.userType;
        this.message=builder.message;
        this.token=builder.token;
        this.isChecksumValid =builder.isChecksumValid;
        this.dataBlock = builder.dataBlock;
        this.socketChannel = builder.socketChannel;
        this.selector = builder.selector;
    }

    public byte getPhase() {
        return phase;
    }

    public void setPhase(byte phase) {
        this.phase = phase;
    }

    public byte getUserType() {
        return userType;
    }

    public void setUserType(byte userType) {
        this.userType = userType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isChecksumValid() {
        return isChecksumValid;
    }

    public void setChecksumValid(boolean checksumValid) {
        this.isChecksumValid = checksumValid;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
    public Selector getSelector() {
        return selector;
    }
    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public DataBlock getDataBlock() {
        return dataBlock;
    }

    public void setDataBlock(DataBlock dataBlock) {
        this.dataBlock = dataBlock;
    }

    public static class RequestBuilder {
        private final byte phase;
        private byte userType;
        private String message = "";
        private String token;
        private DataBlock dataBlock;
        private boolean isChecksumValid = true;
        private SocketChannel socketChannel;
        private Selector selector;

        public RequestBuilder(byte phase) {
            this.phase = phase;
        }

        public RequestBuilder setUserType(byte userType) {
            this.userType = userType;
            return this;
        }

        public RequestBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public RequestBuilder setToken(String token) {
            this.token = token;
            return this;
        }

        public RequestBuilder setChecksumValid(boolean isChecksumValid){
            this.isChecksumValid = isChecksumValid;
            return this;
        }

        public RequestBuilder setSocketChannel(SocketChannel socketChannel){
            this.socketChannel = socketChannel;
            return this;
        }

        public RequestBuilder setSelector(Selector selector){
            this.selector = selector;
            return this;
        }

        public RequestBuilder setDataBlock(DataBlock dataBlock){
            this.dataBlock = dataBlock;
            return this;
        }

        public ORequest build(){
            return new ORequest(this);
        }
    }
}


