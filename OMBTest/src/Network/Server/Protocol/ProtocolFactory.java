package Network.Server.Protocol;

import Network.Useful.Constants;

public class ProtocolFactory {

    private ProtocolFactory(){}

    public static Protocol getProtocol(byte phase){
        Protocol protocol = new AuthenticationProtocol();
        if(phase == Constants.AUTHENTICATION_PHASE){
            protocol = new AuthenticationProtocol();
        } else if (phase == Constants.CHANNEL_CREATE_PHASE) {
            protocol = new ChannelRequestProtocol();
        } else if (phase == Constants.DATA_PHASE) {
            protocol = new DataConveyingProtocol();
        }
        return protocol;
    }
}
