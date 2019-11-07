package com.github.madz0.jerseytest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.madz0.jerseytest.repository.AccountRepository;
import com.github.madz0.jerseytest.repository.AccountRepositoryImpl;
import com.github.madz0.jerseytest.repository.TransferRepository;
import com.github.madz0.jerseytest.repository.TransferRepositoryImpl;
import com.github.madz0.jerseytest.resource.AccountsResource;
import com.github.madz0.jerseytest.service.AccountService;
import com.github.madz0.jerseytest.service.CurrencyService;
import com.github.madz0.jerseytest.service.SimpleCurrencyServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.GenericType;
import java.util.Properties;

@Slf4j
public class AppConfig extends ResourceConfig {

    public AppConfig(Properties properties) {
        register(new Hk2Binder(properties));
        register(AccountsResource.class);
        register(JerseyObjectMapperProvider.class);
        register(JacksonFeature.class);
        registerExceptionMappersPackage(this);
        registerBeanValidationResponse(this);
    }

    public static void registerExceptionMappersPackage(ResourceConfig resourceConfig) {
        resourceConfig.packages("com.github.madz0.jerseytest.exception");
    }

    public static void registerBeanValidationResponse(ResourceConfig resourceConfig) {
        resourceConfig.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    }

    @RequiredArgsConstructor
    public static class Hk2Binder extends AbstractBinder {
        private final Properties properties;

        @Override
        protected void configure() {
            bindFactory(EntityManagerFactoryFactory.class).to(EntityManagerFactory.class).proxy(true).proxyForSameScope(false).in(Singleton.class);
            bindFactory(EntityManagerJerseyFactory.class).to(EntityManager.class).proxy(true).proxyForSameScope(false).in(RequestScoped.class);
            bind(AccountRepositoryImpl.class).to(AccountRepository.class);
            bind(SimpleCurrencyServiceImpl.class).to(CurrencyService.class);
            bind(TransferRepositoryImpl.class).to(TransferRepository.class);
            bindAsContract(AccountService.class);
            bindFactory(ObjectMapperFactory.class).to(ObjectMapper.class);
            bind(new ConfigFieldInjectionResolver(properties))
                    .to(new GenericType<InjectionResolver<Config>>() {
                    }).in(Singleton.class);
            bind(new ConfigParamInjectionResolver(properties))
                    .to(ValueParamProvider.class).in(Singleton.class);
        }
    }
}
