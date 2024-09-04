package Network.Server.Phase;

import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public interface PhaseStrategy {
    OResponse execute(ORequest request, SocketChannel socketChannel, Selector selector);
}
