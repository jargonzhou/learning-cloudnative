package com.spike.springcloud.commons.exception;

/**
 * 存储异常.
 */
public class StorageException extends RuntimeException {

    public static StorageException newException(String message) {
        return new StorageException(message);
    }

    public static StorageException newException(String message, Throwable cause) {
        return new StorageException(message, cause);
    }

    public static StorageException newException(Throwable cause) {
        return new StorageException(cause);
    }

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(Throwable cause) {
        super(cause);
    }
}