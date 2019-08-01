package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.model.BaseModel;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@RequiredArgsConstructor
public abstract class AbstractRepository<T extends BaseModel> {
    final EntityManager entityManager;

    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity was null");
        }
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (entity.getId() != null && entity.getId() > 0) {
                entityManager.merge(entity);
            } else {
                entityManager.persist(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
        return entity;
    }
}
