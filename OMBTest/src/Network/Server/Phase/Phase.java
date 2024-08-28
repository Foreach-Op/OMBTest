package Network.Server.Phase;

import Network.Useful.ORequest;
import Network.Useful.OResponse;

import java.io.IOException;

public class Phase {
    private final PhaseStrategy phaseStrategy;

    public Phase(PhaseStrategy phaseStrategy){
        this.phaseStrategy = phaseStrategy;
    }

    public OResponse executePhase(ORequest request) throws IOException {
        return phaseStrategy.execute(request);
    }
}
