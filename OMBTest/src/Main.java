import Broker.DataBlock;
import Broker.Partition;
import Consume.Consumer;
import Consume.ConsumerPipe;
import Consume.ConsumerPipeManager;
import Consume.Consumption.ConsumingMethod;
import Produce.Producer;
import Produce.ProducerPipe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static Producer dummyproducer = new Producer("Ali","",1);
    private static Producer dummyproducer2 = new Producer("Veli","",1);
    private static Consumer dummyConsumer = new Consumer("Mehmet","",1);
    private static Partition dummyPartition = new Partition("test");

    public static void main(String[] args) {
        startProducing();
        startConsuming();

    }

    public static void startProducing(){
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        ProducerPipe producerPipe = new ProducerPipe(dummyproducer, dummyPartition);
        ProducerPipe producerPipe2 = new ProducerPipe(dummyproducer2, dummyPartition);
        executorService.submit(()->{
            while (true){
                DataBlock dataBlock = new DataBlock(producerPipe.getProducer().getName());
                System.out.println("producerPipe1 produced: "+ dataBlock.getDateTime());
                producerPipe.produce(dataBlock);
                Thread.sleep(1000);
            }
        });
        executorService.submit(()->{
            while (true){
                DataBlock dataBlock = new DataBlock(producerPipe2.getProducer().getName());
                System.out.println("producerPipe2 produced: "+ dataBlock.getDateTime());
                producerPipe2.produce(dataBlock);
                Thread.sleep(3000);
            }
        });

        executorService.shutdown();
    }

    public static void startConsuming(){
        ConsumerPipe consumerPipe = new ConsumerPipe(dummyConsumer, dummyPartition, ConsumingMethod.QUEUE);
        ConsumerPipe consumerPipe2 = new ConsumerPipe(dummyConsumer, dummyPartition, ConsumingMethod.QUEUE);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        ConsumerPipeManager consumerPipeManager = new ConsumerPipeManager();
        consumerPipeManager.submitConsumerPipe(consumerPipe);
        consumerPipeManager.submitConsumerPipe(consumerPipe2);
        scheduler.scheduleAtFixedRate(consumerPipeManager, 1, 5, TimeUnit.SECONDS);

    }
}