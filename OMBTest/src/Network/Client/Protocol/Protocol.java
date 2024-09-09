package Network.Client.Protocol;

import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;


public abstract class Protocol {

    public final void sendRequest(ORequest request, OutputStream outputStream) throws IOException {
        ByteBuffer payload = wrap(request);
        byte[] byteArray = new byte[payload.remaining()];
        payload.get(byteArray);
        outputStream.write(byteArray);
        outputStream.flush();
    }

    public final OResponse getResponse(InputStream inputStream) throws Exception {
        return extract(inputStream);
    }

    protected abstract ByteBuffer wrap(ORequest request);
    protected abstract OResponse extract(InputStream inputStream) throws IOException;
}
