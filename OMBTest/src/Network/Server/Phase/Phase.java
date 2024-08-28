package Network.Server.Phase;

import Network.Useful.ORequest;

import java.io.IOException;

public class Phase {
    private final PhaseStrategy phaseStrategy;

    public Phase(PhaseStrategy phaseStrategy){
        this.phaseStrategy = phaseStrategy;
    }

    public void executePhase(ORequest request) throws IOException {
        phaseStrategy.execute(request);
    }
}
