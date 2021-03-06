package com.github.madz0.jerseytest.exception;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Slf4j
@Provider
public class ExternalServiceExceptionMapper implements ExceptionMapper<ExternalServiceException> {

    @Override
    public Response toResponse(ExternalServiceException e) {
        log.error("errorCode {}", e.getErrorCode(), e);
        return Response.serverError().entity("Sorry, the processing of the request has failed, If you want to report this, please include provided error code for further investigation. Error code=" + e.getErrorCode()).build();
    }
}
