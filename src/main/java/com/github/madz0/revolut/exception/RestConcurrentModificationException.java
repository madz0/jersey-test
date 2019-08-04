package com.github.madz0.revolut.exception;

public class RestConcurrentModificationException extends BaseRuntimeException {
    public RestConcurrentModificationException(String confidentialMessage, String generalMessage, Throwable cause) {
        super(confidentialMessage, generalMessage, cause);
    }
}
