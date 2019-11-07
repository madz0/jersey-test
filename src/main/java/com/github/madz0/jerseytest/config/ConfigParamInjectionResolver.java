package com.github.madz0.jerseytest.config;

import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;

import java.util.Properties;
import java.util.function.Function;

public class ConfigParamInjectionResolver implements ValueParamProvider {
    private final Properties properties;

    ConfigParamInjectionResolver(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Function<ContainerRequest, ?> getValueProvider(Parameter parameter) {
        if (!parameter.isAnnotationPresent(Config.class)
                || !String.class.equals(parameter.getRawType())) {
            return null;
        }
        return x -> {
            Config annotation = parameter.getAnnotation(Config.class);
            if (annotation != null) {
                String prop = annotation.value();
                return properties.getProperty(prop);
            }
            return null;
        };
    }

    @Override
    public PriorityType getPriority() {
        return Priority.HIGH;
    }
}
