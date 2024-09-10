package Login;

import Produce.Producer;
import Produce.ProducerManager;
import Security.User;
import Security.UserManager;

import java.nio.channels.SocketChannel;

public class ProducerLoginProcess implements Loggable {
    @Override
    public User login(String username, SocketChannel socketChannel) throws Exception {
        Producer producer = new Producer(username, socketChannel);
        UserManager.saveUser(producer.getToken(), producer);
        ProducerManager.getInstance().addProducer(producer.getToken(), producer);
        return producer;
    }
}
