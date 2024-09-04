package Network.Server.Phase;



import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class PhaseHandler {
    private PhaseHandler(){}

    public static OResponse execute(ORequest request, SocketChannel socketChannel, Selector selector) throws IOException {
        if(!request.isChecksumValid()){
        }
        PhaseStrategy phaseStrategy = PhaseStrategyFactory.getPhaseStrategy(request);
        Phase phase = new Phase(phaseStrategy);
        return phase.executePhase(request, socketChannel, selector);
    }
}
