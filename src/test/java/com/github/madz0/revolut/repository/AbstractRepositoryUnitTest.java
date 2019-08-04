package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.AbstractUnitTest;
import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

class AbstractRepositoryUnitTest extends AbstractUnitTest {
    private EntityManager entityManager;

    @BeforeEach
    public void beforeEach() {
        entityManager = mockEntityManagerTransaction();
    }

    @Test
    void save() {
        long generatedId = 1L;
        doAnswer(invocationOnMock -> {
            Account account = invocationOnMock.getArgument(0);
            account.setId(generatedId);
            return null;
        }).when(entityManager).persist(any(Account.class));
        final Account account = new Account();
        account.setAmount(BigDecimal.TEN);
        account.setCurrency(Currency.USD);
        AbstractRepository<Account> accountAbstractRepository = new AccountRepositoryImpl(entityManager);
        Account saved = accountAbstractRepository.save(account);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertTrue(saved.getId() > 0);
        assertEquals(generatedId, saved.getId());
    }

    @Test
    void saveMerge() {
        long generatedId = 1L;
        final BigDecimal bigDecimalToChange = BigDecimal.ONE;
        doAnswer(invocationOnMock -> {
            Account account = invocationOnMock.getArgument(0);
            account.setAmount(bigDecimalToChange);
            return null;
        }).when(entityManager).merge(any(Account.class));
        final Account account = new Account();
        account.setAmount(BigDecimal.TEN);
        account.setCurrency(Currency.USD);
        account.setId(generatedId);
        AbstractRepository<Account> accountAbstractRepository = new AccountRepositoryImpl(entityManager);
        Account saved = accountAbstractRepository.save(account);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(bigDecimalToChange, saved.getAmount());
    }

    @Test
    void save_nullEntity_shouldThrow() {
        AbstractRepository<Account> accountAbstractRepository = new AccountRepositoryImpl(entityManager);
        assertThrows(IllegalArgumentException.class, () -> accountAbstractRepository.save(null));
    }

    @Test
    void save_wrongEntityId_shouldThrow() {
        AbstractRepository<Account> accountAbstractRepository = new AccountRepositoryImpl(entityManager);
        Account acc = new Account();
        acc.setId(-1L);
        assertThrows(IllegalArgumentException.class, () -> accountAbstractRepository.save(acc));
    }
}