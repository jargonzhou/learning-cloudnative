package com.spike.reactor.jdk9;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subscribers.TestSubscriber;
import org.junit.jupiter.api.Test;
import org.reactivestreams.FlowAdapters;

import java.util.concurrent.Flow;

public class FlowTest {
    @Test
    public void test() {
        // RxJava => Flow
        Flow.Publisher<String> publisher = FlowAdapters.toFlowPublisher(Flowable.just("1", "2", "3"));

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        Flow.Subscriber<String> subscriber = FlowAdapters.toFlowSubscriber(testSubscriber);

        publisher.subscribe(subscriber);

        testSubscriber.assertComplete();
        testSubscriber.assertValueCount(3);
        testSubscriber.assertValues("1", "2", "3");
    }
}
