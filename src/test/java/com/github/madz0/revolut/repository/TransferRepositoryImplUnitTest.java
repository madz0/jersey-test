package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.AbstractUnitTest;
import com.github.madz0.revolut.exception.DbQueryException;
import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.Currency;
import com.github.madz0.revolut.model.Transfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

class TransferRepositoryImplUnitTest extends AbstractUnitTest {
    private EntityManager entityManager;
    private TransferRepository transferRepository;

    @BeforeEach
    public void BeforeEach() {
        entityManager = mockEntityManagerTransaction();
    }

    @Test
    void findAllByAccountId() {
        final long account1Id = 1;
        final long account2Id = 2;
        final long account3Id = 3;
        final long queryAccountId = 1;

        final TypedQueryMocked<Transfer> queryMocked = new TypedQueryMocked<>();
        doAnswer(invocationOnMock -> {
            Account account1 = new Account();
            account1.setAmount(BigDecimal.TEN);
            account1.setCurrency(Currency.USD);
            account1.setId(account1Id);
            Account account2 = new Account();
            account2.setAmount(BigDecimal.TEN);
            account2.setCurrency(Currency.EUR);
            account2.setId(account2Id);
            Account account3 = new Account();
            account3.setAmount(BigDecimal.TEN);
            account3.setCurrency(Currency.EUR);
            account3.setId(account3Id);

            Transfer transfer1 = new Transfer(account1, account2, BigDecimal.TEN, account1.getCurrency(), account2.getCurrency(), BigDecimal.ONE);
            transfer1.setId(1L);
            Transfer transfer2 = new Transfer(account1, account3, BigDecimal.ONE, account1.getCurrency(), account3.getCurrency(), BigDecimal.ONE);
            transfer2.setId(2L);
            Transfer transfer3 = new Transfer(account2, account3, BigDecimal.ONE, account2.getCurrency(), account3.getCurrency(), BigDecimal.ONE.add(BigDecimal.TEN));
            transfer3.setId(3L);
            List<Transfer> transferList = Stream.of(transfer1, transfer2, transfer3).filter(t -> t.getFromAccountId() == queryAccountId || t.getToAccountId() == queryAccountId).collect(Collectors.toList());
            queryMocked.setResultSet(transferList);
            return queryMocked;
        }).when(entityManager).createQuery(eq("select t from Transfer t where t.from.id = :accountId or t.to.id = :accountId"), eq(Transfer.class));

        final TypedQueryMocked<Long> queryMockedTotalSize = new TypedQueryMocked<>();
        doAnswer(invocationOnMock -> {
            queryMockedTotalSize.setResultSet(Collections.singletonList(2L));
            return queryMockedTotalSize;
        }).when(entityManager).createQuery(eq("select count (t.id) from Transfer t where t.from.id = :accountId or t.to.id = :accountId"), eq(Long.class));
        transferRepository = new TransferRepositoryImpl(entityManager);
        int page = 0;
        int pageSize = 2;
        Page<Transfer> transferPage = transferRepository.findAllByAccountId(queryAccountId, page, pageSize);
        assertNotNull(transferPage);
        assertEquals(page, transferPage.getPage());
        assertEquals(pageSize, transferPage.getPageSize());
        assertEquals(2, transferPage.getTotalSize());
        assertEquals(pageSize, transferPage.getContents().size());
        assertEquals(1, (long) transferPage.getContents().get(0).getId());
    }

    @Test
    void findAll_wrongPageSize_shouldThrowIllegalArgumentException() {
        TransferRepository transferRepository = new TransferRepositoryImpl(entityManager);
        int page = 0;
        int pageSize = 0;
        assertThrows(IllegalArgumentException.class, () -> transferRepository.findAllByAccountId(1L, page, pageSize));
    }

    @Test
    void findAll_ExceptionInCountQuery_shouldThrowDbQueryException() {
        TransferRepository transferRepository = new TransferRepositoryImpl(entityManager);
        int page = 0;
        int pageSize = 2;
        doThrow(RuntimeException.class).when(entityManager).createQuery(eq("select count (t.id) from Transfer t where t.from.id = :accountId or t.to.id = :accountId"), eq(Long.class));
        assertThrows(DbQueryException.class, () -> transferRepository.findAllByAccountId(1L, page, pageSize));
    }

    @Test
    void findAll_ExceptionInListQuery_shouldThrowDbQueryException() {
        TransferRepository transferRepository = new TransferRepositoryImpl(entityManager);
        int page = 0;
        int pageSize = 2;
        doAnswer(invocationOnMock -> {
            TypedQueryMocked<Long> queryMockedTotalSize = new TypedQueryMocked<>();
            queryMockedTotalSize.setResultSet(Collections.singletonList(2L));
            return queryMockedTotalSize;
        }).when(entityManager).createQuery(eq("select count (t.id) from Transfer t where t.from.id = :accountId or t.to.id = :accountId"), eq(Long.class));
        doThrow(RuntimeException.class).when(entityManager).createQuery(eq("select t from Transfer t where t.from.id = :accountId or t.to.id = :accountId"), eq(Transfer.class));
        assertThrows(DbQueryException.class, () -> transferRepository.findAllByAccountId(1L, page, pageSize));
    }
}