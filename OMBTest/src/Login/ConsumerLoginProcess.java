package Login;

import Consume.Consumer;
import Consume.ConsumerManager;
import Consume.SocketConsumerManager;
import Security.User;
import Security.UserManager;

import java.nio.channels.SocketChannel;

public class ConsumerLoginProcess implements Loggable {
    @Override
    public User login(String username, SocketChannel socketChannel) throws Exception {
        Consumer consumer = new Consumer(username);
        SocketConsumerManager.getInstance().addSocketConsumer(socketChannel, consumer);
        ConsumerManager.getInstance().addConsumer(consumer.getToken(), consumer);
        UserManager.saveUser(consumer.getToken(), consumer);
        System.out.println("Consumer Added");
        System.out.println(ConsumerManager.getInstance().getConsumer(consumer.getToken()));
        return consumer;
    }
}
