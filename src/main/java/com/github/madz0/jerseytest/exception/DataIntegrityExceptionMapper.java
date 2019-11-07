package com.github.madz0.jerseytest.exception;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Slf4j
@Provider
public class DataIntegrityExceptionMapper implements ExceptionMapper<DataIntegrityException> {

    @Override
    public Response toResponse(DataIntegrityException e) {
        log.error("", e);
        return Response.status(Response.Status.NOT_FOUND).entity(e.getGeneralMessage()).build();
    }
}
