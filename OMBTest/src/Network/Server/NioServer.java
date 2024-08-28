package Network.Server;

import Network.Server.Protocol.AuthenticationProtocol;
import Network.Server.Protocol.Protocol;
import Network.Server.Protocol.ProtocolHandler;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;
import Security.User;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.HashSet;

public class NioServer {
    private HashSet<SocketChannel> clients = new HashSet<>();
    private HashMap<User, SocketChannel> userSocketChannelMap = new HashMap<>();
    private HashMap<String, User> userHashMap = new HashMap<>();
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    public void start(final int portNumber) throws IOException{
        try(ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            Selector selector = Selector.open()) {
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(portNumber));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true){
                if(selector.select() == 0)
                    continue;

                for(SelectionKey key : selector.selectedKeys()){
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()){
                        accept(selector, key);
                    } else if (key.isReadable()) {
                        read(selector, key);
                    } else if (key.isWritable()) {
                        write(selector, key);
                    }
                }
                selector.selectedKeys().clear();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            for (SocketChannel client : clients){
                try {
                    client.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        SocketChannel client = channel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        // client.register(selector, SelectionKey.OP_WRITE);
        clients.add(client);
        System.out.println("Accepted connection from: " + client.getRemoteAddress());
    }

    private void read(Selector selector, SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();
        int bytesRead = client.read(buffer);
        if (bytesRead == -1) {
            client.close();
            clients.remove(client);
            System.out.println("Connection closed by client");
            return;
        }
        buffer.flip();

//        Protocol protocol = new AuthenticationProtocol();
//        ORequest oRequest = protocol.getRequest(buffer);
//        String receivedMessage = oRequest.getMessage();
        Protocol protocol1 = ProtocolHandler.getProtocol(buffer);
        ORequest oRequest1 = protocol1.getRequest(buffer);
        oRequest1.setSocketChannel(client);
        String receivedMessage = oRequest1.getMessage();
        System.out.println("Received message: " + receivedMessage);
//        OResponse response=new OResponse.ResponseBuilder(Constants.CMD_PHASE, Constants.AUTH_FAIL, Constants.AUTH_SUCCESS).setMessage("token").build();
//        protocol.sendResponse(response,client);
        client.register(selector, SelectionKey.OP_WRITE);

    }

    private void write(Selector selector, SelectionKey key) throws IOException{
        SocketChannel client = (SocketChannel) key.channel();
        System.out.println("Server message: ");
        Protocol protocol = new AuthenticationProtocol();
        OResponse response=new OResponse.ResponseBuilder(Constants.CMD_PHASE, Constants.AUTH_FAIL, Constants.AUTH_SUCCESS).setMessage("token").build();
        protocol.sendResponse(response,client);
//        for (int i = 0; i < 100; i++) {
//            OResponse response=new OResponse.ResponseBuilder(Constants.CMD_PHASE, Constants.AUTH_FAIL, Constants.AUTH_SUCCESS).setMessage("token").build();
//            protocol.sendResponse(response,client);
//        }
        client.register(selector, SelectionKey.OP_READ);
    }
}
