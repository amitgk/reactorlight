package core;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by ak on 20/06/2017.
 */
public class Reactor<T extends Pair<Class<?>,Object>> {
    private List<IProducer> producers = new LinkedList<IProducer>();
    private BlockingQueue<T> queue = new LinkedBlockingQueue<T>(10000);
    private Function<T,T>  eventProcessor;
    public Reactor<T> producer(IProducer producer){
        this.producers.add(producer);
        return this;
    }

    public Reactor<T> eventProcessor(Function<T,T> eventProcessor){
        this.eventProcessor = eventProcessor;
        return this;
    }

    public Consumer<T> getInsertingConsumer(){
        return (T t) -> {

            //Backpressure.
            boolean added = false;
            while(!added) {
                try {
                   added = queue.offer(t, 25, TimeUnit.MILLISECONDS);
                   if(!added){
                       System.out.println("Queue full...waiting");
                   }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void start(){
        producers.forEach(p -> p.start());

        Executors.newSingleThreadExecutor().submit(() -> {
           while(true){

               T msg = queue.take();
               eventProcessor.apply(msg);
           }
        });

    }
}
