package com.spike.reactor.olddays.observer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ObserverPatternTest {

    @Test
    public void test() {
        // given
        Subject<String> subject = new ConcreteSubject();
        Observer<String> observerA = Mockito.spy(new ConcreteObserverA()); // spy
        Observer<String> observerB = Mockito.spy(new ConcreteObserverB());

        // when
        final String NO_LISTENERS = "No listeners";
        final String FOR_A = "Message for A";
        final String FOR_B = "Message for B";
        final String FOR_A_AND_B = "Message for A and B";
        subject.notifyObservers(NO_LISTENERS);

        subject.registerObserver(observerA);
        subject.notifyObservers(FOR_A);

        subject.registerObserver(observerB);
        subject.notifyObservers(FOR_A_AND_B);

        subject.unregisterObserver(observerA);
        subject.notifyObservers(FOR_B);

        subject.unregisterObserver(observerB);
        subject.notifyObservers(NO_LISTENERS);

        // then: 验证收到的消息和次数
        Mockito.verify(observerA, Mockito.times(1)).observe(FOR_A);
        Mockito.verify(observerA, Mockito.times(1)).observe(FOR_A_AND_B);
        Mockito.verifyNoMoreInteractions(observerA); // 不再有交互

        Mockito.verify(observerB, Mockito.times(1)).observe(FOR_B);
        Mockito.verify(observerB, Mockito.times(1)).observe(FOR_A_AND_B);
        Mockito.verifyNoMoreInteractions(observerB);
    }

    //
    // 具体的观察者
    //

    public static class ConcreteObserverA implements Observer<String> {

        @Override
        public void observe(String event) {
            System.out.println("Observer A: " + event);
        }
    }

    public static class ConcreteObserverB implements Observer<String> {

        @Override
        public void observe(String event) {
            System.out.println("Observer B: " + event);
        }
    }

    //
    // 具体的主体
    //

    public static class ConcreteSubject implements Subject<String> {
        // 观察者集合
        private final Set<Observer<String>> observers = new CopyOnWriteArraySet<>();

        @Override
        public void registerObserver(Observer<String> observer) {
            observers.add(observer);
        }

        @Override
        public void unregisterObserver(Observer<String> observer) {
            observers.remove(observer);
        }

        @Override
        public void notifyObservers(String event) {
            // 逐个通知
            observers.forEach(observer -> observer.observe(event));
        }
    }

}
