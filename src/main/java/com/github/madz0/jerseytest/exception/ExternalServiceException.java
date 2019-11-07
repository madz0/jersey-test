package com.github.madz0.jerseytest.exception;

public class ExternalServiceException extends BaseRuntimeException {
    public ExternalServiceException(String confidentialMessage, String generalMessage) {
        super(confidentialMessage, generalMessage);
    }

    public ExternalServiceException(String confidentialMessage, String generalMessage, Throwable cause) {
        super(confidentialMessage, generalMessage, cause);
    }
}
