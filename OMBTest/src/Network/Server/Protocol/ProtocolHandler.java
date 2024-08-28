package Network.Server.Protocol;

import java.nio.ByteBuffer;

public class ProtocolHandler {

    public static Protocol getProtocol(ByteBuffer byteBuffer){
        byte phase = byteBuffer.get();
        return ProtocolFactory.getProtocol(phase);
    }
}
