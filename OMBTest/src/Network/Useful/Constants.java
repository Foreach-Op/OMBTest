package Network.Useful;

public class Constants {
    private static Constants instance;

    private Constants(){}

    public static Constants getInstance(){
        if (instance == null)
            instance = new Constants();
        return instance;
    }

    public static final byte AUTHENTICATION_PHASE = 0x00;
    public static final byte SIGNUP_PHASE=0x01;
    public static final byte CMD_PHASE=0x02;
    public static final byte QUERYING_PHASE = 0x03;

    public static final byte AUTH_SUCCESS = 0x00;
    public static final byte AUTH_FAIL = 0x01;

    public static final byte PRODUCER = 0x00;
    public static final byte CONSUMER = 0x01;

    public byte RESPONSE_STATUS_SUCCESS = 0x00;
    public byte RESPONSE_STATUS_ERROR = 0x01;


}
