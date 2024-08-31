package Network.Server.Phase;

import Network.Useful.Constants;
import Network.Useful.ORequest;

import java.io.IOException;

public class PhaseStrategyFactory {

    public static PhaseStrategy getPhaseStrategy(ORequest request) {
        byte phaseEnum = request.getPhase();
        if(phaseEnum == Constants.AUTHENTICATION_PHASE)
            return new LogInPhaseStrategy();
        else if (phaseEnum == Constants.CHANNEL_CREATE_PHASE) {
            return new ChannelPhaseStrategy();
        } else if (phaseEnum == Constants.DATA_PHASE) {
            return new DataPhaseStrategy();
        }
        return new LogInPhaseStrategy();
    }
}
