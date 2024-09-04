package Network.Client.Protocol;

import Network.Useful.Constants;
import Network.Useful.HashProducer;
import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ChannelRequestProtocol extends Protocol {

    public ChannelRequestProtocol() {
        super();
    }

    @Override
    public ByteBuffer wrap(ORequest request) {
        // Phase->1 byte,
        // UserType->1 byte,
        // Message Size->4 byte,
        // Payload->n byte,
        // Token->16 byte,
        // Checksum->8 byte
        int messageLength = request.getMessage().length();
        byte[] payload = request.getMessage().getBytes();
        long checksum = HashProducer.calculateHash(payload);

        byte[] token = request.getToken().getBytes();

        ByteBuffer buffer = ByteBuffer.allocate(1 + 1 + 4 + messageLength + 16 + 8);
        buffer.put(request.getPhase()); // Phase
        buffer.put(request.getUserType()); // Response Type
        buffer.putInt(messageLength); // Message Length
        buffer.put(payload); // Payload
        buffer.put(token); // Token
        buffer.putLong(checksum); // Checksum
        buffer.flip();

        return buffer;
    }

    @Override
    public OResponse extract(InputStream inputStream) throws IOException {
        // Phase->1 byte (Already captured),
        // Type->1 byte,
        // Status->1 byte,
        // Message Size->4 byte,
        // Payload->n byte,
        // Checksum->8 byte
        byte[] header = new byte[7];
        int err = inputStream.read(header, 0, header.length);
        if (err == -1) throw new EOFException();

        byte phase = header[0];
        byte type = header[1];
        byte status = header[2];

        byte[] size = new byte[4];
        for (int i = 0, j = 3; i < size.length; i++, j++) {
            size[i] = header[j];
        }
        int messageLength = ByteBuffer.wrap(size).getInt();
        byte[] requestMessage = new byte[messageLength];
        err = inputStream.read(requestMessage, 0, messageLength);
        if (err == -1) throw new EOFException();
        byte[] checksum = new byte[8];
        err = inputStream.read(checksum,0, checksum.length);
        if (err == -1) throw new EOFException();
        long receivedChecksum = ByteBuffer.wrap(checksum).getLong();
        long calculatedChecksum = HashProducer.calculateHash(requestMessage);
        boolean isCompatible = (receivedChecksum == calculatedChecksum);
        String msg = new String(requestMessage, StandardCharsets.UTF_8);

//        byte type = byteBuffer.get();
//        byte status = byteBuffer.get();
//
//        byte[] size = new byte[4];
//        byteBuffer.get(size, 0, size.length);
//        int messageLength = ByteBuffer.wrap(size).getInt();
//
//        byte[] payload = new byte[messageLength];
//        byteBuffer.get(payload, 0, messageLength);
//
//        long receivedChecksum = byteBuffer.getLong();
//        long calculatedChecksum = calculateCRC32(payload);
//        boolean isCompatible = (receivedChecksum == calculatedChecksum);
//        String msg = new String(payload, StandardCharsets.UTF_8);
        OResponse.ResponseBuilder responseBuilder = new OResponse.ResponseBuilder(Constants.CHANNEL_CREATE_PHASE);
        responseBuilder.setUserType(type);
        responseBuilder.setResponseStatus(status);
        responseBuilder.setMessage(msg.trim());
        responseBuilder.setChecksumValid(isCompatible);
        return responseBuilder.build();
    }
}
