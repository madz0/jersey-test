package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountRepositoryImplUnitTest {
    private EntityManager entityManager;
    private AccountRepository accountRepository;

    @BeforeEach
    public void beforeClass() {
        mockEntityManagerTransaction();
    }

    @Test
    void findById() {
        final long account1Id = 1;
        Account account1 = new Account();
        account1.setAmount(BigDecimal.TEN);
        account1.setCurrency(Currency.USD);
        account1.setId(account1Id);
        doReturn(account1).when(entityManager).find(eq(Account.class), eq(account1Id));
        accountRepository = new AccountRepositoryImpl(entityManager);
        Optional<Account> returnedAccount = accountRepository.findById(account1Id);
        assertTrue(returnedAccount.isPresent());
        assertEquals(account1.getAmount(), returnedAccount.get().getAmount());
    }

    @Test
    void findByIdWithNullId_shouldThrowIllegalArgumentException() {
        accountRepository = new AccountRepositoryImpl(entityManager);
        assertThrows(IllegalArgumentException.class, () -> {
            accountRepository.findById(null);
        });
    }


    @Test
    void findAll() {
        final TypedQueryMocked<Account> queryMocked = new TypedQueryMocked<>();
        doAnswer(invocationOnMock -> {
            Account account1 = new Account();
            account1.setAmount(BigDecimal.TEN);
            account1.setCurrency(Currency.USD);
            account1.setId(1L);
            Account account2 = new Account();
            account2.setAmount(BigDecimal.TEN);
            account2.setCurrency(Currency.EUR);
            account2.setId(2L);
            Account account3 = new Account();
            account3.setAmount(BigDecimal.TEN);
            account3.setCurrency(Currency.EUR);
            account3.setId(3L);
            List<Account> accountList = Arrays.asList(account1, account2, account3);
            queryMocked.setResultSet(accountList);
            return queryMocked;
        }).when(entityManager).createQuery(eq("select a from Account a"), eq(Account.class));
        final TypedQueryMocked<Long> queryMockedTotalSize = new TypedQueryMocked<>();
        doAnswer(invocationOnMock -> {
            queryMockedTotalSize.setResultSet(Collections.singletonList(3L));
            return queryMockedTotalSize;
        }).when(entityManager).createQuery(eq("select count (a.id) from Account a"), eq(Long.class));
        accountRepository = new AccountRepositoryImpl(entityManager);
        int page = 0;
        int pageSize = 2;
        Page<Account> accountPage = accountRepository.findAll(page, pageSize);
        assertNotNull(accountPage);
        assertEquals(page, accountPage.getPage());
        assertEquals(pageSize, accountPage.getPageSize());
        assertEquals(pageSize, accountPage.getPageSize());
        assertEquals(3, accountPage.getTotalSize());
        assertEquals(pageSize, accountPage.getContents().size());
        assertEquals(1, (long) accountPage.getContents().get(0).getId());
    }

    @Test
    void findForUpdateById() {
    }

    private void mockEntityManagerTransaction() {
        EntityTransaction transaction = mock(EntityTransaction.class);
        doNothing().when(transaction).commit();
        doNothing().when(transaction).rollback();
        entityManager = mock(EntityManager.class);
        doReturn(transaction).when(entityManager).getTransaction();
    }
}