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
    public static final byte CHANNEL_CREATE_PHASE=0x02;
    public static final byte DATA_PHASE = 0x03;

    public static final byte AUTH_SUCCESS = 0x00;
    public static final byte AUTH_FAIL = 0x01;


    public static final byte PRODUCER = 0x00;
    public static final byte CONSUMER = 0x01;


    public static final byte RESPONSE_STATUS_SUCCESS = 0x00;
    public static final byte RESPONSE_STATUS_ERROR = 0x01;
    public static final byte RESPONSE_STATUS_TOKEN_NOT_VERIFIED = 0x02;
    public static final byte RESPONSE_STATUS_AUTHENTICATION_ERROR = 0x04;


    public static final byte ROLE_USER = 0x00;
    public static final byte ROLE_ADMIN = 0x01;

    public static final byte INFO_DATA_TYPE = 0x00;
    public static final byte EVENT_DATA_TYPE = 0x01;
    public static final byte ERROR_DATA_TYPE = 0x03;

}
