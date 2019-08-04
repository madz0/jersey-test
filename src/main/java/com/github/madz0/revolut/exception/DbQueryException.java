package com.github.madz0.revolut.exception;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.ext.Provider;

@Slf4j
@Provider
public class DbQueryException extends BaseRuntimeException {
    public DbQueryException(String message, Throwable cause) {
        super(message, null, cause);
    }
}
