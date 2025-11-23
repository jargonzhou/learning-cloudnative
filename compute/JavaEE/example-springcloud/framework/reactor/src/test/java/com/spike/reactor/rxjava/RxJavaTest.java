package com.spike.reactor.rxjava;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.schedulers.TestScheduler;
import io.reactivex.rxjava3.subscribers.TestSubscriber;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


/**
 * @see TestObserver
 */
public class RxJavaTest {

    //
    // Flowable: 0..N, backpressured
    //

    @Test
    public void testFlowable() {
        // io.reactivex.rxjava3.subscribers.TestSubscriber
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();

        Flowable.just(1, 2, 3)
                .subscribe(testSubscriber);

        testSubscriber.assertComplete();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(3);
        testSubscriber.assertValues(1, 2, 3);
    }

    @Test
    public void testFlowableException() {
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();

        Flowable.fromIterable(Arrays.asList("A", "B", "C"))
                .concatWith(Flowable.error(new RuntimeException("ERROR")))
                .subscribe(testSubscriber);

        testSubscriber.assertError(RuntimeException.class);
        testSubscriber.assertNotComplete(); // stop processing
    }

    @Test
    public void testFlowableTimed() {
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        TestScheduler testScheduler = new TestScheduler();

        Flowable<Long> tick = Flowable.interval(1, TimeUnit.SECONDS, testScheduler);
        Flowable.fromArray("A", "B", "C")
                .zipWith(tick, (string, index) -> index + "-" + string)
                .subscribeOn(testScheduler) // 在调度器上订阅
                .subscribe(testSubscriber);

        // time 0
        testSubscriber.assertNoValues();
        testSubscriber.assertNotComplete();

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        // time 1
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue("0-A");

        testScheduler.advanceTimeTo(4, TimeUnit.SECONDS);
        // time 4
        testSubscriber.assertComplete();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(3);
        testSubscriber.assertValues("0-A", "1-B", "2-C");
    }

    //
    // Observable: 0..N, unbounded
    //

    @Test
    public void testPublishConsumeStreams() {
        // Observable
        Observable<String> observable = Observable.create(
                new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                        emitter.onNext("Hello, reactive world!");
                        emitter.onComplete();
                    }
                }
        );

        // Observer
        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.err.println("onSubscribe: " + d);
            }

            @Override
            public void onNext(String t) {
                System.err.println("onNext: " + t);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.err.println("onError: " + e);
            }

            @Override
            public void onComplete() {
                System.err.println("onComplete");
            }
        });

        // test
        TestObserver<String> testObserver = new TestObserver<>();
        observable.subscribe(testObserver);

        testObserver.assertValue(value -> {
            System.err.println(value);
            return true;
        });
        testObserver.assertComplete();
    }

    @Test
    public void testPublishConsumeStreamsLambda() {
        Observable.create(emitter -> {
            emitter.onNext("Hello, reactive world!");
            emitter.onComplete();
        }).subscribe(
                t -> System.err.println("onNext: " + t),
                e -> System.err.println("onError: " + e),
                () -> System.err.println("onComplete")
        ).dispose();
    }

    @Test
    public void creation() {
        Observable.just(1, 2, 3, 4).subscribe(System.err::print).dispose();
        System.err.println();
        Observable.fromArray(new String[]{"A", "B", "C"}).subscribe(System.err::print).dispose();
        System.err.println();
        Observable.fromIterable(Collections.emptyList()).subscribe(System.err::print).dispose();
        System.err.println();

        Observable<String> hello = Observable.fromCallable(() -> "Hello ");
        ExecutorService es = Executors.newFixedThreadPool(1);
        Future<String> future = es.submit(() -> "World");
        Observable<String> world = Observable.fromFuture(future);
        // concat
        Observable.concat(hello, world, Observable.just("!"))
                .forEach(System.err::print).dispose();
    }

    @Test
    public void asyncSequence() {
        // every 1 second
        Disposable d = Observable.interval(1L, TimeUnit.SECONDS)
                .subscribe(System.out::print, System.err::print);

        // how can we test this???
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        d.dispose();
    }

    @Test
    public void stream_transformation() {
        // map
        // filter
        // count

        // zip
        Observable.zip(Observable.just("A", "B", "C"), Observable.just(1, 2),
                        (a, b) -> a + b)
                .forEach(System.out::println)
                .dispose();


    }

    @Test
    public void scheduler() {
        Disposable d = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io()) // Scheduler
                .subscribe(System.err::print);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        d.dispose();
    }
}
