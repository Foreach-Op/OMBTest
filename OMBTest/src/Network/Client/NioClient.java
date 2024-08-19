package Network.Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NioClient {
    public void start(final int portNumber, final Scanner scanner){
        try(SocketChannel serverChannel = SocketChannel.open()){
            serverChannel.connect(new InetSocketAddress(portNumber));
            System.out.println("Connection Established!");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true){
                String line = scanner.nextLine();
                if(line.equalsIgnoreCase("quit"))
                    break;
                line += System.lineSeparator();
                buffer.clear().put(line.getBytes()).flip();
                while (buffer.hasRemaining()){
                    serverChannel.write(buffer);
                }
                buffer.clear();
                int bytesRead = serverChannel.read(buffer);
                if(bytesRead > 0){
                    buffer.flip();
                    String receivedMessage = new String(buffer.array(), buffer.position(), bytesRead).trim();
                    System.out.println("Received from server: " + receivedMessage);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
