package com.spike.reactor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spike.reactor.support.Logs;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;

/**
 * <pre>
 * @see Scheduler
 * @see Schedulers
 *
 * no execution context:
 * @see Schedulers#immediate()
 * a single resuable thread: reuse for all callers, until scheduler disposed
 * @see Schedulers#single()
 * a per-call dedicated thread:
 * @see Schedulers#newSingle(String)
 * a bounded elastic thread pool: give a blocking process its own thread
 *  ExecutorService-based
 *  thread-per-task-based: VirtualThread (Java 21+)
 * @see Schedulers#boundedElastic()
 * @see Schedulers#newBoundedElastic(int, int, String)
 * a fixed pool of workers tuned for parallel work
 * @see Schedulers#parallel()
 * @see Schedulers#newParallel(String)
 * on existing ExecutorService
 * @see Schedulers#fromExecutorService(ExecutorService)
 *
 * switch the execution context in a reactive chain:
 *  affect where the subsequent operators execute, until anohter publishOn is chained in
 * @see Flux#publishOn(Scheduler)
 *  change the thread from which the whole chain of operators subscribes
 * @see Flux#subscribeOn(Scheduler)
 *
 * @see reactor.core.publisher.ParallelFlux
 * @see reactor.core.publisher.ParallelFlux#runOn(Scheduler)
 * </pre>
 */
public class ReactorSchedulerTest {

    //
    // threading and schedulers
    //
    // concurrency-agnostic: does NOT enforce a concurrency model
    //
    // most operators continue working in the thread on which the previous operator executed.
    // unless specified, the topmost operator itself runs on the thread in which the subscribe() call was made. <==
    // 
    // Scheduler: determine the execution model and where the execution happens.
    // Schedulers
    //
    // default scheduler:
    // Flux#interval(Duration, Duration, Scheduler) use parallel()

    // once subscribe, a chain of Subscriber objects is created, backward to the first publisher.

    @Test
    public void threadDefault() {
        Flux.range(0, 10)
                .log() // [main]
                .map(v -> String.valueOf(v + 1))
                .log() // [main]
                .subscribe(); // [main] <==
    }

    @Test
    public void thread() throws InterruptedException {
        Flux<Integer> flux = Flux.range(0, 10)
                .log() // [T]
                ;

        // subscribe in a thread
        new Thread(() -> flux.map(v -> String.valueOf(v + 1))
                .log() // [T]
                .subscribe() // [T] <==
                , "T").start();

        Thread.sleep(1000L);
    }

    @Test
    public void publishOn() {
        Logger logger = Logs.getLogger("publishOn");

        Flux.range(0, 20)
                .map(String::valueOf)
                .filter((s -> s.length() > 1))
                .doOnNext(v -> logger.info("{}", v)) // [main]
                .publishOn(Schedulers.newSingle("P")) // execute on scheduler
                .map(String::hashCode)
                .doOnNext(v -> logger.info("{}", v)) // [P-1]
                .subscribe(); // [main] <==
    }

    @Test
    public void subscribeOn() throws InterruptedException {
        Logger logger = Logs.getLogger("subscribeOn");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Mono<Car> mono = Mono.fromCallable(() -> {
                    try {
                        Car car = objectMapper.readValue(json, Car.class);
                        logger.info("{}", car);
                        return car;
                    } catch (Exception e) {
                        logger.error("failed to read json", e);
                        throw e;
                    }
                })
                .doOnEach(v -> logger.info("{}", v)) // [S-1]
                // change the thread from which the whole chain of operators subscribes
                .subscribeOn(Schedulers.newSingle("S"));

        // subscribe in a thread
        new Thread(() -> mono
                .doOnEach(v -> logger.info("{}", v)) // [S-1]
                .subscribe()).start();

        Thread.sleep(1000L);
    }


    public static class Car {
        private String color;
        private String type;

        public Car() {
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Car{" +
                    "color='" + color + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    @Test
    public void parallel() {
        Logger logger = Logs.getLogger("parallel");

        Flux.range(0, 100)
                .parallel() // parallel execution
                .runOn(Schedulers.parallel())
                .doOnEach(v -> logger.info("{}", v))
                .subscribe();
    }
}
