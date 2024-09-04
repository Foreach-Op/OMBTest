package Network.Server.Phase;

import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Phase {
    private final PhaseStrategy phaseStrategy;

    public Phase(PhaseStrategy phaseStrategy){
        this.phaseStrategy = phaseStrategy;
    }

    public OResponse executePhase(ORequest request, SocketChannel socketChannel, Selector selector) throws IOException {
        return phaseStrategy.execute(request, socketChannel, selector);
    }
}
