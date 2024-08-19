package Produce;

import Broker.DataBlock;
import Consume.ConsumerPipe;

import java.util.ArrayList;
import java.util.List;

public class ProducerPipeManager implements Runnable{
    private final List<ProducerPipe> producerPipeList;

    public ProducerPipeManager(){
        this.producerPipeList = new ArrayList<>();
    }

    public void submitProducerPipe(ProducerPipe producerPipe){
        producerPipeList.add(producerPipe);
    }

    @Override
    public void run() {
        for (ProducerPipe producerPipe : producerPipeList) {
            //producerPipe.produce(new DataBlock(producerPipe.getProducer().getName()));
        }
    }
}
