package Network.Server.Protocol;

import Network.Useful.Constants;
import Network.Useful.HashProducer;
import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ChannelRequestProtocol extends Protocol{

    public ChannelRequestProtocol() {
        super();
    }

    @Override
    public ByteBuffer wrap(OResponse response) {
        // Phase->1 byte,
        // UserType->1 byte,
        // Status->1 byte,
        // Message Size->4 byte,
        // Payload->n byte,
        // Checksum->8 byte
        // String encodedMessage = encode(response.getMessage().split(" "));
        String message = response.getMessage();
        message += "\n";
        int messageLength = message.length();
        byte[] payload = message.getBytes();
        long checksum = HashProducer.calculateHash(payload);

        ByteBuffer buffer = ByteBuffer.allocate(1 + 1 + 1 + 4 + messageLength + 8);

        buffer.put(response.getPhase()); // Phase
        buffer.put(response.getUserType()); // Response Type
        buffer.put(response.getResponseStatus()); // Response Status
        buffer.putInt(messageLength); // Message Length
        buffer.put(payload); // Payload
        buffer.putLong(checksum); // Checksum
        //buffer.put("\n".getBytes());
        buffer.flip();
        return buffer;
    }


    @Override
    public ORequest extract(ByteBuffer byteBuffer) throws IOException {
        // Phase->1 byte (Already captured),
        // UserType->1 byte,
        // Payload Size->4 byte,
        // Payload->n byte,
        // Token->16 byte,
        // Checksum->8 byte
        // byte phase = byteBuffer.get();
        byte type = byteBuffer.get();
        byte[] size = new byte[4];
        byteBuffer.get(size, 0, size.length);
        int messageLength = ByteBuffer.wrap(size).getInt();
        byte[] payload = new byte[messageLength];
        byteBuffer.get(payload, 0, messageLength);
        byte[] token = new byte[16];
        byteBuffer.get(token, 0, token.length);
        long receivedChecksum = byteBuffer.getLong();
        long calculatedChecksum = HashProducer.calculateHash(payload);
        boolean isCompatible = (receivedChecksum == calculatedChecksum);
        String msg = new String(payload, StandardCharsets.UTF_8);
        String tkn = new String(token, StandardCharsets.UTF_8);

        ORequest.RequestBuilder requestBuilder = new ORequest.RequestBuilder(Constants.CHANNEL_CREATE_PHASE);
        requestBuilder.setUserType(type).setMessage(msg).setToken(tkn).setChecksumValid(isCompatible);
        return requestBuilder.build();
    }
}
