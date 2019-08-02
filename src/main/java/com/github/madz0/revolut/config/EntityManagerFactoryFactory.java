package com.github.madz0.revolut.config;

import org.glassfish.jersey.server.CloseableService;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class EntityManagerFactoryFactory implements Supplier<EntityManagerFactory> {
    private final CloseableService closeableService;

    @Inject
    public EntityManagerFactoryFactory(CloseableService closeableServiceParam) {
        closeableService = requireNonNull(closeableServiceParam);
    }

    private EntityManagerFactory provide() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("h2-unit");
        closeableService.add(entityManagerFactory::close);
        return entityManagerFactory;
    }

    @Override
    public EntityManagerFactory get() {
        return provide();
    }
}
