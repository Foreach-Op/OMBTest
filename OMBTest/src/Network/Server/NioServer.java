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
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    public void start(final String host, final int portNumber) throws IOException{
        try(ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            Selector selector = Selector.open()) {
            serverSocketChannel.configureBlocking(false);
            InetSocketAddress hostAddress = new InetSocketAddress(host, portNumber);
            serverSocketChannel.bind(hostAddress);
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
                        read(selector, key);
                    }
                    try {
                        if (key.isWritable()) {
                            write(selector, key);
                        }
                    }catch (Exception e){
                        System.out.println(e.getMessage());
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
            selector.selectedKeys().remove(key);
            System.out.println("Connection closed by client");
            return;
        }
        buffer.flip();

        Protocol readProtocol = ProtocolHandler.getProtocol(buffer);
        ORequest oRequest1 = readProtocol.getRequest(buffer);
        OResponse response = PhaseHandler.execute(oRequest1, client, selector);
        if(response.getPhase() == Constants.DATA_PHASE && response.getUserType() == Constants.CONSUMER)
            return;
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
            responseBuilder.setDataBlock(dataBlock);
            OResponse response = responseBuilder.build();
            dataConveyingProtocol.sendResponse(response, client);
        }
        // client.register(selector, SelectionKey.OP_READ);
    }
}
