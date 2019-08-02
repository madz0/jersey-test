package com.github.madz0.revolut.config;

import com.github.madz0.revolut.repository.AccountRepository;
import com.github.madz0.revolut.repository.AccountRepositoryImpl;
import com.github.madz0.revolut.repository.TransferRepository;
import com.github.madz0.revolut.repository.TransferRepositoryImpl;
import com.github.madz0.revolut.service.AccountService;
import com.github.madz0.revolut.service.CurrencyService;
import com.github.madz0.revolut.service.SimpleCurrencyServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        register(LoggingFilter.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindAsContract(AccountService.class);
                bind(EntityManagerFactoryFactory.class).to(EntityManagerFactory.class).in(Singleton.class);
                bind(EntityManagerHk2Factory.class).to(EntityManager.class).in(RequestScoped.class);
                bind(AccountRepositoryImpl.class).to(AccountRepository.class);
                bind(SimpleCurrencyServiceImpl.class).to(CurrencyService.class);
                bind(TransferRepositoryImpl.class).to(TransferRepository.class);
            }
        });
    }
}
