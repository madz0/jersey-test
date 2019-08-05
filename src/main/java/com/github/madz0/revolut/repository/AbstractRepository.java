package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.exception.DataIntegrityException;
import com.github.madz0.revolut.exception.DbSaveOperationException;
import com.github.madz0.revolut.exception.RestConcurrentModificationException;
import com.github.madz0.revolut.exception.RestIllegalArgumentException;
import com.github.madz0.revolut.model.BaseModel;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;

@RequiredArgsConstructor
public abstract class AbstractRepository<T extends BaseModel> {
    protected final EntityManager entityManager;

    public T save(T entity) {
        if (entity == null) {
            throw new RestIllegalArgumentException("Entity was null");
        }
        if (entity.getId() != null && entity.getId() <= 0) {
            throw new RestIllegalArgumentException("Wrong id value for entity " + entity.toString());
        }

        EntityTransaction transaction = entityManager.getTransaction();
        boolean alreadyActive = transaction.isActive();
        try {
            if (!alreadyActive) {
                transaction.begin();
            }
            if (entity.getId() != null) {
                T fromDb = (T) entityManager.find(entity.getClass(), entity.getId());
                if (fromDb == null) {
                    throw new DataIntegrityException(null, "id " + entity.getId() + " not exists");
                }
                entity.setCreatedDate(fromDb.getCreatedDate());
                entity.setCreatedTime(fromDb.getCreatedTime());
                entityManager.merge(entity);
            } else {
                entityManager.persist(entity);
            }
            commitTransaction(transaction, alreadyActive);
            return entity;
        } catch (DataIntegrityException e) {
            rollBackTransaction(transaction, alreadyActive);
            throw e;
        } catch (OptimisticLockException e) {
            rollBackTransaction(transaction, alreadyActive);
            throw new RestConcurrentModificationException("transient entity version =" + entity.getVersion(), "Either version fields was incorrect or a concurrent operation has been occurred", e);
        } catch (Exception e) {
            rollBackTransaction(transaction, alreadyActive);
            throw new DbSaveOperationException(entity.toString(), e);
        }
    }

    private void rollBackTransaction(EntityTransaction transaction, boolean alreadyActive) {
        if (!alreadyActive) {
            transaction.rollback();
        }
    }

    private void commitTransaction(EntityTransaction transaction, boolean alreadyActive) {
        if (!alreadyActive) {
            transaction.commit();
        }
    }
}
