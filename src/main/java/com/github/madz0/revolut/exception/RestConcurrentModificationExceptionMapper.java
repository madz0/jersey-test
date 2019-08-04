package com.github.madz0.revolut.exception;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Slf4j
@Provider
public class RestConcurrentModificationExceptionMapper implements ExceptionMapper<RestConcurrentModificationException> {
    @Override
    public Response toResponse(RestConcurrentModificationException e) {
        log.error("", e);
        return Response.status(Response.Status.CONFLICT).entity(e.getGeneralMessage()).build();
    }
}
