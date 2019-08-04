package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.exception.DbQueryException;
import com.github.madz0.revolut.exception.RestIllegalArgumentException;
import com.github.madz0.revolut.model.Account;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
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
        try {
            totalSize = entityManager.createQuery("select count (a.id) from Account a", Long.class).getSingleResult();
        } catch (Exception e) {
            throw new DbQueryException("Failed to execute count query", e);
        }

        if (page * size > totalSize) {
            throw new RestIllegalArgumentException("page value is bigger than result set size");
        }
        try {
            List<Account> accounts = entityManager.createQuery(
                    "select a from Account a", Account.class)
                    .setFirstResult(page * size)
                    .setMaxResults(size)
                    .getResultList();
            return new Page<>(accounts, totalSize, page, size);
        } catch (Exception e) {
            throw new DbQueryException("Failed to execute list query. page=" + page + ", size=" + size + ", totalSize=" + totalSize, e);
        }
    }

    @Override
    public Optional<Account> findForUpdateById(Long id) {
        if (id == null || id <= 0) {
            throw new RestIllegalArgumentException("id");
        }
        try {
            return Optional.ofNullable(entityManager.find(Account.class, id, LockModeType.PESSIMISTIC_WRITE));
        } catch (Exception e) {
            throw new DbQueryException("Failed to execute find query. id=" + id, e);
        }
    }
}
