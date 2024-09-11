package Produce;

import Network.Useful.HashProducer;
import Security.User;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Producer extends User {

    private final Set<ProducerPipe> producerPipes;
    public Producer(String username) {
        super(username,"P");
        producerPipes = new HashSet<>();
    }

    public Set<ProducerPipe> getProducerPipes(){
        return this.producerPipes;
    }

    public void addProducerPipe(ProducerPipe producerPipe){
        producerPipes.add(producerPipe);
    }

    public ProducerPipe getPipe(String partitionName){
        return producerPipes.stream().filter(s->s.getPartition().getName().equals(partitionName)).toList().getFirst();
    }
}
