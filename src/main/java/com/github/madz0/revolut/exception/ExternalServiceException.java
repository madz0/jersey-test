package com.github.madz0.revolut.exception;

public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message, Throwable e) {
        super(message, e);
    }
}
