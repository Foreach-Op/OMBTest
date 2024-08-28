package Network.Server.Phase;

import Network.Useful.ORequest;
import Network.Useful.OResponse;

public interface PhaseStrategy {
    OResponse execute(ORequest request);
}
