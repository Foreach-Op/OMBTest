package Network.Useful;

import java.nio.channels.SocketChannel;

public class ORequest {
    private byte phase;
    private byte requestType;
    private String message;
    private String token;
    private boolean isChecksumValid;
    private SocketChannel socketChannel;

    private ORequest(RequestBuilder builder) {
        this.phase=builder.phase;
        this.requestType=builder.requestType;
        this.message=builder.message;
        this.token=builder.token;
        this.isChecksumValid =builder.isChecksumValid;
    }

    public byte getPhase() {
        return phase;
    }

    public void setPhase(byte phase) {
        this.phase = phase;
    }

    public byte getRequestType() {
        return requestType;
    }

    public void setRequestType(byte requestType) {
        this.requestType = requestType;
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

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public static class RequestBuilder {
        private final byte phase;
        private final byte requestType;
        private String message = "";
        private String token;
        private boolean isChecksumValid = true;
        private SocketChannel socketChannel;

        public RequestBuilder(byte phase, byte requestType) {
            this.phase = phase;
            this.requestType = requestType;
        }

        public RequestBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public RequestBuilder setToken(String token) {
            this.token = token;
            return this;
        }

        public RequestBuilder setChecksumValid(boolean checksumValid){
            this.isChecksumValid = checksumValid;
            return this;
        }

        public RequestBuilder setSocketChannel(SocketChannel socketChannel){
            this.socketChannel = socketChannel;
            return this;
        }

        public ORequest build(){
            return new ORequest(this);
        }
    }
}


