package Network.Server.Phase;

import Consume.Consumer;
import Consume.ConsumerManager;
import Network.Useful.Constants;
import Network.Useful.ORequest;
import Produce.Producer;
import Security.AuthenticationManager;
import Security.User;

import java.nio.channels.SocketChannel;

public class LogInPhaseStrategy implements PhaseStrategy {
    @Override
    public void execute(ORequest request) {
        String msg = request.getMessage();
        String[] arr = msg.split(" ");
        String username = arr[0];
        String password = arr[1];
        AuthenticationManager authenticationManager = new AuthenticationManager();
        boolean isAuthenticated = authenticationManager.checkUsernamePassword(username, password);
        if(!isAuthenticated)
            throw new RuntimeException("Username or password is invalid");
        byte userType = request.getRequestType();
        handleUser(userType, username, request.getSocketChannel());
    }

    private void handleUser(byte userType, String username, SocketChannel socketChannel){
        if(userType == Constants.PRODUCER){
            handleProducer(username);
        } else if (userType == Constants.CONSUMER) {
            handleConsumer(username, socketChannel);
        }
    }

    private void handleProducer(String username){
        Producer producer = new Producer(username);
        // ConsumerManager.getInstance().addConsumer(consumer.getToken(), consumer);
    }

    private void handleConsumer(String username, SocketChannel socketChannel){
        Consumer consumer = new Consumer(username, socketChannel);
        try {
            ConsumerManager.getInstance().addConsumer(consumer.getToken(), consumer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
