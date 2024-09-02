package Network.Client.Protocol;

import Broker.DataBlock;
import Network.Useful.Constants;
import Network.Useful.HashProducer;
import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DataConveyingProtocol extends Protocol{
    public DataConveyingProtocol() {
        super();
    }

    @Override
    public ByteBuffer wrap(ORequest request) {
        // Phase->1 byte,
        // Message Type->1 byte,
        // Payload Size->4 byte,
        // Payload->n byte,
        // Token->16 byte,
        // Epoch->8 byte
        // Checksum->8 byte
        DataBlock dataBlock = request.getDataBlock();
        long epoch = dataBlock.getCreatedDateTime().toEpochSecond(ZoneOffset.UTC);
        String constructedMessage = constructData(dataBlock.getPartitionName(), dataBlock.getMessage());
        int messageLength = constructedMessage.length();
        byte[] payload = constructedMessage.getBytes();
        long checksum = HashProducer.calculateHash(payload);

        byte[] token = request.getToken().getBytes();

        ByteBuffer buffer = ByteBuffer.allocate(1 + 1 + 4 + messageLength + 16 + 8 + 8);
        buffer.put(request.getPhase()); // Phase
        buffer.put(dataBlock.getDataType()); // Message Type
        buffer.putInt(messageLength); // Message Length
        buffer.put(payload); // Payload
        buffer.put(token); // Token
        buffer.putLong(epoch); // Epoch
        buffer.putLong(checksum); // Checksum
        buffer.flip();

        return buffer;
    }

    @Override
    public OResponse extract(InputStream inputStream) throws IOException {
        // Phase->1 byte,
        // Message Type->1 byte,
        // Payload Size->4 byte,
        // Payload->n byte,
        // Epoch->8 byte
        // Checksum->8 byte
        byte[] header = new byte[6];
        int err = inputStream.read(header, 0, header.length);
        if (err == -1) throw new EOFException();

        byte phase = header[0];
        byte dataType = header[1];

        byte[] size = new byte[4];
        for (int i = 0, j = 2; i < size.length; i++, j++) {
            size[i] = header[j];
        }
        int messageLength = ByteBuffer.wrap(size).getInt();
        byte[] requestMessage = new byte[messageLength];
        err = inputStream.read(requestMessage, 0, messageLength);
        if (err == -1) throw new EOFException();

        byte[] epoch = new byte[8];
        err = inputStream.read(epoch,0, epoch.length);
        if (err == -1) throw new EOFException();
        long receivedEpoch = ByteBuffer.wrap(epoch).getLong();
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(receivedEpoch, 0, ZoneOffset.UTC);

        byte[] checksum = new byte[8];
        err = inputStream.read(checksum,0, checksum.length);
        if (err == -1) throw new EOFException();
        long receivedChecksum = ByteBuffer.wrap(checksum).getLong();
        long calculatedChecksum = HashProducer.calculateHash(requestMessage);
        boolean isCompatible = (receivedChecksum == calculatedChecksum);

        String msg = new String(requestMessage, StandardCharsets.UTF_8);
        String[] extractedMessage = extractData(msg);
        String partitionName = extractedMessage[0].trim();
        String message = extractedMessage[1].trim();
        DataBlock dataBlock = new DataBlock(message, partitionName);
        dataBlock.setCreatedDateTime(dateTime);
        dataBlock.setDataType(dataType);
        OResponse.ResponseBuilder responseBuilder = new OResponse.ResponseBuilder(Constants.DATA_PHASE);
        responseBuilder.setDataBlock(dataBlock);
        responseBuilder.setChecksumValid(isCompatible);
        return responseBuilder.build();
    }

    public String constructData(String partitionName, String message){
        StringBuilder sb = new StringBuilder();
        sb.append("%d#".formatted(partitionName.length()));
        sb.append(partitionName);
        sb.append(message);
        sb.append("\n");
        return sb.toString();
    }

    public String[] extractData(String data){
        int ind = data.indexOf("#");
        String str1 = data.substring(0,ind);
        int lng =Integer.parseInt(str1);
        String partitionName = data.substring(ind+1, ind+1+lng);
        String message = data.substring(ind+1+lng);
        return new String[]{partitionName, message};
    }
}
