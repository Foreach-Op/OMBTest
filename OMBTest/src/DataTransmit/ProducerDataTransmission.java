package DataTransmit;

import Broker.DataBlock;
import Produce.Producer;
import Produce.ProducerManager;
import Produce.ProducerPipe;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ProducerDataTransmission implements DataTransmittable{
    @Override
    public void transmitData(String token, DataBlock dataBlock, SocketChannel socketChannel, Selector selector) throws Exception {
        Producer producer = ProducerManager.getInstance().getProducer(token);
        ProducerPipe pipe = producer.getPipe(dataBlock.getPartitionName());
        pipe.produce(dataBlock);
        System.out.println("Produced: "+dataBlock);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }
}
