package com.github.madz0.revolut.service;

import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.Currency;
import com.github.madz0.revolut.repository.AccountRepository;
import com.github.madz0.revolut.repository.Page;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    AccountService accountService;
    AccountRepository accountRepository;

    @Test
    public void getAccountByIdTest() {
        final long generatedId = 1;
        accountRepository = mock(AccountRepository.class);
        doAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            if (id == null || id <= 0 || (id != generatedId)) {
                return invocationOnMock.callRealMethod();
            } else {
                Account account = new Account();
                account.setAmount(BigDecimal.TEN);
                account.setCurrency(Currency.USD);
                account.setId(generatedId);
                return Optional.of(account);
            }
        }).when(accountRepository).findById(Mockito.anyLong());
        accountService = new AccountService(accountRepository);
        Account savedAccount = accountService.findById(generatedId);
        assertNotNull(savedAccount);
        assertEquals(generatedId, (long) savedAccount.getId());
    }

    @Test
    public void saveTest() {
        final long generatedId = 1;
        accountRepository = mock(AccountRepository.class);
        doAnswer(invocationOnMock -> {
            Account acc = invocationOnMock.getArgument(0);
            if (acc == null) {
                return invocationOnMock.callRealMethod();
            } else {
                acc.setId(generatedId);
                return acc;
            }
        }).when(accountRepository).save(Mockito.any(Account.class));
        accountService = new AccountService(accountRepository);
        Account account = new Account();
        account.setAmount(BigDecimal.TEN);
        account.setCurrency(Currency.USD);
        Account savedAccount = accountService.save(account);
        assertNotNull(savedAccount);
        assertEquals(generatedId, (long) savedAccount.getId());
    }

    @Test
    public void findAllTest() {
        accountRepository = mock(AccountRepository.class);
        doAnswer(invocationOnMock -> {
            int page = invocationOnMock.getArgument(0);
            int size = invocationOnMock.getArgument(1);
            if (page < 0 || size <= 0) {
                return invocationOnMock.callRealMethod();
            } else {
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
                return new Page<>(accountList.subList(page, page + size), accountList.size(), page, size);
            }
        }).when(accountRepository).findAll(anyInt(), anyInt());
        accountService = new AccountService(accountRepository);
        int page = 0;
        int pageSize = 2;
        Page<Account> accountPage = accountService.findAll(page, pageSize);
        assertNotNull(accountPage);
        assertEquals(page, accountPage.getPage());
        assertEquals(pageSize, accountPage.getPageSize());
        assertEquals(pageSize, accountPage.getPageSize());
        assertEquals(3, accountPage.getTotalSize());
        assertEquals(pageSize, accountPage.getContents().size());
        assertEquals(1, (long) accountPage.getContents().get(0).getId());
    }
}
