# reactorlight
Lightweight reactor framework

A framework to process events/messages in a single thread. Provides support for multiple producers. Each producer writes to a common blocking queue.
The messages from the blocking queue are passed to an eventprocessor in a single thread.

1. Define a reactor.
        Reactor<Pair<Class<?>,Object>> reactor = new Reactor<>();
        
        The Pair<Class<?>,Object> allows any type of message to be sent to the event processor. (Refer to type erasure in java for more information)
2. Define a producer
  public class Producer1<T  extends Pair<Class<?>,Object>> implements IProducer<T> {
    private Consumer<T> insertingConsumer;
    private ExecutorService es;
    
    public Producer1(Consumer<T> consumer, ExecutorService es){
        this.insertingConsumer = consumer;
        this.es = es;
    }
    @Override
    public void start() {
       //Generate a million messages
        es.submit(() -> {
            Stream.generate(() -> new Pair(String.class,"Producer2 - msg"))
                    .limit(1000000).forEach(t -> insertingConsumer.accept((T)t));

        });

    }

3. Add producers to the reactor. 
   ExecutorService es = Executors.newCachedThreadPool();
   
          reactor.producer(new Producer1(reactor.getInsertingConsumer(),es))
                .producer(new Producer2(reactor.getInsertingConsumer(),es))
                .producer(new Producer3(reactor.getInsertingConsumer(),es))
 
4. Define the EventProcessor
        Function<Pair<Class<?>,Object>,Pair<Class<?>,Object>> eventProcessor = (item) -> {

            //System.out.println("Received item of type:"+item.getLeft());
            //System.out.println("Processing item "+item.getLeft().cast(item.getRight()));
            perfMonitor.accept(item);
            return item;
        };

5. Add the eventprocessor to the reactor and start it.
        reactor.producer(new Producer1(reactor.getInsertingConsumer(),es))
                .producer(new Producer2(reactor.getInsertingConsumer(),es))
                .producer(new Producer3(reactor.getInsertingConsumer(),es))
                .eventProcessor(eventProcessor)
                .start();
