package com.github.madz0.jerseytest.repository;

import com.github.madz0.jerseytest.exception.DataIntegrityException;
import com.github.madz0.jerseytest.exception.DbQueryException;
import com.github.madz0.jerseytest.exception.RestIllegalArgumentException;
import com.github.madz0.jerseytest.model.Account;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryImpl extends AbstractRepository<Account> implements AccountRepository {
    private final EntityManager entityManager;

    @Inject
    public AccountRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Account> findById(Long id) {
        if (id == null || id <= 0) {
            throw new RestIllegalArgumentException("id");
        }
        try {
            return Optional.ofNullable(entityManager.find(Account.class, id));
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new DbQueryException("Failed to execute find query. id=" + id, e);
        }
    }

    @Override
    public Page<Account> findAll(int page, int size) {
        if (page < 0) {
            throw new RestIllegalArgumentException("page<0");
        }
        if (size <= 0) {
            throw new RestIllegalArgumentException("size<=0");
        }

        long totalSize;
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            try {
                totalSize = entityManager.createQuery("select count (a.id) from Account a", Long.class).getSingleResult();
            } catch (NoResultException e) {
                throw new DataIntegrityException(null, "No entry found");
            } catch (Exception e) {
                throw new DbQueryException("Failed to execute count query", e);
            }

            if (((long) page) * size > totalSize) {
                throw new RestIllegalArgumentException("page value is bigger than result set size");
            }
            try {
                List<Account> accounts = entityManager.createQuery(
                        "select a from Account a", Account.class)
                        .setFirstResult(page * size)
                        .setMaxResults(size)
                        .getResultList();
                transaction.commit();
                return new Page<>(accounts, totalSize, page, size);
            } catch (NoResultException e) {
                throw new DataIntegrityException(null, "No entry found");
            } catch (Exception e) {
                throw new DbQueryException("Failed to execute list query. page=" + page + ", size=" + size + ", totalSize=" + totalSize, e);
            }
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public Optional<Account> findForUpdateById(Long id) {
        if (id == null || id <= 0) {
            throw new RestIllegalArgumentException("id");
        }
        try {
            return Optional.ofNullable(entityManager.find(Account.class, id, LockModeType.PESSIMISTIC_WRITE));
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new DbQueryException("Failed to execute find query. id=" + id, e);
        }
    }
}
