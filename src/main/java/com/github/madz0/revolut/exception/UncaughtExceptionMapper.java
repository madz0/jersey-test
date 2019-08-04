package com.github.madz0.revolut.exception;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

@Slf4j
@Provider
public class UncaughtExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        if (e instanceof NotFoundException) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (e instanceof BadRequestException) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else if(e instanceof NotAllowedException) {
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        else {
            String errorCode = UUID.randomUUID().toString();
            log.error("Uncaught exception. error code {} ", errorCode, e);
            return Response.serverError().entity("Sorry, the processing of the request has failed, If you want to report this, please include provided error code for further investigation. Error code=" + errorCode).build();
        }
    }
}
