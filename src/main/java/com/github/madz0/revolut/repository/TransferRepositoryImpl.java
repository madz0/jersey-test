package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.exception.DataIntegrityException;
import com.github.madz0.revolut.exception.DbQueryException;
import com.github.madz0.revolut.exception.RestIllegalArgumentException;
import com.github.madz0.revolut.model.Transfer;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class TransferRepositoryImpl extends AbstractRepository<Transfer> implements TransferRepository {

    @Inject
    public TransferRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Optional<Transfer> findTransferByAccountIdAndId(Long accountId, Long transferId) {
        assertFindTransferByAccountIdAndIdIllegalArguments(accountId, transferId);
        try {
            return Optional.ofNullable(entityManager.createQuery("select t from Transfer t where t.id = :transferId and t.from.id = :accountId", Transfer.class)
                    .setParameter("transferId", transferId).setParameter("accountId", accountId)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new DbQueryException("accountId=" + accountId + ", transferId=" + transferId, e);
        }
    }

    @Override
    public Page<Transfer> findAllByAccountId(Long accountId, int page, int size) {
        assertFindAllIllegalArguments(accountId, page, size);
        long totalSize;
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            try {
                totalSize = entityManager.createQuery("select count (t.id) from Transfer t where t.from.id = :accountId or t.to.id = :accountId", Long.class)
                        .setParameter("accountId", accountId)
                        .getSingleResult();
            } catch (NoResultException e) {
                throw new DataIntegrityException(null, "accountId=" + accountId + " is not found");
            } catch (Exception e) {
                throw new DbQueryException("Failed to execute count query. accountId = " + accountId, e);
            }

            if (((long) page) * size > totalSize) {
                throw new RestIllegalArgumentException("page value is bigger than result set size");
            }

            try {
                List<Transfer> accounts = entityManager.createQuery("select t from Transfer t where t.from.id = :accountId or t.to.id = :accountId", Transfer.class)
                        .setParameter("accountId", accountId)
                        .setFirstResult(page * size)
                        .setMaxResults(size)
                        .getResultList();
                transaction.commit();
                return new Page<>(accounts, totalSize, page, size);
            } catch (NoResultException e) {
                throw new DataIntegrityException(null, "accountId=" + accountId + " is not found");
            } catch (Exception e) {
                throw new DbQueryException("Failed to execute list query. accountId=" + accountId + ", page=" + page + ", size=" + size + ", totalSize=" + totalSize, e);
            }
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    private void assertFindAllIllegalArguments(Long accountId, int page, int size) {
        if (page < 0) {
            throw new RestIllegalArgumentException("page<0");
        }
        if (size <= 0) {
            throw new RestIllegalArgumentException("size<=0");
        }
        if (accountId == null || accountId <= 0) {
            throw new RestIllegalArgumentException("accountId<=0");
        }
    }

    private void assertFindTransferByAccountIdAndIdIllegalArguments(Long accountId, Long transferId) {
        if (accountId == null || accountId <= 0) {
            throw new RestIllegalArgumentException("page<0");
        }
        if (transferId == null || transferId <= 0) {
            throw new RestIllegalArgumentException("size<=0");
        }
    }
}
