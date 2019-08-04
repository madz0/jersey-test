package com.github.madz0.revolut.repository;

import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.Currency;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class AccountRepositoryImplIntegrationTest {

    @Inject
    private AccountRepository accountRepository;

    @Test
    void findById() {
        Account account = new Account();
        account.setAmount(BigDecimal.TEN);
        account.setCurrency(Currency.USD);
        accountRepository.save(account);
        assertNotNull(account.getId());
        Optional<Account> db = accountRepository.findById(account.getId());
        assertTrue(db.isPresent());
        assertEquals(db.get().getAmount(), account.getAmount());
    }

    @Test
    void findAll() {
    }

    @Test
    void findForUpdateById() {
    }
}