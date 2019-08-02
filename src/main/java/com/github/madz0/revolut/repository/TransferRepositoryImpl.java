package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.model.Transfer;

import javax.persistence.EntityManager;

public class TransferRepositoryImpl extends AbstractRepository<Transfer> implements TransferRepository {
    public TransferRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }
}
