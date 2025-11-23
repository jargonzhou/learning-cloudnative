package com.spike.reactor.rxjava;

import com.spike.reactor.domain.Temperature;
import io.reactivex.rxjava3.observers.TestObserver;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class RxSseEmitterTest {
    @Test
    public void testWithTestObserver() {
        TemperatureSensor sensor = new TemperatureSensor();

        // io.reactivex.rxjava3.observers.TestObserver
        TestObserver<Temperature> testObserver = new TestObserver<>();
        sensor.getDataStream() // Observable<Temperature>
                .subscribe(testObserver);

        testObserver.assertNotComplete();
        testObserver.awaitCount(1);
        testObserver.assertValue(t -> {
            System.err.println(t);
            return true;
        });
        testObserver.assertNotComplete();
    }

    @Test
    public void testWithMockito() throws InterruptedException, IOException {
        TemperatureSensor sensor = new TemperatureSensor();

        RxSseEmitter<Temperature> emitter = new RxSseEmitter<>();
        // spy on io.reactivex.rxjava3.core.Observer
        RxSseEmitter.RxSseObserver<Temperature> observer = Mockito.spy(emitter.getObserver());
        sensor.getDataStream().subscribe(observer);


        // called at least once in this duration
        Thread.sleep(5_000);
        Mockito.verify(observer, Mockito.atLeast(1))
                .onNext(Mockito.any(Temperature.class));
    }
}
