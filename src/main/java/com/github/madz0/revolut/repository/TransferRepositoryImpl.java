package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.model.Transfer;

import javax.persistence.EntityManager;
import java.util.List;

public class TransferRepositoryImpl extends AbstractRepository<Transfer> implements TransferRepository {
    public TransferRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Page<Transfer> findAllByAccountId(Long accountId, int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("page<0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size<=0");
        }
        if (accountId == null || accountId <= 0) {
            throw new IllegalArgumentException("accountId<=0");
        }

        long totalSize = (long) entityManager.createQuery("select count (t.id) from Transfer t where t.from.id = :accountId or t.to.id = :accountId")
                .setParameter("accountId", accountId)
                .getSingleResult();
        List<Transfer> accounts = entityManager.createQuery("select t from Transfer t where t.from.id = :accountId or t.to.id = :accountId", Transfer.class)
                .setParameter("accountId", accountId)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
        return new Page<>(accounts, totalSize, page, size);
    }
}
