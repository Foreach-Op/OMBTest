package Network.Server.Phase;



import Network.Useful.ORequest;

import java.io.IOException;

public class PhaseHandler {
    private PhaseHandler(){}

    public static void execute(ORequest request) throws IOException {
        if(!request.isChecksumValid()){
            System.out.println("Not Valid");
        }
        PhaseStrategy phaseStrategy = PhaseStrategyFactory.getPhaseStrategy(request);
        Phase phase = new Phase(phaseStrategy);
        phase.executePhase(request);
    }
}
