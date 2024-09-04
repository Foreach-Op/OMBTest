package Network.Client;

import Broker.DataBlock;

import java.util.List;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        String username = "admin";
        String password = "admin";
        String host = "localhost";
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
                    DataBlock dataBlock1 = consumerClient.getDataBlock("channel1");
                    //DataBlock dataBlock2 = consumerClient.getDataBlock("channel2");
                    if(dataBlock1 == null){
                        continue;
                    }
                    System.out.println(dataBlock1);
                    //System.out.println(dataBlock2);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
                }
            } else if(res.equalsIgnoreCase("p")){
                System.out.println("Producer Client");
                ProducerClient producerClient = startProducerClient(username, password, host, port);
                producerClient.start();
                //producerClient.connect(host, port);
                int i = 0;
                while (true){
                    String message = scanner.nextLine();
                    producerClient.startSending(message);
                    i++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static ConsumerClient startConsumerClient(String username, String password, String host, int port){
        return new ConsumerClient(username, password, host, port, List.of("channel1", "channel2"));
    }

    public static ProducerClient startProducerClient(String username, String password, String host, int port){
        return new ProducerClient(username, password, host, port, List.of("channel1", "channel2"));

    }
}
