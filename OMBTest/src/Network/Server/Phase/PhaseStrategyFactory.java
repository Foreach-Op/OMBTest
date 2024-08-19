package Network.Server.Phase;

import Network.Useful.PhaseEnum;

import java.io.IOException;

public class PhaseStrategyFactory {

//    public static PhaseStrategy getPhaseStrategy(Connection connection) throws IOException {
//        PhaseControl phaseControl = new PhaseControl();
//        PhaseEnum phaseEnum = phaseControl.checkPhase(connection.getInputStream());
//        // Protocol protocol = ProtocolFactory.getProtocol(phaseEnum, connection);
//        if(phaseEnum == PhaseEnum.Authentication)
//            return new LogInPhaseStrategy(connection);
//        else if(phaseEnum == PhaseEnum.Signup)
//            return new SignUpPhaseStrategy(connection);
//        else if(phaseEnum == PhaseEnum.Cmd)
//            return new CmdPhaseStrategy(connection);
//        return new LogInPhaseStrategy(connection);
//    }
}
