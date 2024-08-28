package Network.Server.Protocol;

import Network.Useful.OResponse;

import java.nio.ByteBuffer;

public class ProtocolHandler {

    public static Protocol getProtocol(ByteBuffer byteBuffer){
        byte phase = byteBuffer.get();
        return ProtocolFactory.getProtocol(phase);
    }

    public static Protocol getProtocol(OResponse response){
        byte phase = response.getPhase();
        return ProtocolFactory.getProtocol(phase);
    }
}
