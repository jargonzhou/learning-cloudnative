package com.spike.reactor.olddays.observer;

// 观察者
@FunctionalInterface // 函数接口
public interface Observer<T> {
    void observe(T event);
}
