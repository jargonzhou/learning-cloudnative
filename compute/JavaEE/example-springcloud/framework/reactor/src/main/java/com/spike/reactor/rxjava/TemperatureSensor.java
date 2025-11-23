package com.spike.reactor.rxjava;

import com.spike.reactor.domain.Temperature;
import io.reactivex.rxjava3.core.Observable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TemperatureSensor {
    private final Random rnd = new Random();

    private final Observable<Temperature> dataStream =
            Observable.range(0, Integer.MAX_VALUE)
                    // arg: Function<?, ObservableSource>
                    .concatMap(tick -> Observable.just(tick)
                            .delay(rnd.nextInt(5000), TimeUnit.MILLISECONDS)
                            .map(ignore -> this.probe()))
                    // 为什么要用publish, refCount???
                    .publish() // 共享
                    .refCount() // 只在有外部订阅时创建订阅
            ;

    private Temperature probe() {
        return new Temperature(16 + rnd.nextGaussian() * 10);
    }

    public Observable<Temperature> getDataStream() {
        return dataStream;
    }
}
