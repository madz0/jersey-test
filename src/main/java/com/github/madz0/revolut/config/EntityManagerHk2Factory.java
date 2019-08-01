package com.github.madz0.revolut.config;

import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.server.CloseableService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static java.util.Objects.requireNonNull;

public class EntityManagerHk2Factory implements Factory<EntityManager> {
    private final EntityManagerFactory emf;
    private final CloseableService closeableService;

    @Inject
    public EntityManagerHk2Factory(EntityManagerFactory emfParam, CloseableService closeableServiceParam) {
        emf = requireNonNull(emfParam);
        closeableService = requireNonNull(closeableServiceParam);
    }

    @Override
    public EntityManager provide() {
        EntityManager entityManager = emf.createEntityManager();
        closeableService.add(entityManager::close);
        return entityManager;
    }

    @Override
    public void dispose(EntityManager entityManager) {
        entityManager.close();
    }
}
