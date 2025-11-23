package com.spike.reactor;

import com.spike.reactor.support.Logs;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * <pre>
 * cold publishers:
 * @see Flux#defer(Supplier)
 *
 * multicast elsements of a stream
 * @see reactor.core.publisher.ConnectableFlux
 * @see Flux#publish()
 *
 * cache elements of a stream
 * @see Flux#cache()
 *
 * share elements of a stream
 * @see Flux#share()
 * </pre>
 */
public class ReactorHotAndColdTest {

    //INFO  [main] defer (ReactorHotAndColdTest.java:43)[defer]: No subscription so far
    //INFO  [main] defer (ReactorHotAndColdTest.java:39)[lambda$defer$0]: Generating new items
    //INFO  [main] defer (ReactorHotAndColdTest.java:41)[lambda$defer$1]: new subscription
    //INFO  [main] defer (ReactorHotAndColdTest.java:44)[lambda$defer$2]: 1 c4752464-8924-44ac-b337-c93976331de0
    //INFO  [main] defer (ReactorHotAndColdTest.java:39)[lambda$defer$0]: Generating new items
    //INFO  [main] defer (ReactorHotAndColdTest.java:41)[lambda$defer$1]: new subscription
    //INFO  [main] defer (ReactorHotAndColdTest.java:45)[lambda$defer$3]: 2 e2c79eee-77c8-4107-b7ac-f74073b19d7e
    //INFO  [main] defer (ReactorHotAndColdTest.java:46)[defer]: Data generate twice
    @Test
    public void defer() {
        // cold publisher:
        // no data would be generated without a subscriber
        // whenever a subscriber appears, all the sequence data is generated for that subscriber

        Logger logger = Logs.getLogger("defer");
        Flux<String> flux = Flux.defer(() -> {
            logger.info("Generating new items");
            return Flux.just(UUID.randomUUID().toString());
        }).doOnSubscribe(s -> logger.info("new subscription")); // 2

        logger.info("No subscription so far");
        flux.subscribe(e -> logger.info("1 {}", e));
        flux.subscribe(e -> logger.info("2 {}", e));
        logger.info("Data generate twice");
    }

    //INFO  [main] just (ReactorHotAndColdTest.java:77)[just]: No subscription so far
    //INFO  [main] just (ReactorHotAndColdTest.java:75)[lambda$just$4]: new subscription
    //INFO  [main] just (ReactorHotAndColdTest.java:78)[lambda$just$5]: 1: da06eef6-aec9-413c-9044-a5673183cb29
    //INFO  [main] just (ReactorHotAndColdTest.java:75)[lambda$just$4]: new subscription
    //INFO  [main] just (ReactorHotAndColdTest.java:79)[lambda$just$6]: 2: da06eef6-aec9-413c-9044-a5673183cb29
    //INFO  [main] just (ReactorHotAndColdTest.java:80)[just]: Data generate once

    /**
     * @see org.reactivestreams.Processor
     */
    @Test
    public void just() {
        // hot publisher:
        // data generation does NOT depend on the presence of a subscriber
        // when a subscriber appears, may not send the previously generated values, only now ones

        Logger logger = Logs.getLogger("just");
        Flux<String> flux = Flux.just(UUID.randomUUID().toString())
                .doOnSubscribe(s -> logger.info("new subscription")); // 2

        logger.info("No subscription so far");
        flux.subscribe(e -> logger.info("1: {}", e));
        flux.subscribe(e -> logger.info("2: {}", e));
        logger.info("Data generate once");
    }

    //
    // cold publisher => hot publisher
    //

    //INFO  [main] publish (ReactorHotAndColdTest.java:110)[publish]: No subscription so far
    //INFO  [main] publish (ReactorHotAndColdTest.java:114)[publish]: connect
    //INFO  [main] publish (ReactorHotAndColdTest.java:106)[lambda$publish$7]: new subscription
    //INFO  [main] publish (ReactorHotAndColdTest.java:111)[lambda$publish$8]: 1: 0
    //INFO  [main] publish (ReactorHotAndColdTest.java:112)[lambda$publish$9]: 2: 0
    //INFO  [main] publish (ReactorHotAndColdTest.java:111)[lambda$publish$8]: 1: 1
    //INFO  [main] publish (ReactorHotAndColdTest.java:112)[lambda$publish$9]: 2: 1
    //INFO  [main] publish (ReactorHotAndColdTest.java:111)[lambda$publish$8]: 1: 2
    //INFO  [main] publish (ReactorHotAndColdTest.java:112)[lambda$publish$9]: 2: 2

    /**
     * <pre>
     * @see Flux#publish()
     * @see Flux#replay()
     *
     * @see ConnectableFlux#connect()
     * @see ConnectableFlux#refCount()
     * @see ConnectableFlux#autoConnect()
     * </pre>
     */
    @Test
    public void publish() {
        // multicast
        Logger logger = Logs.getLogger("publish");
        Flux<Integer> flux = Flux.range(0, 3) // cold publisher
                .doOnSubscribe(s -> logger.info("new subscription")); // 1

        ConnectableFlux<Integer> cf = flux.publish();

        logger.info("No subscription so far");
        cf.subscribe(e -> logger.info("1: {}", e));
        cf.subscribe(e -> logger.info("2: {}", e));

        logger.info("connect");
        // trigger downstream subscriber execution
        cf.connect();
    }

    //INFO  [main] cache (ReactorHotAndColdTest.java:125)[cache]: No subscription so far
    //INFO  [main] cache (ReactorHotAndColdTest.java:122)[lambda$cache$10]: new subscription
    //INFO  [main] cache (ReactorHotAndColdTest.java:126)[lambda$cache$11]: 1: 0
    //INFO  [main] cache (ReactorHotAndColdTest.java:126)[lambda$cache$11]: 1: 1
    //INFO  [main] cache (ReactorHotAndColdTest.java:127)[lambda$cache$12]: 2: 0
    //INFO  [main] cache (ReactorHotAndColdTest.java:127)[lambda$cache$12]: 2: 1
    //INFO  [main] cache (ReactorHotAndColdTest.java:122)[lambda$cache$10]: new subscription
    //INFO  [main] cache (ReactorHotAndColdTest.java:131)[lambda$cache$13]: 3: 0
    //INFO  [main] cache (ReactorHotAndColdTest.java:131)[lambda$cache$13]: 3: 1
    @Test
    public void cache() throws InterruptedException {
        // data cache
        Logger logger = Logs.getLogger("cache");
        Flux<Integer> flux = Flux.range(0, 2)
                .doOnSubscribe(s -> logger.info("new subscription")); // 2

        Flux<Integer> cachedFlux = flux.cache(Duration.ofSeconds(1)); // cache for 1 second

        logger.info("No subscription so far");
        cachedFlux.subscribe(e -> logger.info("1: {}", e));
        cachedFlux.subscribe(e -> logger.info("2: {}", e));

        Thread.sleep(1200); // wait for cache expire

        cachedFlux.subscribe(e -> logger.info("3: {}", e));
    }

    //INFO  [main] share (ReactorHotAndColdTest.java:149)[share]: No subscription so far
    //INFO  [main] share (ReactorHotAndColdTest.java:145)[lambda$share$14]: new subscription
    //INFO  [parallel-1] share (ReactorHotAndColdTest.java:150)[lambda$share$15]: 1: 0
    //INFO  [parallel-2] share (ReactorHotAndColdTest.java:150)[lambda$share$15]: 1: 1
    //INFO  [parallel-3] share (ReactorHotAndColdTest.java:150)[lambda$share$15]: 1: 2
    //INFO  [parallel-4] share (ReactorHotAndColdTest.java:150)[lambda$share$15]: 1: 3
    //INFO  [parallel-4] share (ReactorHotAndColdTest.java:154)[lambda$share$16]: 2: 3
    //INFO  [parallel-5] share (ReactorHotAndColdTest.java:150)[lambda$share$15]: 1: 4
    //INFO  [parallel-5] share (ReactorHotAndColdTest.java:154)[lambda$share$16]: 2: 4
    @Test
    public void share() throws InterruptedException {
        // propagate the data that the subscriber has not missed yet
        Logger logger = Logs.getLogger("share");
        Flux<Integer> flux = Flux.range(0, 5)
                .delayElements(Duration.ofMillis(100))
                .doOnSubscribe(s -> logger.info("new subscription"));

        Flux<Integer> sharedFlux = flux.share();

        logger.info("No subscription so far");
        sharedFlux.subscribe(e -> logger.info("1: {}", e));

        Thread.sleep(400);

        sharedFlux.subscribe(e -> logger.info("2: {}", e));

        Thread.sleep(1000);
    }
}
