package com.github.madz0.revolut.config;

import com.github.madz0.revolut.repository.AccountRepository;
import com.github.madz0.revolut.repository.AccountRepositoryImpl;
import com.github.madz0.revolut.repository.TransferRepository;
import com.github.madz0.revolut.repository.TransferRepositoryImpl;
import com.github.madz0.revolut.service.AccountService;
import com.github.madz0.revolut.service.CurrencyService;
import com.github.madz0.revolut.service.SimpleCurrencyServiceImpl;
import org.glassfish.jersey.internal.inject.AbstractBinder;

import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        //register(LoggingFilter.class);
        register(AccountService.class);
        register(new Hk2Binder());
    }

    public static class Hk2Binder extends AbstractBinder {
        @Override
        protected void configure() {
            bindAsContract(AccountService.class);
            bindFactory(EntityManagerFactoryFactory.class).to(EntityManagerFactory.class).proxy(true).proxyForSameScope(false).in(Singleton.class);
            bindFactory(EntityManagerJerseyFactory.class).to(EntityManager.class).proxy(true).proxyForSameScope(false).in(RequestScoped.class);
            bind(AccountRepositoryImpl.class).to(AccountRepository.class);
            bind(SimpleCurrencyServiceImpl.class).to(CurrencyService.class);
            bind(TransferRepositoryImpl.class).to(TransferRepository.class);
        }
    }
}
