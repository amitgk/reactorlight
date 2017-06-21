package Samples;

import core.IProducer;
import core.Pair;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by ak on 21/06/2017.
 */
public class Producer1<T  extends Pair<Class<?>,Object>> implements IProducer<T> {
    private Consumer<T> insertingConsumer;
    private ExecutorService es;
    public Producer1(Consumer<T> consumer, ExecutorService es){
        this.insertingConsumer = consumer;
        this.es = es;
    }
    @Override
    public void start() {
        es.submit(() -> {
            Stream.generate(() -> new Pair(String.class,"Producer2 - msg"))
                    .limit(1000000).forEach(t -> insertingConsumer.accept((T)t));

        });

    }

    @Override
    public void stop() {
        //stop the producer
    }
}
