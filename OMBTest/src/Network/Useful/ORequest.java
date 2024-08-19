package Network.Useful;

public class ORequest {
    private byte phase;
    private byte requestType;
    private String message;
    private String token;
    private boolean areChecksumsCompatible;

    private ORequest(RequestBuilder builder) {
        this.phase=builder.phase;
        this.requestType=builder.requestType;
        this.message=builder.message;
        this.token=builder.token;
        this.areChecksumsCompatible=builder.areChecksumsCompatible;
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

    public static class RequestBuilder {
        private final byte phase;
        private final byte requestType;
        private String message = "";
        private String token;
        private boolean areChecksumsCompatible = true;


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

        public RequestBuilder setAreChecksumsCompatible(boolean areChecksumsCompatible){
            this.areChecksumsCompatible = areChecksumsCompatible;
            return this;
        }

        public ORequest build(){
            return new ORequest(this);
        }
    }
}


