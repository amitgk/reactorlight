package core;

/**
 * Created by ak on 20/06/2017.
 */
public interface IProducer<T extends Pair<Class<?>,Object>> {
    void start();
    void stop();
}
