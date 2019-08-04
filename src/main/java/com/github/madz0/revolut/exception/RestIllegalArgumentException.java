package com.github.madz0.revolut.exception;

public class RestIllegalArgumentException extends BaseRuntimeException {
    public RestIllegalArgumentException(String generalMessage) {
        super(null, generalMessage);
    }

    public RestIllegalArgumentException(String generalMessage, Throwable cause) {
        this(null, generalMessage, cause);
    }

    public RestIllegalArgumentException(String confidentialMessage, String generalMessage, Throwable cause) {
        super(confidentialMessage, generalMessage, cause);
    }
}
