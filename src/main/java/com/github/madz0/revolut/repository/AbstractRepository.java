package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.exception.DbSaveOperationException;
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
        if (entity.getId() != null && entity.getId() <= 0) {
            throw new IllegalArgumentException("Wrong id value for entity " + entity.toString());
        }
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (entity.getId() != null) {
                entityManager.merge(entity);
            } else {
                entityManager.persist(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new DbSaveOperationException(entity.toString(), e);
        }
        return entity;
    }
}
