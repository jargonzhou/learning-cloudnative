package com.spike.reactor.olddays.observer;

// 主体
public interface Subject<T> {
    void registerObserver(Observer<T> observer);

    void unregisterObserver(Observer<T> observer);

    void notifyObservers(T event);
}
