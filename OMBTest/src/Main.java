import Broker.DataBlock;
import Network.Client.ClassicClientC;
import Network.Client.ClassicClientP;
import Network.Client.NioClient;
import Network.Server.NioServer;
import Network.Useful.ORequest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.CRC32;

public class Main {

//    private static Producer dummyproducer = new Producer("Ali","",1);
//    private static Producer dummyproducer2 = new Producer("Veli","",1);
//    private static Consumer dummyConsumer = new Consumer("Mehmet");
//    private static Partition dummyPartition = new Partition("test");

    public static void main(String[] args) {
        //startProducing();
        //startConsuming();
        StringBuilder sb = new StringBuilder();
        List<String> partitionNames = new ArrayList<>();
        partitionNames.add("channelchannel");
        partitionNames.add("oguzogzu");
        String partitionName = "channelchannel";
        String data = "testdata";
        String msg = constructData(partitionNames, data);
        System.out.println(msg);
        extractData(msg);
        int portNumber = 12345;

        try(Scanner scanner = new Scanner(System.in)){
            System.out.println("Is this a server?");
            String res = scanner.nextLine();
            if(res.equalsIgnoreCase("y")){
                System.out.println("NIO Server");
                startServer(portNumber);
            } else if (res.equalsIgnoreCase("c")) {
                System.out.println("Consumer Client");
                startConsumerClient(portNumber);
            } else if(res.equalsIgnoreCase("p")){
                System.out.println("Producer Client");
                startProducerClient(portNumber);
            }
        }
//        ByteBuffer buffer = constructMessage(new ORequest.RequestBuilder((byte) 3, (byte) 4).setMessage("HelloWorld").build());
//        System.out.println("ByteBuffer contents:");
//
//        while (buffer.hasRemaining()) {
//            System.out.print(buffer.get() + " ");
//        }
        System.out.println("------");
        // System.out.println(buffer.get());
//        String encoded = encode("Hello World".split(" "));
//        System.out.println(encoded);
//        String[] decoded = decode(encoded);
//        for(String d: decoded)
//            System.out.println(d);
    }

    public static String constructData(List<String> partitionNames, String message){
        StringBuilder sb = new StringBuilder();

        for (String partitionName: partitionNames){
            sb.append("%d#".formatted(partitionName.length()));
            sb.append(partitionName);
        }
        sb.append(" ");
        sb.append(message);
        sb.append("\n");
        return sb.toString();
    }

    public static String[] extractData(String data){
        int firstSpaceInd = data.indexOf(" ");
        String firstSub = data.substring(0, firstSpaceInd);
        List<String> partitions = new ArrayList<>();
        int lastInd = 0;
        while (true){
            int ind = firstSub.indexOf("#", lastInd+1);
            if(ind == -1)
                break;
            String str1 = firstSub.substring(0, ind);
            int lng =Integer.parseInt(str1);
            String partitionName = firstSub.substring(ind+1, ind+1+lng);
            firstSub = firstSub.substring(ind+1+lng);
            partitions.add(partitionName);
        }
        String message = data.substring(firstSpaceInd+1);
        partitions.forEach(System.out::println);
        System.out.println(message);
        return new String[]{"partitionName", message};
    }

    public static String encode(String[] data){
        StringBuilder sb = new StringBuilder();
        for (String curr : data) {
            sb.append(curr.length());
            sb.append("#");
            sb.append(curr);
        }
        return sb.toString();
    }

    public static String[] decode(String data){
        List<String> lst = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int len = 0;
        boolean isStarted = false;
        for (int i = 0; i < data.length(); i++) {
            char curr = data.charAt(i);

            if(curr == '#' && !isStarted){
                System.out.println(sb.length());
                len = Integer.parseInt(sb.toString());
                System.out.println("len "+len);
                isStarted = true;
                sb = new StringBuilder(len);
            } else if (!isStarted) {
                sb.append(curr);
            }else {
                sb.append(curr);
                len--;
            }
            if(len == 0 && isStarted) {
                lst.add(sb.toString());
                System.out.println("Added:"+sb);
                isStarted = false;
                sb = new StringBuilder();
            }
        }
        return lst.toArray(new String[0]);
    }

    public static ByteBuffer constructMessage(ORequest request) {
        int messageLength = request.getMessage().length();
        long checksum = calculateCRC32(request.getMessage().getBytes());
        ByteBuffer buffer = ByteBuffer.allocate(1 + 1 + 4 + messageLength + 8);

        buffer.put(request.getPhase()); // Phase
        buffer.put(request.getUserType()); // Request Type
        buffer.putInt(messageLength); // Message Length
        buffer.put(request.getMessage().getBytes()); // Payload
        buffer.putLong(checksum); // Checksum
        buffer.flip();
        return buffer;
    }

    public static long calculateCRC32(byte[] bytes) {
        // 8 bytes
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
    }

    public static void startServer(int portNumber){
        try {
            new NioServer().start(portNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startClient(int portNumber, Scanner scanner){
        new NioClient().start(portNumber, scanner);
    }

    public static void startConsumerClient(int portNumber){
        new ClassicClientC().start(portNumber);
    }

    public static void startProducerClient(int portNumber){
        new ClassicClientP().start(portNumber);
    }

//    public static void startProducing(){
//        ExecutorService executorService = Executors.newFixedThreadPool(2);
//        ProducerPipe producerPipe = new ProducerPipe(dummyproducer, dummyPartition);
//        ProducerPipe producerPipe2 = new ProducerPipe(dummyproducer2, dummyPartition);
//        executorService.submit(()->{
//            while (true){
//                DataBlock dataBlock = new DataBlock(producerPipe.getProducer().getName());
//                System.out.println("producerPipe1 produced: "+ dataBlock.getDateTime());
//                producerPipe.produce(dataBlock);
//                Thread.sleep(1000);
//            }
//        });
//        executorService.submit(()->{
//            while (true){
//                DataBlock dataBlock = new DataBlock(producerPipe2.getProducer().getName());
//                System.out.println("producerPipe2 produced: "+ dataBlock.getDateTime());
//                producerPipe2.produce(dataBlock);
//                Thread.sleep(3000);
//            }
//        });
//
//        executorService.shutdown();
//    }
//
//    public static void startConsuming(){
//        ConsumerPipe consumerPipe = new ConsumerPipe(dummyConsumer, dummyPartition, ConsumingMethod.QUEUE);
//        ConsumerPipe consumerPipe2 = new ConsumerPipe(dummyConsumer, dummyPartition, ConsumingMethod.QUEUE);
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//        ConsumerPipeManager consumerPipeManager = new ConsumerPipeManager();
//        consumerPipeManager.submitConsumerPipe(consumerPipe);
//        consumerPipeManager.submitConsumerPipe(consumerPipe2);
//        scheduler.scheduleAtFixedRate(consumerPipeManager, 1, 5, TimeUnit.SECONDS);
//
//    }
}