package com.github.madz0.revolut.config;

import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;

import javax.inject.Singleton;
import java.util.Properties;

@Singleton
public class ConfigFieldInjectionResolver implements InjectionResolver<Config> {

    private final Properties properties;
    ConfigFieldInjectionResolver(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> handle) {
        if (String.class == injectee.getRequiredType()) {
            Config annotation = injectee.getParent().getAnnotation(Config.class);
            if (annotation != null) {
                String prop = annotation.value();
                return properties.getProperty(prop);
            }
        }
        return null;
    }

    @Override
    public boolean isConstructorParameterIndicator() {
        return true;
    }

    @Override
    public boolean isMethodParameterIndicator() {
        return true;
    }
}
