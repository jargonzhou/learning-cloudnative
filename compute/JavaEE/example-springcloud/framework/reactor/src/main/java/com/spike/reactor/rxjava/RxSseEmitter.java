package com.spike.reactor.rxjava;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Objects;


public class RxSseEmitter<T> extends SseEmitter {
    private static final long DEFAULT_TIMEOUT = 30 * 60 * 1000;
    private final RxSseObserver<T> observer;

    public RxSseEmitter() {
        super(DEFAULT_TIMEOUT);

        this.observer = new RxSseObserver<T>() {
            @Override
            public void onNext(T t) {
                try {
                    System.err.println(t);
                    RxSseEmitter.this.send(t, MediaType.APPLICATION_JSON);
                } catch (IOException e) {
                    unsubscribe();
                }
            }
        };

        super.onCompletion(observer::unsubscribe); // 完成时回调
        super.onTimeout(observer::unsubscribe); // 超时时回调
    }

    public RxSseObserver<T> getObserver() {
        return observer;
    }

    public static abstract class RxSseObserver<T> implements Observer<T> {
        private Disposable disposable;

        @Override
        public void onSubscribe(Disposable s) {
            this.disposable = s;
        }

        @Override
        public void onError(Throwable t) {
            // do nothing
        }

        @Override
        public void onComplete() {
            // do nothing
        }

        public void unsubscribe() {
            if (Objects.nonNull(disposable)) {
                disposable.dispose();
            }
        }
    }
}
