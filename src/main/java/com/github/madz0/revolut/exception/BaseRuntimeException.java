package com.github.madz0.revolut.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BaseRuntimeException extends RuntimeException {
    private String confidentialMessage, generalMessage, errorCode;

    public BaseRuntimeException(String confidentialMessage, String generalMessage, Throwable cause) {
        super(confidentialMessage + "___" + generalMessage, cause);
        setup(confidentialMessage, generalMessage);
    }

    public BaseRuntimeException(String confidentialMessage, String generalMessage) {
        super(confidentialMessage + "___" + generalMessage);
        setup(confidentialMessage, generalMessage);
    }

    private void setup(String confidentialMessage, String generalMessage) {
        this.generalMessage = generalMessage;
        this.confidentialMessage = confidentialMessage;
        this.errorCode = UUID.randomUUID().toString();
    }
}
