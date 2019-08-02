package com.github.madz0.revolut.exception;

public class DbQueryException extends RuntimeException {
    public DbQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
