package Network.Server.Phase;

import Network.Useful.Constants;
import Network.Useful.ORequest;

import java.io.IOException;

public class PhaseStrategyFactory {

    public static PhaseStrategy getPhaseStrategy(ORequest request) throws IOException {
        byte phaseEnum = request.getPhase();
        if(phaseEnum == Constants.AUTHENTICATION_PHASE)
            return new LogInPhaseStrategy();
        return new LogInPhaseStrategy();
    }
}
