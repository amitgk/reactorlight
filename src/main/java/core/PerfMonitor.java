package core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * Created by ak on 21/06/2017.
 */
public class PerfMonitor<T extends Pair<Class<?>,Object>> implements Consumer<T> {
    AtomicLong counter = new AtomicLong(0);
    Map<Class<?>,AtomicLong> counterByType = new HashMap<>();
    private long startTime;
    public PerfMonitor(){
        startTime = System.currentTimeMillis();
    }
    @Override
    public void accept(T t) {
        counter.incrementAndGet();
        if(counter.get() % 100000 == 0){
            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;
            startTime = System.currentTimeMillis();
            printstats(timeTaken);
        }
        //counterByType.computeIfPresent(t.getLeft(),(k,v)->v.incrementAndGet());
        if(counterByType.containsKey(t.getLeft())){
            counterByType.get(t.getLeft()).incrementAndGet();
        }
        counterByType.putIfAbsent(t.getLeft(),new AtomicLong(0));
    }

    private void printstats(long timeTaken){
        System.out.println("Time taken for 100K mesages:"+timeTaken+",Rate="+100000*1000/timeTaken);


        System.out.println(counterByType);
        System.out.println("Total messages:"+counter.get());
    }
}
