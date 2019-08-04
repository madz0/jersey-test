package com.github.madz0.revolut.exception;

public class DataIntegrityException extends BaseRuntimeException {
    public DataIntegrityException(String confidentialMessage, String generalMessage) {
        super(confidentialMessage, generalMessage);
    }

    public DataIntegrityException(String confidentialMessage, String generalMessage, Throwable cause) {
        super(confidentialMessage, generalMessage, cause);
    }
}
