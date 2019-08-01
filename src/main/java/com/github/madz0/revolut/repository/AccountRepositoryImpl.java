package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.model.Account;

import javax.persistence.EntityManager;
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
}
