package Network.Server;

import Broker.DataBlock;
import Consume.*;
import Network.Server.Phase.PhaseHandler;
import Network.Server.Protocol.DataConveyingProtocol;
import Network.Server.Protocol.Protocol;
import Network.Server.Protocol.ProtocolHandler;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;
import Produce.ProducerPipe;
import Produce.ProducerPipeManager;
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
import java.util.List;
import java.util.Map;

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
                        System.out.println("Not Valid");
                        continue;
                    }
                    if (key.isAcceptable()){
                        accept(selector, key);
                    }
                    if (key.isReadable()) {
                        System.out.println("Reading");
                        read(selector, key);
                    }
                    if (key.isWritable()) {
                        System.out.println("Writing");
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

        Protocol readProtocol = ProtocolHandler.getProtocol(buffer);
        ORequest oRequest1 = readProtocol.getRequest(buffer);
        oRequest1.setSocketChannel(client);
        oRequest1.setSelector(selector);
        String receivedMessage = oRequest1.getMessage();
        System.out.println("Received message: " + receivedMessage);
        OResponse response = PhaseHandler.execute(oRequest1);
        Protocol writeProtocol = ProtocolHandler.getProtocol(response);
        writeProtocol.sendResponse(response, client);

        // client.register(selector, SelectionKey.OP_READ);

    }

    private void write(Selector selector, SelectionKey key) throws IOException{
        SocketChannel client = (SocketChannel) key.channel();

        Protocol dataConveyingProtocol = new DataConveyingProtocol();

        DataBlock[] dataBlocks = SocketConsumerManager.getInstance().getData(client, 10);

        for (DataBlock dataBlock : dataBlocks) {
            OResponse.ResponseBuilder responseBuilder = new OResponse.ResponseBuilder(Constants.DATA_PHASE);
            responseBuilder.setMessage(dataBlock.getData());
            OResponse response = responseBuilder.build();
            dataConveyingProtocol.sendResponse(response, client);
        }
        //OResponse response=new OResponse.ResponseBuilder(Constants.CMD_PHASE, Constants.AUTH_FAIL, Constants.AUTH_SUCCESS).setMessage("token").build();
        //protocol.sendResponse(response,client);
//        for (int i = 0; i < 100; i++) {
//            OResponse response=new OResponse.ResponseBuilder(Constants.CMD_PHASE, Constants.AUTH_FAIL, Constants.AUTH_SUCCESS).setMessage("token").build();
//            protocol.sendResponse(response,client);
//        }
        client.register(selector, SelectionKey.OP_READ);
    }
}
