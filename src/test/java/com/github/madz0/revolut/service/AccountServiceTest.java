package com.github.madz0.revolut.service;

import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.Currency;
import com.github.madz0.revolut.repository.AccountRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
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
}
