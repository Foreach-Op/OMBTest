package Network.Server.Phase;

import Network.Useful.ORequest;

public interface PhaseStrategy {
    void execute(ORequest request);
}
