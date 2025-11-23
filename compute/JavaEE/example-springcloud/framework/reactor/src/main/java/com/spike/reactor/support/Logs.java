package com.spike.reactor.support;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Logs {
    public static Logger getLogger(String name) {
        return LoggerFactory.getLogger(name);
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
