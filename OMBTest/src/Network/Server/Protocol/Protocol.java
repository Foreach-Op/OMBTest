package Network.Server.Protocol;

import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;


public abstract class Protocol {

    protected DataInputStream input;
    protected DataOutputStream output;
    protected Constants constants = Constants.getInstance();

    public Protocol() {
        this.input = null;
        this.output = null;
    }

    public final void sendResponse(OResponse response, SocketChannel channel) throws IOException {
        ByteBuffer message = wrap(response);
        // payload.flip();
        int res = channel.write(message);
    }

    public final ORequest getRequest(ByteBuffer byteBuffer){
        try {
            return extract(byteBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String encode(String[] data){
        StringBuilder sb = new StringBuilder();
        for (String curr : data) {
            sb.append(curr.length());
            sb.append("#");
            sb.append(curr);
        }
        return sb.toString();
    }

    public String[] decode(String data){
        List<String> lst = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int len = 0;
        boolean isStarted = false;
        for (int i = 0; i < data.length(); i++) {
            char curr = data.charAt(i);

            if(curr == '#' && !isStarted){
                len = Integer.parseInt(sb.toString());
                isStarted = true;
                sb = new StringBuilder(len);
            } else if (!isStarted) {
                sb.append(curr);
            }else {
                sb.append(curr);
                len--;
            }
            if(len == 0 && isStarted) {
                lst.add(sb.toString());
                isStarted = false;
                sb = new StringBuilder();
            }
        }
        return lst.toArray(new String[0]);
    }

    protected abstract ByteBuffer wrap(OResponse response);
    protected abstract ORequest extract(ByteBuffer byteBuffer) throws IOException;
}
