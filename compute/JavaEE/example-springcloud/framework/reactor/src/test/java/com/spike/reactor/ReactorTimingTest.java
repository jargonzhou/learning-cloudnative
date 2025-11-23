package com.spike.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;

/**
 * <pre>
 * @see Flux#interval(Duration)
 * @see Flux#delayElements(Duration)
 * @see Flux#delaySequence(Duration)
 * @see Flux#elapsed()
 * </pre>
 */
public class ReactorTimingTest {
    //INFO  [main] reactor.Flux.Elapsed.1 (Loggers.java:279)[info]: | onSubscribe([Fuseable] FluxElapsed.ElapsedSubscriber)
    //INFO  [main] reactor.Flux.Elapsed.1 (Loggers.java:279)[info]: | request(unbounded)
    //INFO  [parallel-1] reactor.Flux.Elapsed.1 (Loggers.java:279)[info]: | onNext([111,0])
    //INFO  [parallel-2] reactor.Flux.Elapsed.1 (Loggers.java:279)[info]: | onNext([105,1])
    //INFO  [parallel-3] reactor.Flux.Elapsed.1 (Loggers.java:279)[info]: | onNext([107,2])
    //INFO  [parallel-4] reactor.Flux.Elapsed.1 (Loggers.java:279)[info]: | onNext([108,3])
    //INFO  [parallel-5] reactor.Flux.Elapsed.1 (Loggers.java:279)[info]: | onNext([104,4])
    //INFO  [parallel-5] reactor.Flux.Elapsed.1 (Loggers.java:279)[info]: | onComplete()
    @Test
    public void elapsed() throws InterruptedException {
        Flux<Tuple2<Long, Integer>> flux = Flux.range(0, 5)
                .delayElements(Duration.ofMillis(100))
                .elapsed()
                .log();

        flux.subscribe();

        Thread.sleep(1000);
    }
}
