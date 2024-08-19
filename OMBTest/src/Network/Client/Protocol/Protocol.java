package Network.Client.Protocol;

import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;


public abstract class Protocol {

    protected DataInputStream input;
    protected DataOutputStream output;
    protected Constants constants = Constants.getInstance();

    public Protocol() {
        this.input = null;
        this.output = null;
    }

    public final void sendRequest(ORequest request, OutputStream outputStream) throws IOException {
        ByteBuffer payload = wrap(request);
        byte[] byteArray = new byte[payload.remaining()];
        payload.get(byteArray);
        outputStream.write(byteArray);
        outputStream.flush();
        // output.write(null,0, 0);
    }

    public final OResponse getResponse(InputStream inputStream) throws Exception {
        return extract(inputStream);
    }

    public long calculateCRC32(byte[] bytes) {
        // 8 bytes
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
    }

    protected abstract ByteBuffer wrap(ORequest request);
    protected abstract OResponse extract(InputStream inputStream) throws IOException;
}
