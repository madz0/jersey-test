package com.github.madz0.revolut.exception;

public class RestUnsupportedOperationException extends BaseRuntimeException {
    public RestUnsupportedOperationException(String generalMessage) {
        super(null, generalMessage);
    }

    public RestUnsupportedOperationException(String generalMessage, Throwable cause) {
        this(null, generalMessage, cause);
    }

    public RestUnsupportedOperationException(String confidentialMessage, String generalMessage, Throwable cause) {
        super(confidentialMessage, generalMessage, cause);
    }
}
