package Network.Server.Phase;

import Consume.Consumer;
import Consume.ConsumerManager;
import Login.LoginHandler;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Network.Useful.OResponse;
import Produce.Producer;
import Produce.ProducerManager;
import Security.Authentication.AuthenticationManager;
import Security.User;
import Security.UserManager;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class LogInPhaseStrategy implements PhaseStrategy {
    private final LoginHandler loginHandler = new LoginHandler();

    @Override
    public OResponse execute(ORequest request, SocketChannel socketChannel, Selector selector) {
        System.out.println("LogInPhaseStrategy");
        OResponse.ResponseBuilder responseBuilder = new OResponse.ResponseBuilder(Constants.AUTHENTICATION_PHASE);
        byte userType = request.getUserType();
        String msg = request.getMessage();
        String[] arr = msg.split(" ");
        String username = arr[0];
        String password = arr[1];
        AuthenticationManager authenticationManager = new AuthenticationManager();
        boolean isAuthenticated = authenticationManager.checkUsernamePassword(username, password);
        responseBuilder.setUserType(userType);
        if(!isAuthenticated){
            responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_AUTHENTICATION_ERROR).setMessage("Username or password is invalid");
            return responseBuilder.build();
        }
        User user = null;
        try {
            user = loginHandler.handleLogin(userType, username, socketChannel);
            // user = handleUser(userType, username);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_AUTHENTICATION_ERROR).setMessage(e.getMessage());
            return responseBuilder.build();
        }
        responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_SUCCESS).setMessage(user.getToken());
        System.out.println("Token:" + user.getToken());
        return responseBuilder.build();
    }
}
