package Produce;

import Network.Useful.HashProducer;
import Security.User;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class Producer extends User {

    private final List<ProducerPipe> producerPipes;
    public Producer(String username) {
        super(username,"P");
        producerPipes = new ArrayList<>();
    }

    public List<ProducerPipe> getProducerPipes(){
        return this.producerPipes;
    }

    public void addProducerPipe(ProducerPipe producerPipe){
        producerPipes.add(producerPipe);
    }

    public ProducerPipe getPipe(String partitionName){
        return producerPipes.stream().filter(s->s.getPartition().getName().equals(partitionName)).toList().getFirst();
    }
}
