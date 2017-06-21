package core;

import Samples.Producer1;
import Samples.Producer2;
import Samples.Producer3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by arjun on 21/06/2017.
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("Starting...");

        ExecutorService es = Executors.newCachedThreadPool();
        Reactor<Pair<Class<?>,Object>> reactor = new Reactor<>();

        PerfMonitor<Pair<Class<?>,Object>> perfMonitor = new PerfMonitor<>();
        Function<Pair<Class<?>,Object>,Pair<Class<?>,Object>> eventProcessor = (item) -> {

            System.out.println("Received item of type:"+item.getLeft());
            //System.out.println("Processing item "+item.getLeft().cast(item.getRight()));
            perfMonitor.accept(item);
            return item;
        };




        reactor.producer(new Producer1(reactor.getInsertingConsumer(),es))
                .producer(new Producer2(reactor.getInsertingConsumer(),es))
                .producer(new Producer3(reactor.getInsertingConsumer(),es))
                .eventProcessor(eventProcessor)
                .exceptionHandler(System.out::println)
                .start();


    }
}
