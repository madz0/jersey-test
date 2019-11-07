package com.github.madz0.jerseytest.config;

import org.glassfish.jersey.server.CloseableService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class EntityManagerJerseyFactory implements Supplier<EntityManager> {
    private final EntityManagerFactory emf;
    private final CloseableService closeableService;

    @Inject
    public EntityManagerJerseyFactory(EntityManagerFactory emfParam, CloseableService closeableServiceParam) {
        emf = requireNonNull(emfParam);
        closeableService = requireNonNull(closeableServiceParam);
    }

    private EntityManager provide() {
        EntityManager entityManager = emf.createEntityManager();
        closeableService.add(entityManager::close);
        return entityManager;
    }

    @Override
    public EntityManager get() {
        return provide();
    }
}
