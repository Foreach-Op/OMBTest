package Network.Server.Phase;

import Consume.Consumer;
import Consume.ConsumerManager;
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
    private SocketChannel socketChannel;
    private Selector selector;
    @Override
    public OResponse execute(ORequest request, SocketChannel socketChannel, Selector selector) {
        System.out.println("LogInPhaseStrategy");
        this.socketChannel = socketChannel;
        this.selector = selector;
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
            user = handleUser(userType, username);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_AUTHENTICATION_ERROR).setMessage(e.getMessage());
            return responseBuilder.build();
        }
        responseBuilder.setResponseStatus(Constants.RESPONSE_STATUS_SUCCESS).setMessage(user.getToken());
        System.out.println("Token:" + user.getToken());
        return responseBuilder.build();
    }

    private User handleUser(byte userType, String username) throws Exception {
        User user = null;
        if(userType == Constants.PRODUCER){
            user = handleProducer(username);
        } else if (userType == Constants.CONSUMER) {
            user = handleConsumer(username);
        }
        UserManager.saveUser(user.getToken(), user);
        return user;
    }

    private User handleProducer(String username) throws Exception {
        Producer producer = new Producer(username, socketChannel);
        ProducerManager.getInstance().addProducer(producer.getToken(), producer);
        return producer;
    }

    private User handleConsumer(String username) throws Exception {
        Consumer consumer = new Consumer(username, socketChannel);
        ConsumerManager.getInstance().addConsumer(consumer.getToken(), consumer);
        System.out.println("Consumer Added");
        System.out.println(ConsumerManager.getInstance().getConsumer(consumer.getToken()));
        return consumer;
    }
}
