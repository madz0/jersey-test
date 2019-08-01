package com.github.madz0.revolut.config;

import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.server.CloseableService;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static java.util.Objects.requireNonNull;

public class EntityManagerFactoryFactory implements Factory<EntityManagerFactory> {
    private final CloseableService closeableService;

    @Inject
    public EntityManagerFactoryFactory(CloseableService closeableServiceParam) {
        closeableService = requireNonNull(closeableServiceParam);
    }

    @Override
    public EntityManagerFactory provide() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("h2-unit");
        closeableService.add(entityManagerFactory::close);
        return entityManagerFactory;
    }

    @Override
    public void dispose(EntityManagerFactory entityManagerFactory) {
        entityManagerFactory.close();
    }
}
