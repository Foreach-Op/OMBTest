package Network.Useful;

public class OResponse {
    private byte phase;
    private byte userType;
    private byte responseStatus;
    private String message;
    private String token;
    private boolean isChecksumValid;

    private OResponse(ResponseBuilder builder) {
        this.phase = builder.phase;
        this.userType = builder.userType;
        this.responseStatus = builder.responseStatus;
        this.message = builder.message;
        this.token = builder.token;
        this.isChecksumValid = builder.isChecksumValid;
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

    public byte getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(byte responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsChecksumValid() {
        return isChecksumValid;
    }

    public void setChecksumValid(boolean checksumValid) {
        this.isChecksumValid = checksumValid;
    }

    public static class ResponseBuilder {
        private final byte phase;
        private byte userType;
        private byte responseStatus;
        private String message;
        private String token;

        private boolean isChecksumValid = true;


        public ResponseBuilder(byte phase) {
            this.phase = phase;
        }

        public ResponseBuilder setResponseStatus(byte responseStatus) {
            this.responseStatus = responseStatus;
            return this;
        }

        public ResponseBuilder setUserType(byte userType) {
            this.userType = userType;
            return this;
        }

        public ResponseBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public ResponseBuilder setToken(String token) {
            this.token = token;
            return this;
        }

        public ResponseBuilder setChecksumValid(boolean isChecksumValid){
            this.isChecksumValid = isChecksumValid;
            return this;
        }

        public OResponse build(){
            return new OResponse(this);
        }
    }
}



