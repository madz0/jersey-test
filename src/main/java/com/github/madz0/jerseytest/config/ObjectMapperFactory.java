package com.github.madz0.jerseytest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jaxrs.Jaxrs2TypesModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import javax.inject.Singleton;
import java.util.function.Supplier;

@Singleton
public class ObjectMapperFactory implements Supplier<ObjectMapper> {
    public ObjectMapper get() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        mapper.registerModule(new Jaxrs2TypesModule());
        mapper.registerModule(new ParameterNamesModule());
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
