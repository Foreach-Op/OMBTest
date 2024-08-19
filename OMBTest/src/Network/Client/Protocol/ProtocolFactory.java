package Network.Client.Protocol;
import Network.Useful.Constants;

public class ProtocolFactory {

    private ProtocolFactory(){}

    public static Protocol getProtocol(byte phase){
        Protocol protocol = new AuthenticationProtocol();
        if(phase == Constants.AUTHENTICATION_PHASE){
            protocol = new AuthenticationProtocol();
        }
        return protocol;
    }
}
