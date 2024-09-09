package Network.Server.Protocol;

import Broker.DataBlock;
import Network.Useful.Constants;
import Network.Useful.HashProducer;
import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DataConveyingProtocol extends Protocol{

    public DataConveyingProtocol() {
        super();
    }

    @Override
    public ByteBuffer wrap(OResponse response) {
        // Phase->1 byte,
        // Response Status->1 byte,
        // Message Type->1 byte,
        // Payload Size->4 byte,
        // Payload->n byte,
        // Epoch->8 byte
        // Checksum->8 byte
        DataBlock dataBlock = response.getDataBlock();
        String partitionName = dataBlock.getPartitionName();
        String message = dataBlock.getMessage();

        String data = constructData(partitionName, message);

        int dataLength = data.length();
        byte[] payload = data.getBytes();
        long checksum = HashProducer.calculateHash(payload);
        long epoch = dataBlock.getCreatedDateTime().toEpochSecond(ZoneOffset.UTC);

        ByteBuffer buffer = ByteBuffer.allocate(1 + 1 + 1 + 4 + dataLength + 8 + 8);

        buffer.put(response.getPhase()); // Phase
        buffer.put(response.getResponseStatus()); // Response Status
        buffer.put(dataBlock.getDataType()); // Message Type
        buffer.putInt(dataLength); // Message Length
        buffer.put(payload); // Payload
        buffer.putLong(epoch); // Epoch
        buffer.putLong(checksum); // Checksum
        buffer.flip();
        return buffer;
    }


    @Override
    public ORequest extract(ByteBuffer byteBuffer) throws IOException {
        // Phase->1 byte,
        // Message Type->1 byte,
        // Payload Size->4 byte,
        // Payload->n byte,
        // Token->16 byte,
        // Epoch->8 byte
        // Checksum->8 byte

        System.out.println(byteBuffer.array().length);
        byte dataType = byteBuffer.get();

        byte[] size = new byte[4];
        byteBuffer.get(size, 0, size.length);
        int messageLength = ByteBuffer.wrap(size).getInt();

        byte[] payload = new byte[messageLength];
        byteBuffer.get(payload, 0, messageLength);

        byte[] token = new byte[16];
        byteBuffer.get(token, 0, token.length);
        System.out.println(new String(token, StandardCharsets.UTF_8));
        long epoch = byteBuffer.getLong();

        long receivedChecksum = byteBuffer.getLong();
        long calculatedChecksum = HashProducer.calculateHash(payload);
        boolean isCompatible = (receivedChecksum == calculatedChecksum);

        String data = new String(payload, StandardCharsets.UTF_8);
        String tkn = new String(token, StandardCharsets.UTF_8);
        String[] strings = extractData(data);
        String partitionName = strings[0];
        String message = strings[1];
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC);
        DataBlock dataBlock = new DataBlock(message, partitionName);
        dataBlock.setCreatedDateTime(dateTime);
        dataBlock.setDataType(dataType);
        ORequest.RequestBuilder requestBuilder = new ORequest.RequestBuilder(Constants.DATA_PHASE);
        requestBuilder.setDataBlock(dataBlock).setToken(tkn).setChecksumValid(isCompatible);
        return requestBuilder.build();
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
        data = data.trim();
        int ind = data.indexOf("#");
        String str1 = data.substring(0,ind);
        int lng =Integer.parseInt(str1);
        String partitionName = data.substring(ind+1, ind+1+lng);
        String message = data.substring(ind+1+lng);
        return new String[]{partitionName, message};
    }
}
