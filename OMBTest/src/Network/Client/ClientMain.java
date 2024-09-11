package Network.Client;

import Broker.DataBlock;

import java.util.List;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        String username = "oguz";
        String password = "oguz";
        String host = "localhost";
        //String host = "192.168.1.109";
        int port = 12345;

        try(Scanner scanner = new Scanner(System.in)){
            System.out.println("Consumer or Producer?");
            String res = scanner.nextLine();
            if (res.equalsIgnoreCase("c")) {
                System.out.println("Consumer Client");
                ConsumerClient consumerClient = startConsumerClient(username, password, host, port);
                consumerClient.start();
                // consumerClient.connect(host, port);
                // consumerClient.startListening();
                while (true){
                    DataBlock dataBlock1 = consumerClient.receiveData("channel1");
                    //DataBlock dataBlock2 = consumerClient.getDataBlock("channel2");
                    if(dataBlock1 == null){
                        continue;
                    }
                    System.out.println(dataBlock1);
                }
            } else if(res.equalsIgnoreCase("p")){
                System.out.println("Producer Client");
                ProducerClient producerClient = startProducerClient(username, password, host, port);
                producerClient.start();

                while (true){
                    String message = scanner.nextLine();
                    producerClient.sendMessage(message);
                }
            }
        }
    }

    public static ConsumerClient startConsumerClient(String username, String password, String host, int port){
        return new ConsumerClient(username, password, host, port, List.of("channel1", "channel2"));
    }

    public static ProducerClient startProducerClient(String username, String password, String host, int port){
        return new ProducerClient(username, password, host, port, List.of("channel1", "channel2", "channel3"));

    }
}
