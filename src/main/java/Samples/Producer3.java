package Samples;

import core.IProducer;
import core.Pair;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by ak on 21/06/2017.
 */
public class Producer3<T  extends Pair<Class<?>,Object>> implements IProducer<T> {
    private Consumer<T> insertingConsumer;
    private ExecutorService es;
    public Producer3(Consumer<T> consumer, ExecutorService es){

        this.insertingConsumer = consumer;
        this.es = es;
    }
    @Override
    public void start() {
        es.submit(() -> {
            Stream.generate(() -> new Pair(Double.class,1.24))
                    .limit(1000000).forEach(t -> insertingConsumer.accept((T)t));

        });

    }

    @Override
    public void stop() {
        return;
    }
}
