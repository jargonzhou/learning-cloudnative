package com.spike.reactor;

import com.spike.reactor.support.Logs;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * <pre>
 * mapping elements of reactive sequences:
 * @see Flux#map(Function)
 * @see Flux#cast(Class)
 * @see Flux#index()
 * @see Flux#timestamp()
 *
 * filtering reactive sequences:
 * @see Flux#filter(Predicate)
 * @see Flux#ignoreElements()
 * @see Flux#take(long)
 * @see Flux#take(Duration)
 * @see Flux#takeLast(int)
 * @see Flux#takeUntil(Predicate)
 * @see Flux#takeUntilOther(Publisher)
 * @see Flux#elementAt(int)
 * @see Flux#single()
 * @see Flux#skip(long)
 *
 * collecting reactive sequences:
 * @see Flux#collect(Collector)
 * @see Flux#collectList()
 * @see Flux#collectMap(Function)
 * @see Flux#collectMultimap(Function)
 * @see Flux#repeat()
 * @see Flux#repeat(long)
 * @see Flux#defaultIfEmpty(Object)
 * @see Flux#distinct()
 * @see Flux#distinctUntilChanged()
 *
 * reducing stream elements:
 * @see Flux#count()
 * @see Flux#all(Predicate)
 * @see Flux#any(Predicate)
 * @see Flux#reduce(BiFunction)
 * @see Flux#scan(BiFunction)
 * @see Flux#then()
 * @see Flux#thenMany(Publisher)
 * @see Flux#thenEmpty(Publisher)
 *
 * combining reactive streams:
 * @see Flux#concat(Publisher[])
 * @see Flux#merge(Publisher[])
 * @see Flux#zip(Publisher, Publisher)
 * @see Flux#zipWithIterable(Iterable)
 * @see Flux#combineLatest(Function, Publisher[])
 *
 * batching stream elements:
 * buffering: {@code Flux<List<T>>}
 * @see Flux#buffer(int)
 * windowing: {@code Flux<Flux<T>>}
 * @see Flux#window(int)
 * @see Flux#windowUntil(Predicate)
 * grouping: {@code Flux<GroupedFlux<K,T>>}
 * @see Flux#groupBy(Function)
 *
 * flatMap, concatMap, flatMapSequential
 * @see Flux#flatMap(Function)
 * @see Flux#concatMap(Function)
 * @see Flux#flatMapSequential(Function)
 *
 * sample elements:
 * @see Flux#sample(Duration)
 *
 * into blocking structures:
 * @see Flux#toIterable()
 * @see Flux#toStream()
 * @see Flux#blockFirst()
 * @see Flux#blockLast()
 *
 * peeking elements:
 * @see Flux#doOnNext(Consumer)
 * @see Flux#doOnComplete(Runnable)
 * @see Flux#doOnError(Consumer)
 * @see Flux#doOnSubscribe(Consumer)
 * @see Flux#doOnRequest(LongConsumer)
 * @see Flux#doOnCancel(Runnable)
 * @see Flux#doOnTerminate(Runnable)
 * @see Flux#doOnEach(Consumer)
 *
 * materializing and dematerializing signals:
 * @see Flux#materialize()
 * @see Flux#dematerialize()
 *
 * compose and transform reactive streams:
 * @see Flux#transform(Function)
 * @see Flux#transformDeferred(Function)
 * </pre>
 */
public class ReactorTransformationTest {

    @Test
    public void thenMany() {
        Flux<Integer> flux = Flux.just(1, 2, 3)
                .thenMany(Flux.just(4, 5))
                .log();

        StepVerifier.create(flux)
                .expectNext(4, 5)
                .verifyComplete();
    }

    @Test
    public void concat() {
        Flux<Integer> flux = Flux.concat(
                        Flux.range(1, 3),
                        Flux.range(4, 2),
                        Flux.range(6, 5))
                .log();

        StepVerifier.create(flux)
//                .expectNextCount(10)
//                .verifyComplete();
                .expectNextSequence(Stream.iterate(1, v -> v < 11, v -> v + 1).toList())
                .expectComplete()
                .verify();
    }

    @Test
    public void buffer() {
        Flux<List<Integer>> flux = Flux.range(1, 13)
                .buffer(4)
                .log();

        StepVerifier.create(flux)
                .expectNext(List.of(1, 2, 3, 4))
                .expectNext(List.of(5, 6, 7, 8))
                .expectNext(List.of(9, 10, 11, 12))
                .expectNext(List.of(13))
                .verifyComplete();
    }

    @Test
    public void windowUntil() {
        Logger logger = Logs.getLogger("windowUntil");
        Flux<Flux<Integer>> flux = Flux.range(101, 5)
                .windowUntil(v -> v % 2 == 0, true);
        flux.subscribe(f -> f.collectList() // collect to list
                .log()
                .doOnEach(v -> logger.info("{}", v))
                .subscribe());

        // how to test this???
    }

    @Test
    public void groupBy() {
        Logger logger = Logs.getLogger("groupBy");
        Flux<GroupedFlux<String, Integer>> flux = Flux.range(1, 7)
                .groupBy(e -> e % 2 == 0 ? "Even" : "Odd");
        flux.subscribe(gf -> gf.scan(
                        new ArrayList<>(),
                        (list, elem) -> {
                            list.add(elem);
                            if (list.size() > 2) {
                                list.remove(0);
                            }
                            return list;
                        })
                .filter(l -> !l.isEmpty())
                .subscribe(v -> logger.info("{}: {}", gf.key(), v))
        );
    }

    @Test
    public void flatMap() {
        Flux<String> flux = Flux.range(1, 3)
                .map(i -> "user-" + i)
                .flatMap(u -> requestBooks(u)
                        .map(b -> u + "/" + b));

        Disposable d = flux.log()
                .subscribe();

        // subscribe in parallel
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        d.dispose();
    }

    private static final Random random = new Random();

    private Flux<String> requestBooks(String user) {
        return Flux.range(1, random.nextInt(3) + 1)
                .map(i -> "book-" + i)
                .delayElements(Duration.ofMillis(3));
    }

    @Test
    public void sample() {
        Disposable d = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(100))
                .sample(Duration.ofMillis(500))
                .log()
                .subscribe();

        // subscribe in parallel
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        d.dispose();
    }

    @Test
    public void materialize() {
        Logger logger = Logs.getLogger("materialize");
        Mono<List<Object>> flux = Flux.range(1, 3)
                .doOnNext(v -> logger.info("data: {}", v))
                // data to signal
                .materialize()
                .doOnNext(s -> logger.info("signal: {}", s))
                // signal to data
                .dematerialize()
                .collectList()
                .log();

        flux.subscribe();
    }

    @Test
    public void transform() {
        Function<Flux<String>, Flux<String>> logUserInfo = flux ->
                flux.index()
                        .map(Tuple2::getT2);

        Flux<String> flux = Flux.range(1000, 3)
                .map(i -> "user-" + i)
                .transform(logUserInfo) // apply function on streams: on assembly time
                .log();

        flux.subscribe();
    }


    //INFO  [main] transformDeferred (ReactorTransformationTest.java:273)[lambda$transformDeferred$19]: B 1
    //INFO  [main] transformDeferred (ReactorTransformationTest.java:273)[lambda$transformDeferred$19]: B 2
    //INFO  [main] transformDeferred (ReactorTransformationTest.java:270)[lambda$transformDeferred$18]: A 1
    //INFO  [main] transformDeferred (ReactorTransformationTest.java:270)[lambda$transformDeferred$18]: A 2
    @Test
    public void transformDeferred() {
        Logger logger = Logs.getLogger("transformDeferred"); // old: compose
        AtomicBoolean ai = new AtomicBoolean(false);
        Function<Flux<String>, Flux<String>> logUserInfo = flux -> {
            if (ai.get()) {
                ai.set(false);
                return flux.doOnNext(e -> logger.info("A {}", e));
            } else {
                ai.set(true);
                return flux.doOnNext(e -> logger.info("B {}", e));
            }
        };

        Flux<String> flux = Flux.just("1", "2")
                .transformDeferred(logUserInfo); // apply function on stream: on subscription time

        flux.subscribe();
        flux.subscribe();
    }

}
