package com.github.madz0.revolut.exception;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Slf4j
@Provider
public class RestIllegalArgumentExceptionMapper implements ExceptionMapper<RestIllegalArgumentException> {
    @Override
    public Response toResponse(RestIllegalArgumentException e) {
        log.error("", e);
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getGeneralMessage()).build();
    }
}
