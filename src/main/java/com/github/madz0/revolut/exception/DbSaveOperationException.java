package com.github.madz0.revolut.exception;

public class DbSaveOperationException extends RuntimeException {
    public DbSaveOperationException(String message, Throwable e) {
        super(message, e);
    }
}
