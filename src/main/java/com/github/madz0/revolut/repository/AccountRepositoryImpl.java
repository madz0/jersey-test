package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.model.Account;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryImpl extends AbstractRepository<Account> implements AccountRepository {
    private final EntityManager entityManager;

    public AccountRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Account> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id");
        }
        return Optional.ofNullable(entityManager.find(Account.class, id));
    }

    @Override
    public Page<Account> findAll(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("page<0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size<=0");
        }

        long totalSize = (long) entityManager.createQuery("select count (a.id) from Account a").getSingleResult();
        List<Account> accounts = entityManager.createQuery("select a from Account a", Account.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
        return new Page<>(accounts, totalSize, page, size);
    }

    @Override
    public Optional<Account> findForUpdateById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id");
        }
        return Optional.ofNullable(entityManager.find(Account.class, id, LockModeType.PESSIMISTIC_WRITE));
    }
}
