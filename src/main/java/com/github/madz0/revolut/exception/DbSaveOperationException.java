package com.github.madz0.revolut.exception;

public class DbSaveOperationException extends BaseRuntimeException {
    public DbSaveOperationException(String message, Throwable cause) {
        super(message, null, cause);
    }
}
